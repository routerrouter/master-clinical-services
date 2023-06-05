package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID>, JpaSpecificationExecutor<Stock> {

    @Query(value = "select s from Stock s where s.product.productId= :#{#stock.product.productId} " +
            "and s.location.locationId= :#{#stock.location.locationId} " +
            "and s.expirationDate= :#{#stock.expirationDate}")
    Optional<Stock> existProductOnStock(@Param("stock") Stock stock);

    @Query(value = "select s from Stock s where s.product.productId=:#{#stock.product.productId} " +
            "and s.model=:#{#stock.model} and s.lifespan=:#{#stock.lifespan}")
    Optional<Stock> existEquipmentOnStock(@Param("stock") Stock stock);

    @Modifying
    @Query(value = "update Stock s " +
            "set s.cust= :#{#stock.cust},s.quantity= :#{#stock.quantity} " +
            "where s.product.productId= :#{#stock.product.productId} " +
            "and s.lote= :#{#stock.lote} " +
            "and s.expirationDate= :#{#stock.expirationDate} " +
            "and s.storage.storageId= :#{#stock.storage.storageId}")
    void updateStockExistence(@Param("stock") Stock stock);

    @Query(value = "select s from Stock s where s.storage.storageId =?1")
    List<Stock> findAll(@Param("storageId") UUID storageId);

    @Query(value = "select s from Stock s where s.storage.storageId =?1 and s.product.productId=?2 and s.expirationDate=?3 and s.lote=?4")
    Optional<Stock> findIsProductStocked(@Param("storageId") UUID storageId, @Param("productId") UUID productId, @Param("expirationDate") LocalDate expirationDate, @Param("lote") String lote);

    @Query(value = "select s from Stock s where s.storage.storageId =?1 and s.product.productId=?2 and s.lifespan=?3 and s.model=?4")
    Optional<Stock> findIsEquipmentStocked(@Param("storageId") UUID storageId, @Param("productId") UUID productId, @Param("lifespan") Long lifespan, @Param("model") String model);

}