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
import master.ao.storage.core.domain.repositories.MovementRepository;
import master.ao.storage.core.domain.repositories.UserRepository;
import master.ao.storage.core.domain.services.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final ProductService productService;
    private final EntityService entityService;
    private final LocationService locationService;
    private final UserRepository userRepository;
    private final StockService stockService;

    @Transactional
    @Override
    public Movement save(Movement movement, UUID userId, UUID originStorageId) {

        validateMovement(movement,userId);
        validateItems(movement, originStorageId);

        movement.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
        movement.calculateTotalValue();
        movement.setMovementStatus();
        Movement movementSaved =  movementRepository.save(movement);
        stockMovementation(movementSaved);
        return  movementSaved;
    }
    private void stockMovementation(Movement movementSaved) {

       /* if (isCritical()) {

        }*/
        //launchAlerts();
    }

    @Override
    public List<Movement> listAll() {
        return null;
    }

    private void validateItems(Movement movement,UUID originStorageId ) {
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
                setStockForSave(item, originStorageId);
            }

        });
    }

    private void setStockForSave(ItemsMovement item, UUID originStorageId) {
        var stock = new Stock();
        item.setQuantity(getQuantity(item.getMovement().getMovementType(),item.getQuantity(),item.getMovement().getDevolutionType()));
        BeanUtils.copyProperties(item,stock);

        stockService.saveOrUpdate(stock, item.getMovement().getMovementType(), originStorageId);
    }

    private void validateMovement(Movement movement,UUID userId) {
        Entities entity = new Entities();
        if (movement != null && movement.getEntity() != null) {
             entity = entityService.fetchOrFail(movement.getEntity().getEntityId()).get();
        }
        var user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException(userId));


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
}
