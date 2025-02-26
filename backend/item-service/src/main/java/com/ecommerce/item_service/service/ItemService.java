package com.ecommerce.item_service.service;

import com.ecommerce.item_service.payload.ItemDTO;

import java.util.List;

public interface ItemService {

    ItemDTO createItem(ItemDTO itemDTO);
    List<ItemDTO> getAllItems();
    ItemDTO getItemById(String id);
    int getStockById(String id);
    ItemDTO updateItem(String id, ItemDTO itemDTO);
    ItemDTO updateStock(String id, int quantity);
    void deleteItem(String id);
}
