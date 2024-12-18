package com.example.itemservice.service;

import com.example.itemservice.model.Item;
import com.example.itemservice.persistence.ItemRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import model.ItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepo itemRepo;

    private final ObjectMapper objectMapper;

    public ItemDTO createItem(ItemDTO itemDTO) {
        var savedItem = itemRepo.save(
                Item.builder()
                        .name(itemDTO.name())
                        .description(itemDTO.description())
                        .price(itemDTO.price())
                        .build()
        );
        return objectMapper.convertValue(savedItem, ItemDTO.class);
    }

    public List<ItemDTO> getAllItems() {
        return itemRepo.findAll().stream().map(item -> objectMapper.convertValue(item, ItemDTO.class)).toList();
    }

    public ItemDTO getItemById(String id) {
        return itemRepo.findById(id).map(item -> objectMapper.convertValue(item, ItemDTO.class)).orElseThrow(() -> new EntityNotFoundException("Item not found"));
    }

    public void deleteItemById(String id) {
        var item = itemRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepo.delete(item);
    }
}