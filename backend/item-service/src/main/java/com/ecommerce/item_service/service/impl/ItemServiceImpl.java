package com.ecommerce.item_service.service.impl;

import com.ecommerce.item_service.dao.ItemRepository;
import com.ecommerce.item_service.entity.Item;
import com.ecommerce.item_service.payload.ItemDTO;
import com.ecommerce.item_service.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final RestTemplate restTemplate;

    public ItemServiceImpl(ItemRepository itemRepository, RestTemplate restTemplate) {
        this.itemRepository = itemRepository;
        this.restTemplate = restTemplate;
    }
    @Override
    public ItemDTO createItem(ItemDTO itemDTO) {

        return new ItemDTO(itemRepository.save(itemDTO.toItem())
        );
    }

    @Override
    public List<ItemDTO> getAllItems() {
        System.out.println("In get all item service impl");
        return itemRepository.findAll().stream().map(ItemDTO::new).toList();
    }

    @Override
    public ItemDTO getItemById(String id) {
        Item item = itemRepository.findOne(id);
        return new ItemDTO(item);
    }

    @Override
    public ItemDTO updateItem(String id, ItemDTO itemDTO) {
        System.out.println("In update item service impl");
        System.out.println("Id: " + id);
        System.out.println("ItemDTO: " + itemDTO);
        return new ItemDTO(itemRepository.update(id, itemDTO.toItem()));
    }

    @Override
    public void deleteItem(String id) {
        itemRepository.deleteOne(id);
    }
}
