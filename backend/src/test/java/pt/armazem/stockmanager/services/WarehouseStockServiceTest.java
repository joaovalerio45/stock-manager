package pt.armazem.stockmanager.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.armazem.stockmanager.domain.entities.Item;
import pt.armazem.stockmanager.domain.entities.Warehouse;
import pt.armazem.stockmanager.domain.entities.WarehouseStock;
import pt.armazem.stockmanager.exceptions.BusinessRuleException;
import pt.armazem.stockmanager.exceptions.ResourceNotFoundException;
import pt.armazem.stockmanager.repositories.WarehouseStockRepository;

@ExtendWith(MockitoExtension.class)
public class WarehouseStockServiceTest {

    @Mock 
    private WarehouseStockRepository warehouseStockRepository;

    @Mock 
    private WarehouseService warehouseService;

    @Mock 
    private ItemService itemService;

    @InjectMocks 
    private WarehouseStockService warehouseStockService;

    private Warehouse warehouse;
    private Item item;   

    @BeforeEach 
    void setUp(){
        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("TestWarehouse");

        item = new Item();
        item.setId(2L);
        item.setName("TestItem");
    }

    @Test 
    @DisplayName("Stock increase on a stock record that already exists")
    void addStockToStockRecord(){
        WarehouseStock ws = new WarehouseStock();

        ws.setItem(item);
        ws.setWarehouse(warehouse);
        ws.setCurrentStock(new BigDecimal("10"));

        when(warehouseStockRepository.findByWarehouseIdAndItemIdForUpdate(1L,2L))
            .thenReturn(Optional.of(ws));

        when(warehouseStockRepository.save(any(WarehouseStock.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        WarehouseStock result = warehouseStockService.addStock(warehouse,item, new BigDecimal("5"));

        assertNotNull(result);
        assertEquals(result.getCurrentStock(),new BigDecimal( "15"));
        verify(warehouseStockRepository, times(1)).save(ws);
    }

    @Test
    @DisplayName(value = "Adding a invalid quantity to a stock record that already exists")
    void invalidQuantityOnAddStock(){
        BigDecimal quantity = new BigDecimal("-5");
        assertThrows(IllegalArgumentException.class, () -> {
            warehouseStockService.addStock(warehouse, item, quantity);
        });

        verify(warehouseStockRepository, never()).save(any());

    }


    @Test 
    @DisplayName(value = "Create a new stock record for a warehouse/item pair that is still not registered")
    void createStockRecordAddStock(){
        when(warehouseStockRepository.findByWarehouseIdAndItemIdForUpdate(warehouse.getId(), item.getId()))
            .thenReturn(Optional.empty());

        when(warehouseStockRepository.save(any(WarehouseStock.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal quantity = new BigDecimal("10");
        WarehouseStock ws = warehouseStockService.addStock(warehouse, item,quantity);

        assertNotNull(ws);
        assertEquals(quantity, ws.getCurrentStock());
        assertEquals(item, ws.getItem());
        assertEquals(warehouse, ws.getWarehouse());

        verify(warehouseStockRepository, times(1)).save(any(WarehouseStock.class));
    }


    @Test 
    @DisplayName (value = "Stock decrease on a stock record that already exists")
    void deductStockfromStockRecord(){
        WarehouseStock ws = new WarehouseStock();

        ws.setItem(item);
        ws.setWarehouse(warehouse);
        ws.setCurrentStock(new BigDecimal("10"));

        when(warehouseStockRepository.findByWarehouseIdAndItemIdForUpdate(warehouse.getId(), item.getId()))
            .thenReturn(Optional.of(ws));

        when(warehouseStockRepository.save(any(WarehouseStock.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        WarehouseStock result = warehouseStockService.deductStock(warehouse, item, new BigDecimal("6"));

        assertEquals(result.getCurrentStock(), new BigDecimal("4"));
        assertNotNull(result);
        verify(warehouseStockRepository, times(1)).save(any(WarehouseStock.class));
    }

    @Test 
    @DisplayName ("Deduct stock from a stock record that has insufficient quantity")
    void invalidDeductStockQuantity(){
        WarehouseStock ws = new WarehouseStock();

        ws.setItem(item);
        ws.setWarehouse(warehouse);
        ws.setCurrentStock(new BigDecimal("10"));

        when(warehouseStockRepository.findByWarehouseIdAndItemIdForUpdate(warehouse.getId(), item.getId()))
            .thenReturn(Optional.of(ws));

        assertThrows(BusinessRuleException.class, () -> {
            warehouseStockService.deductStock(warehouse, item, new BigDecimal("15"));
        });

        verify(warehouseStockRepository, never()).save(any(WarehouseStock.class));
    }

    @Test 
    @DisplayName("Deduct stock from a warehouse/item pair that doesnt have a stock record yet")
    void deductStockfromNonExistentRecord(){
        BigDecimal quantity = new BigDecimal("10");

        when(warehouseStockRepository.findByWarehouseIdAndItemIdForUpdate(warehouse.getId(), item.getId()))
            .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            warehouseStockService.deductStock(warehouse, item, quantity);
        });

        verify(warehouseStockRepository, never()).save(any(WarehouseStock.class));

    }

    @Test 
    @DisplayName("Deduct an invalid (negative/zero) quantity of stock")
    void deductStockInvalidQuantityRequest(){
        BigDecimal quantity = new BigDecimal("-10");

        assertThrows(IllegalArgumentException.class, () -> {
            warehouseStockService.deductStock(warehouse, item, quantity);
        });

        verify(warehouseStockRepository, never()).save(any(WarehouseStock.class));
    }

    
}
