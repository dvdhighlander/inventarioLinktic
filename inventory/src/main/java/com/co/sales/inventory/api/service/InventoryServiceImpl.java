package com.co.sales.inventory.api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.co.sales.inventory.api.client.ProductClient;
import com.co.sales.inventory.api.client.Products;
import com.co.sales.inventory.api.constants.InventoryConstants;
import com.co.sales.inventory.api.model.Inventory;
import com.co.sales.inventory.api.model.InventoryDTO;
import com.co.sales.inventory.api.model.InventoryResponse;
import com.co.sales.inventory.api.repository.InventoryRepository;

import reactor.core.publisher.Mono;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public List<InventoryDTO> getAllInventory(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Inventory> rta = inventoryRepository.findAll(page);
		return rta.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public InventoryDTO getInventoryById(int id) {
		Optional<Inventory> inventoryData = inventoryRepository.findById(id);
		if (!inventoryData.isPresent() || inventoryData.isEmpty()) {
			System.out.println(InventoryConstants.NO_INVENTORY + id);
			return null;
		}
		return convertToDTO(inventoryData.get());
	}

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public InventoryResponse createInventory(Inventory inventory) {
		InventoryResponse response = new InventoryResponse();
		try {
			Inventory newInventory = inventoryRepository.save(inventory);
			response.setInventory(newInventory);
			response.setResponseMessage(InventoryConstants.OK);
			System.out.println(InventoryConstants.INVENTORY_CREATED + newInventory.getProducto_id());
		} catch (Exception e) {
			System.out.println(InventoryConstants.ERROR_CREATING + e.getMessage());
			response.setResponseMessage(InventoryConstants.ERROR_CREATING + e.getMessage());
		}
		return response;
	}

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public InventoryResponse updateInventory(Inventory inventory) {
		InventoryResponse response = new InventoryResponse();
		try {
			Inventory updatedInventory = inventoryRepository.findById(inventory.getProducto_id()).orElseThrow();
			response.setInventory(updatedInventory);
			response.setResponseMessage(InventoryConstants.OK);
			System.out.println(InventoryConstants.INVENTORY_UPDATED + inventory.getProducto_id());
		} catch (NoSuchElementException nex) {
			response.setResponseMessage(InventoryConstants.NO_PRODUCT);
			System.out.println(InventoryConstants.NO_PRODUCT + " " + inventory.getProducto_id());
		} catch (Exception e) {
			response.setResponseMessage(InventoryConstants.ERROR_UPDATE + e.getMessage());
			System.out.println(InventoryConstants.ERROR_UPDATE + e.getMessage());
		}
		return response;
	}

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public InventoryResponse deleteInventory(int id) {
		InventoryResponse response = new InventoryResponse();
		try {
			inventoryRepository.deleteById(id);
			response.setResponseMessage(InventoryConstants.OK);
			System.out.println(InventoryConstants.INVENTORY_DELETED + id);
		} catch (Exception e) {
			response.setResponseMessage(InventoryConstants.ERROR_DELETE + e.getMessage());
			System.out.println(InventoryConstants.ERROR_DELETE + e.getMessage());
		}

		return response;
	}

	private InventoryDTO convertToDTO(Inventory inventory) {
		ProductClient clientProduct = new ProductClient(WebClient.builder());
		Mono<Products> rta = clientProduct.findProductbyId(inventory.getProducto_id());
		if (rta.hasElement().block()) {
			return new InventoryDTO(inventory.getProducto_id(), rta.block().getNombre(), rta.block().getPrecio(),
					inventory.getCantidad());
		} else {
			return null;
		}
	}

}
