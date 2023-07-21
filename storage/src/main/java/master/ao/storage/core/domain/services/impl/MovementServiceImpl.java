package master.ao.storage.core.domain.services.impl;

import lombok.RequiredArgsConstructor;
import master.ao.storage.core.domain.enums.DevolutionType;
import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.exceptions.MovementNotFoundException;
import master.ao.storage.core.domain.exceptions.UserNotFoundException;
import master.ao.storage.core.domain.models.Entities;
import master.ao.storage.core.domain.models.ItemsMovement;
import master.ao.storage.core.domain.models.Movement;
import master.ao.storage.core.domain.models.Stock;
import master.ao.storage.core.domain.repositories.ItemsMovementRepository;
import master.ao.storage.core.domain.repositories.MovementRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final UserService userService;
    private final UserRepository userRepository;
    private final StockService stockService;
    private final ItemsMovementRepository itemsMovementRepository;

    @Transactional
    @Override
    public Movement save(Movement movement, UUID userId) {

        validateMovement(movement,userId);
        validateItems(movement);

        movement.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
        movement.calculateTotalValue();
        movement.setMovementStatus();
        Movement movementSaved =  movementRepository.save(movement);
        return  movementSaved;
    }
    private void stockMovementation(Movement movementSaved) {

       /* if (isCritical()) {

        }*/
        //launchAlerts();
    }

    @Override
    public List<Movement> listAndFilterAllMovements(Specification<Movement> spec, UUID userId) {

        List<Movement> movementList = movementRepository.findAll(spec)
                .stream()
                .sorted(getMovementComparator())
                .collect(Collectors.toList());


        return movementList;
    }

    @Override
    public List<ItemsMovement> listItemsByMovement(UUID movementId, UUID userId) {
        fetchOrFail(movementId);

        return itemsMovementRepository.findAllByMovementId(movementId)
                .stream()
                .collect(Collectors.toList());
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

            if (!movement.getMovementType().equals(MovementType.REQUEST)
                    && !movement.getMovementType().equals(MovementType.ORDER) ) {
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

    private void validateMovement(Movement movement,UUID userId) {
        Entities entity = new Entities();
        if (movement != null && movement.getEntity() != null) {
             entity = entityService.fetchOrFail(movement.getEntity().getEntityId()).get();
        }
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));


        movement.setUserGroup(user.getGroupId());
        movement.setEntity(entity);
        movement.setUserId(user.getUserId());
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
