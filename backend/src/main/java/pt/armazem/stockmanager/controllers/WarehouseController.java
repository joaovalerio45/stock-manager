package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Warehouse;
import pt.armazem.stockmanager.services.WarehouseService;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/{id}")
    public Warehouse fetchWarehouseById(@PathVariable Long id){
        return warehouseService.getWarehouseById(id);
    } 

    @GetMapping 
    public List<Warehouse> fetchAllWarehouses(){
        return warehouseService.getAllWarehouses();
    }

}

