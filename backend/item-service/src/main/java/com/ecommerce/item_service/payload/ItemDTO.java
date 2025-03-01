package com.ecommerce.item_service.payload;

import com.ecommerce.item_service.entity.Item;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public record ItemDTO(String id, String name, String upc, double price, List<String> imageUrls, int stock, Date createdAt, Date updatedAt) {

    public ItemDTO(Item item) {
        this(item.getId() == null ? new ObjectId().toHexString() : item.getId().toHexString(),
                item.getName(), item.getUpc(), item.getPrice(), item.getImageUrls(), item.getStock(), item.getCreatedAt(), item.getUpdatedAt());
    }

    public Item toItem() {
        ObjectId objectId = (id == null || id.isEmpty()) ? new ObjectId() : new ObjectId(id);
        return new Item(objectId, name, upc, price, imageUrls, stock, createdAt, updatedAt);
    }

}
