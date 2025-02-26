package com.ecommerce.item_service.controller;

import com.ecommerce.item_service.payload.ItemDTO;
import com.ecommerce.item_service.service.ItemService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class ItemGraphQLController {

    private final ItemService itemService;

    public ItemGraphQLController(ItemService itemService) {
        this.itemService = itemService;
    }

    @QueryMapping
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }

    @QueryMapping
    public int getAvailableStock(@Argument String id) {
        return itemService.getStockById(id);
    }

    @QueryMapping
    public ItemDTO getItem(@Argument String id) {
        return itemService.getItemById(id);
    }

//    @SchemaMapping
//    public ItemAttributeDTO getItemAttribute(ItemDTO itemDTO) {
//        return itemAttributeService.getItemAttributeById(itemDTO.getAttributeId());
//    }

    @MutationMapping
    public ItemDTO createItem( @Argument String name,
                               @Argument String upc,
                               @Argument double price,
                               @Argument List<String> imageUrls,
                               @Argument int stock) {
        ItemDTO itemDTO = new ItemDTO(null, name, upc, price, imageUrls, stock, new Date(), new Date());
        return itemService.createItem(itemDTO);
    }


    @MutationMapping
    public ItemDTO updateItem(@Argument String id,
                              @Argument String name,
                              @Argument String upc,
                              @Argument double price,
                              @Argument List<String> imageUrls,
                              @Argument int stock) {
        ItemDTO itemDTO = new ItemDTO(id, name, upc, price, imageUrls, stock, new Date(), new Date());
        return itemService.updateItem(id, itemDTO);
    }

    @MutationMapping
    public ItemDTO updateStock(@Argument String id, @Argument int quantity) {
        return itemService.updateStock(id, quantity);
    }

    @MutationMapping
    public Boolean deleteItem(@Argument String id) {
        itemService.deleteItem(id);
        return true;
    }
}
