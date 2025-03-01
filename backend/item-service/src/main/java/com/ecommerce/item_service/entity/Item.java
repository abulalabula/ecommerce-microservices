package com.ecommerce.item_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private ObjectId id;
    private String name;
    private String upc;
    private double price;
    private List<String> imageUrls;
    private int stock;
    private Date createdAt;
    private Date updatedAt;
}