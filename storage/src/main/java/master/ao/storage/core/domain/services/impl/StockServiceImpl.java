package master.ao.storage.core.domain.services.impl;

import master.ao.storage.core.domain.enums.MovementType;
import master.ao.storage.core.domain.models.Product;
import master.ao.storage.core.domain.models.Stock;
import master.ao.storage.core.domain.repositories.StockRepository;
import master.ao.storage.core.domain.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository repository;

    @Transactional
    @Override
    public Stock saveOrUpdate(Stock stockRequest, MovementType movementType) {

        // Verificar o tipo do movimento enviado(INPUT/OUTPUT)
        // Verificar existencia do item pelo armazem informado
        // Efetuar a operação de acordo ao tipo do movimento

        Optional<Stock> stocked = null;
        Stock stock = new Stock();
        stock.setAcquisitionDate(stockRequest.getAcquisitionDate());

        if (isEquipment(stockRequest.getProduct())) {
            stocked = repository.existEquipmentOnStock(stockRequest);
            if (stocked.isPresent()) {
                stock = stocked.get();
                stock.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
                stock.setModel(stockRequest.getModel());
                stock.setSerialNumber(stockRequest.getSerialNumber());
                stock.setLifespan(stockRequest.getLifespan());
                stock.setQuantity(movementType.quantityUpdated(stock.getQuantity(), stockRequest.getQuantity()));
            } else {
                stock = repository.save(stockRequest);
            }
        } else {
            stocked = repository.existProductOnStock(stockRequest);
            if (stocked.isPresent()) {
                stock = stocked.get();
                stock.setLastUpdateAt(LocalDateTime.now(ZoneId.of("UTC")));
                stock.setLocation(stockRequest.getLocation());
                stock.setQuantity(movementType.quantityUpdated(stock.getQuantity(), stockRequest.getQuantity()));
                repository.updateStockExistence(stock);
            } else {
                stock = repository.save(stockRequest);
            }
        }

        return stock;
    }

    private boolean isEquipment(Product product) {
        return product.getGroup().getName().contains("EQUIPAMENTO") ? true : false;
    }
}
