package com.example.itemservice;

import com.example.itemservice.model.Item;
import com.example.itemservice.persistence.ItemRepo;
import com.example.itemservice.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import model.ItemDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepo itemRepo;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testCreateItem() {
        ItemDTO itemDTO = new ItemDTO(UUID.randomUUID(),"name", "description", BigDecimal.valueOf(10.0));
        Item savedItem = Item.builder()
                .name(itemDTO.name())
                .description(itemDTO.description())
                .price(itemDTO.price())
                .build();

        when(itemRepo.save(any(Item.class))).thenReturn(savedItem);
        when(objectMapper.convertValue(savedItem, ItemDTO.class)).thenReturn(itemDTO);

        ItemDTO result = itemService.createItem(itemDTO);

        assertEquals(itemDTO, result);
        verify(itemRepo, times(1)).save(any(Item.class));
        verify(objectMapper, times(1)).convertValue(savedItem, ItemDTO.class);
    }

    @Test
    public void testGetAllItems() {
        Item item1 = Item.builder().name("name1").description("description1").price(BigDecimal.valueOf(10.0)).build();
        Item item2 = Item.builder().name("name2").description("description2").price(BigDecimal.valueOf(20.0)).build();
        List<Item> items = Arrays.asList(item1, item2);

        when(itemRepo.findAll()).thenReturn(items);
        when(objectMapper.convertValue(item1, ItemDTO.class)).thenReturn(new ItemDTO(UUID.randomUUID(),"name1", "description1", BigDecimal.valueOf(10.0)));
        when(objectMapper.convertValue(item2, ItemDTO.class)).thenReturn(new ItemDTO(UUID.randomUUID(),"name2", "description2", BigDecimal.valueOf(20.0)));

        List<ItemDTO> result = itemService.getAllItems();

        assertEquals(2, result.size());
        verify(itemRepo, times(1)).findAll();
        verify(objectMapper, times(2)).convertValue(any(Item.class), eq(ItemDTO.class));
    }

    @Test
    public void testGetItemById() {
        String id = "1";
        Item item = Item.builder().name("name").description("description").price(BigDecimal.valueOf(10.0)).build();
        ItemDTO itemDTO = new ItemDTO(UUID.randomUUID(),"name", "description", BigDecimal.valueOf(10.0));

        when(itemRepo.findById(id)).thenReturn(Optional.of(item));
        when(objectMapper.convertValue(item, ItemDTO.class)).thenReturn(itemDTO);

        ItemDTO result = itemService.getItemById(id);

        assertEquals(itemDTO, result);
        verify(itemRepo, times(1)).findById(id);
        verify(objectMapper, times(1)).convertValue(item, ItemDTO.class);
    }

    @Test
    public void testGetItemByIdNotFound() {
        String id = "1";

        when(itemRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(id));
        verify(itemRepo, times(1)).findById(id);
    }

    @Test
    public void testDeleteItemById() {
        String id = "1";
        Item item = Item.builder().name("name").description("description").price(BigDecimal.valueOf(10.0)).build();

        when(itemRepo.findById(id)).thenReturn(Optional.of(item));

        itemService.deleteItemById(id);

        verify(itemRepo, times(1)).findById(id);
        verify(itemRepo, times(1)).delete(item);
    }

}
