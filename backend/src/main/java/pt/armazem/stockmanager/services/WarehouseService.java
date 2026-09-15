package pt.armazem.stockmanager.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.domain.entities.Warehouse;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.WarehouseRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));
    }

    public List<Warehouse> getAllWarehouses(){
        return warehouseRepository.findAll();
    }

    public Warehouse getActiveWarehouseById(Long id) {
        Warehouse warehouse = getWarehouseById(id);
        if (!warehouse.getActive()) {
            throw new BusinessRuleException("Warehouse '" + warehouse.getName() + "' is inactive.");
        }
        return warehouse;
    }
}
