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
import java.util.Optional;

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
    public Optional<Item> findById(String id) {

        ObjectId objectId;
        try {
            objectId = new ObjectId(id); // Convert only if it's an ObjectId
        } catch (IllegalArgumentException e) {
            return Optional.empty(); // Invalid ObjectId format
        }

        Item item = itemCollection.find(eq("_id", objectId)).first();
        return Optional.ofNullable(item);
    }

    @Override
    public Item update(String id, Item item) {

        if (item.getId() == null) {
            throw new IllegalArgumentException("Item ID cannot be null for update.");
        }

        ObjectId objectId = new ObjectId(String.valueOf(id));
        var updateQuery = Updates.combine(
                Updates.set("name", item.getName()),
                Updates.set("upc", item.getUpc()),
                Updates.set("price", item.getPrice()),
                Updates.set("imageUrls", item.getImageUrls()),
                Updates.set("stock", item.getStock()),
                Updates.set("updatedAt", new Date())
        );
        var result = itemCollection.updateOne(Filters.eq("_id", objectId), updateQuery, new UpdateOptions().upsert(false));
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
        itemCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

}