package com.ecommerce.item_service.dao;
import com.ecommerce.item_service.dao.ItemRepository;
import com.ecommerce.item_service.entity.Item;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@Repository
public class MongoDbItemRepository implements ItemRepository {

    private final MongoClient client;
    private MongoCollection<Item> itemCollection;
    @Value("${spring.data.mongodb.database}")  // Inject database name
    private String databaseName;

    public MongoDbItemRepository(MongoClient mongoClient) {
        this.client = mongoClient;
    }

    @PostConstruct
    void init() {
        itemCollection = client.getDatabase(databaseName).getCollection("items", Item.class);
    }

    @Override
    public Item save(Item item) {
        item.setId(new ObjectId());
        itemCollection.insertOne(item);
        return item;
    }

    @Override
    public List<Item> findAll() {
        return itemCollection.find().into(new ArrayList<>());
    }

    @Override
    public Item findOne(String id) {
        System.out.println("in find One + " + id);

        return itemCollection.find(eq("_id", new ObjectId(id))).first();
    }

    @Override
    public Item update(String id, Item item) {
        System.out.println("In update item repository");

        if (item.getId() == null) {
            throw new IllegalArgumentException("Item ID cannot be null for update.");
        }

        ObjectId objectId = new ObjectId(String.valueOf(id));
        System.out.println("Item ID: " + objectId);
        var updateQuery = Updates.combine(
                Updates.set("name", item.getName()),
                Updates.set("upc", item.getUpc()),
                Updates.set("price", item.getPrice()),
                Updates.set("imageUrls", item.getImageUrls()),
                Updates.set("stock", item.getStock()),
                Updates.set("updatedAt", new Date())
        );
        System.out.println("Update Query: " + updateQuery);
        var result = itemCollection.updateOne(Filters.eq("_id", objectId), updateQuery, new UpdateOptions().upsert(false));
        System.out.println("Result: " + result);
        if (result.getModifiedCount() > 0) {
            return item;
        } else {
            return null;
        }
    }

    @Override
    public long deleteAll() {
        return itemCollection.deleteMany(new Document()).getDeletedCount();
    }

    @Override
    public void deleteOne(String id) {
        DeleteResult result = itemCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
//        return result.getDeletedCount();
    }

}