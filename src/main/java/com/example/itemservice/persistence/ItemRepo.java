package com.example.itemservice.persistence;

import com.example.itemservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends JpaRepository<Item, String> {
}
