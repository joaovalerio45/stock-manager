package pt.armazem.stockmanager.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pt.armazem.stockmanager.services.ItemService;
import pt.armazem.stockmanager.domain.entities.Item;
import pt.armazem.stockmanager.dtos.ItemRequest;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public Item fetchItembyID(@PathVariable Long id){
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<Item> fetchAllItems(){
        return itemService.getAllItems();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item receiveItemRequest(@Valid @RequestBody ItemRequest request){
        return itemService.createItem(request);
    }

    @PutMapping("/{id}")
    public Item updateItemById(@PathVariable Long id, @Valid @RequestBody ItemRequest request){
        return itemService.updateItem(id, request);
    }
    
    @PatchMapping("/{id}/toggle-active")
    public Item toggleActiveByID(@PathVariable Long id){
        return itemService.toggleActiveItem(id);
    }
    
}
