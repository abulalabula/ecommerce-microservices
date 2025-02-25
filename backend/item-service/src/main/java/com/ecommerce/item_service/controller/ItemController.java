package com.ecommerce.item_service.controller;

import com.ecommerce.item_service.payload.ItemDTO;
import com.ecommerce.item_service.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem( @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.createItem(itemDTO));
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable String id) {
        ItemDTO item = itemService.getItemById(id);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable String id,  @RequestBody ItemDTO itemDTO) {
        ItemDTO updateItem = itemService.updateItem(id, itemDTO);
        return updateItem != null ? ResponseEntity.ok(updateItem) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
