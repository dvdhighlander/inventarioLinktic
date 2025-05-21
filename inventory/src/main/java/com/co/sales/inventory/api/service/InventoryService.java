package com.co.sales.inventory.api.service;

import java.util.List;

import com.co.sales.inventory.api.model.Inventory;
import com.co.sales.inventory.api.model.InventoryDTO;
import com.co.sales.inventory.api.model.InventoryResponse;

public interface InventoryService {
	public abstract List<InventoryDTO> getAllInventory(int pageNumber, int pageSize);
	public abstract InventoryDTO getInventoryById(int id);
	public abstract InventoryResponse createInventory(Inventory  inventory);
	public abstract InventoryResponse updateInventory(Inventory inventory);
	public abstract InventoryResponse deleteInventory(int id);
}
