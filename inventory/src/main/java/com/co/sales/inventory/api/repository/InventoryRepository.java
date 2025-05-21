package com.co.sales.inventory.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.co.sales.inventory.api.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{

}
