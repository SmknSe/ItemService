package com.example.itemservice.util;

import com.example.itemservice.model.Item;
import model.ItemDTO;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ItemDTOMapper implements Function<Item, ItemDTO> {
    @Override
    public ItemDTO apply(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getPrice()
        );
    }
}
