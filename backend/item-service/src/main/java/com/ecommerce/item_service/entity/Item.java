package com.ecommerce.item_service.entity;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class Item {
    private ObjectId id;
    private String name;
    private String upc;
    private double price;
    private List<String> imageUrls;
    private int stock;
    private Date createdAt;
    private Date updatedAt;

    public Item() {}

    public Item(ObjectId id, String name, String upc, double price, List<String> imageUrls, int stock, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.upc = upc;
        this.price = price;
        this.imageUrls = imageUrls;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // **Getters and Setters**
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // **toString() for debugging**
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", upc='" + upc + '\'' +
                ", price=" + price +
                ", imageUrls=" + imageUrls +
                ", stock=" + stock +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}