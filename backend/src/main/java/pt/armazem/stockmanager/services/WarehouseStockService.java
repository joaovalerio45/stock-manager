  package pt.armazem.stockmanager.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Item;
import pt.armazem.stockmanager.domain.entities.Warehouse;
import pt.armazem.stockmanager.domain.entities.WarehouseStock;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.WarehouseStockRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseStockService {

    private final WarehouseStockRepository warehouseStockRepository;
    private final WarehouseService warehouseService;
    private final ItemService itemService;

    public WarehouseStock getWarehouseStock(Long warehouseId, Long itemId) {
        return warehouseStockRepository.findByWarehouseIdAndItemId(warehouseId, itemId)
            .orElseThrow(() -> new ResourceNotFoundException("No stock found for item " + itemId + " in warehouse " + warehouseId + "."));
    }

    public List<WarehouseStock> getStocksByWarehouseId(Long warehouseId) {
        warehouseService.getWarehouseById(warehouseId);
        return warehouseStockRepository.findByWarehouseId(warehouseId);
    }

    public BigDecimal getStockQuantity(Long warehouseId, Long itemId) {
        return getWarehouseStock(warehouseId, itemId).getCurrentStock();
    }

    public WarehouseStock addStock(Warehouse warehouse, Item item, BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        WarehouseStock ws = warehouseStockRepository.findByWarehouseIdAndItemIdForUpdate(warehouse.getId(), item.getId())
            .orElseGet(() -> {
                WarehouseStock wsNew = new WarehouseStock();
                wsNew.setItem(item);
                wsNew.setWarehouse(warehouse);
                return wsNew;
            });

        ws.setCurrentStock(ws.getCurrentStock().add(quantity));
        return warehouseStockRepository.save(ws);
    }

    public WarehouseStock addStock(Long warehouseId, Long itemId, BigDecimal quantity) {
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
        Item item = itemService.getItemById(itemId);
        return addStock(warehouse, item, quantity);
    }

    public WarehouseStock deductStock(Warehouse warehouse, Item item, BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        WarehouseStock ws = warehouseStockRepository.findByWarehouseIdAndItemIdForUpdate(warehouse.getId(), item.getId())
            .orElseThrow(() -> new ResourceNotFoundException("No stock found for item " + item.getId() + " in warehouse " + warehouse.getId() + "."));

        if (quantity.compareTo(ws.getCurrentStock()) > 0) {
            throw new BusinessRuleException("The quantity must not exceed current stock for item: " + item.getName());
        }

        ws.setCurrentStock(ws.getCurrentStock().subtract(quantity));
        return warehouseStockRepository.save(ws);
    }

    public WarehouseStock deductStock(Long warehouseId, Long itemId, BigDecimal quantity) {
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
        Item item = itemService.getItemById(itemId);
        return deductStock(warehouse, item, quantity);
    }
}
