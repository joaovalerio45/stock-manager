package pt.armazem.stockmanager.repositories;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.armazem.stockmanager.domain.entities.WarehouseStock;

public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, Long> {

    Optional<WarehouseStock> findByWarehouseIdAndItemId(Long warehouseId, Long itemId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ws FROM WarehouseStock ws WHERE ws.warehouse.id = :warehouseId AND ws.item.id = :itemId")
    Optional<WarehouseStock> findByWarehouseIdAndItemIdForUpdate(@Param("warehouseId") Long warehouseId, @Param("itemId") Long itemId);

    List<WarehouseStock> findByWarehouseId(Long warehouseId);
}
