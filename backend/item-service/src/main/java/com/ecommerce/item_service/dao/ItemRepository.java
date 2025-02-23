package com.ecommerce.item_service.dao;

import com.ecommerce.item_service.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository {
    Item save(Item item);
    List<Item> findAll();
    Item findOne(String id);
    Item update(String id, Item item);
    long deleteAll();
    void deleteOne(String id);
}