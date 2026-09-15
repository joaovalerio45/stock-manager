package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.WarehouseStock;
import pt.armazem.stockmanager.services.WarehouseStockService;

@RestController
@RequestMapping("/api/warehouse-stocks")
@RequiredArgsConstructor
public class WarehouseStockController {

    private final WarehouseStockService warehouseStockService;

    @GetMapping("/{warehouseId}/items/{itemId}")
    public WarehouseStock fetchWarehouseStockbyId(@PathVariable Long warehouseId, @PathVariable Long itemId) {
        return warehouseStockService.getWarehouseStock(warehouseId, itemId);
    }

    @GetMapping("/{warehouseId}")
    public List<WarehouseStock> fetchStocksByWarehouse(@PathVariable Long warehouseId) {
        return warehouseStockService.getStocksByWarehouseId(warehouseId);
    }
}
