package master.ao.storage.core.domain.repositories;

import master.ao.storage.core.domain.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
            "set s.cust= :#{#stock.cust}, " +
            "s.quantity= :#{#stock.quantity}," +
            "s.lastUpdateAt= :#{#stock.lastUpdateAt} " +
            "where s.product.productId= :#{#stock.product.productId} " +
            "and s.lote= :#{#stock.lote} " +
            "and s.expirationDate= :#{#stock.expirationDate} " +
            "and s.storage.storageId= :#{#stock.storage.storageId}")
    void updateStockExistence(@Param("stock") Stock stock);

    /*
    * @Param("cust") BigDecimal cust,
                              @Param("quantity") UUID quantity,
                              @Param("lastUpdatedAt") LocalDateTime lastUpdatedAt,
                              @Param("productId") UUID productId,
                              @Param("lote") String lote,
                              @Param("expirationDate") LocalDate expirationDate,
                              @Param("storageId") UUID storageId)
    * */
}