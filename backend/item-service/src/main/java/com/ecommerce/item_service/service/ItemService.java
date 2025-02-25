package com.ecommerce.item_service.service;

import com.ecommerce.item_service.payload.ItemDTO;

import java.util.List;

public interface ItemService {

    ItemDTO createItem(ItemDTO itemDTO);
    List<ItemDTO> getAllItems();
    ItemDTO getItemById(String id);
    ItemDTO updateItem(String id, ItemDTO itemDTO);
    void deleteItem(String id);
}
