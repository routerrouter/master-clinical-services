package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.api.config.security.AuthenticationCurrentUserService;
import master.ao.storage.core.domain.enums.DevolutionType;
import master.ao.storage.core.domain.enums.ItemStatus;
import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.exceptions.MovementNotFoundException;
import master.ao.storage.core.domain.models.Entities;
import master.ao.storage.core.domain.models.ItemsMovement;
import master.ao.storage.core.domain.models.Movement;
import master.ao.storage.core.domain.models.Stock;
import master.ao.storage.core.domain.repositories.ItemsMovementRepository;
import master.ao.storage.core.domain.repositories.MovementRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.*;
import master.ao.storage.core.domain.utils.Converts;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final ProductService productService;
    private final EntityService entityService;
    private final LocationService locationService;
    private final Converts convert;
    private final AuthenticationCurrentUserService currentUserService;
    private final UtilService utilService;
    private final StockService stockService;
    private final ItemsMovementRepository itemsMovementRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Movement save(Movement movement) {

        validateMovement(movement);

        if (!movement.getMovementType().equals(MovementType.REQUEST)
                && !movement.getMovementType().equals(MovementType.ORDER)
                && !movement.getMovementType().equals(MovementType.DEVOLUTION)) {
            validateItems(movement);
        } else {
            validateItemsRequestAndOrderMovement(movement);
        }

        movement.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
        movement.calculateTotalValue();
        movement.setMovementStatus();

        return  movementRepository.save(movement);
    }


    private void modifyMovementStatus(Movement movement) {
        movementRepository.save(movement);
    }

    @Override
    public List<Movement> listAndFilterAllMovements(Specification<Movement> spec, LocalDate initial, LocalDate end) {

        return movementRepository.findAll(spec)
                .stream()
                .filter(movement -> movement.getMovementDate().isAfter(initial.minusDays(1)))
                .filter(movement -> movement.getMovementDate().isBefore(end.plusDays(1)))
                .filter(movement -> convert.convertUuidToString(movement.getUserGroup())
                        .equals(convert.convertUuidToString(utilService.getUserGroup())))
                .sorted(getMovementComparator())
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemsMovement> listItemsByMovement(UUID movementId) {
        fetchOrFail(movementId);

        return itemsMovementRepository.findAllByMovementId(movementId)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Entities> listEntitiesWithPendingRequest() {
        List<Entities> entities = new ArrayList<>();
        movementRepository.findAll().stream()
                .filter(movement -> movement.getMovementType().toString().equals("REQUEST"))
                .filter(movement -> movement.getMovementStatus().toString().equals("PENDING"))
                .forEach(entity -> {
            entities.add(entity.getEntity());
        });
        return entities
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateRequestMovementStatus(UUID requestId) {
        movementRepository.updateMovementStatus(requestId);
        itemsMovementRepository.updateItemMovementStatus(requestId);
    }

    private void validateItems(Movement movement) {
        movement.getItems().forEach(item -> {
            var product = productService.fetchOrFail(
                    item.getProduct().getProductId()).get();

            var location = locationService.fetchOrFail(
                    item.getLocation().getLocationId()).get();

            item.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
            item.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
            item.setMovement(movement);
            item.setProduct(product);
            item.setLocation(location);

            setStockForSave(item);

        });
    }

    private void validateItemsRequestAndOrderMovement(Movement movement) {
        movement.getItems().forEach(item -> {

            var product = productService.fetchOrFail(
                    item.getProduct().getProductId()).get();

            var location = stockService.findExistenceByProduct(product.getProductId())
                    .stream()
                    .findFirst();

            item.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
            item.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
            item.setMovement(movement);
            item.setProduct(product);
            item.setLocation(location.get().getLocation());

            if (movement.getMovementType().equals(MovementType.DEVOLUTION)){
                setStockForSave(item);
            }

        });
    }

    private void setStockForSave(ItemsMovement item) {
        var stock = new Stock();
        item.setQuantity(getQuantity(item.getMovement().getMovementType(),item.getQuantity(),item.getMovement().getDevolutionType()));
        BeanUtils.copyProperties(item,stock);

        stockService.saveOrUpdate(stock, item.getMovement().getMovementType());
    }

    private void validateMovement(Movement movement) {
        Entities entity = new Entities();
        if (movement != null && movement.getEntity() != null) {
             entity = entityService.fetchOrFail(movement.getEntity().getEntityId()).get();
        }

        movement.setUserGroup(utilService.getUserGroup());
        movement.setEntity(entity);
        movement.setUser(userRepository.findById(currentUserService.getCurrentUser().getUserId()).get()); // rever possivel bug
    }

    public Optional<Movement> fetchOrFail(UUID movementId) {
        var movement = movementRepository.findById(movementId)
                .orElseThrow(() -> new MovementNotFoundException(movementId));

        return Optional.of(movement);
    }

    private Long getQuantity(MovementType type, Long quantity, DevolutionType devolutionType) {
        if (type.equals(MovementType.DEVOLUTION) && DevolutionType.EXTERNAL.equals(devolutionType)) {
            return -quantity;
        } else {
            return quantity;
        }
    }

    private Comparator<Movement> getMovementComparator() {
        return (movement1, movement2) ->
                movement1.getMovementDate().
                compareTo(movement2.getMovementDate());
    }
}
