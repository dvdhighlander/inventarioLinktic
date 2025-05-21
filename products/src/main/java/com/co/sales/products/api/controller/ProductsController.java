package com.co.sales.products.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.co.sales.products.api.model.Products;
import com.co.sales.products.api.model.ProductsDTO;
import com.co.sales.products.api.model.ProductsResponse;
import com.co.sales.products.api.service.ProductsServiceImpl;

@RestController
@RequestMapping("/Products")
public class ProductsController {

	@Autowired
	private ProductsServiceImpl productService;

	@GetMapping
	@ResponseBody
	public List<Products> getAllProducts(@RequestParam(required = true) int page,
			@RequestParam(required = true) int pageSize) {
		return productService.getAllProducts(page, pageSize);
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Products> getProductById(@PathVariable int id) {
		Optional<Products> product = productService.getProductById(id);
		return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ProductsResponse createProduct(@RequestBody ProductsDTO newProd) {
		return productService.createProduct(newProd);
	}

	@PutMapping("/{id}")
	public ProductsResponse updateProduct(@RequestBody ProductsDTO newProd, @PathVariable int id) {
		return productService.updateProduct(newProd, id);
	}

	@DeleteMapping("/{id}")
	public ProductsResponse deleteProduct(@PathVariable int id) {
		return productService.deleteProduct(id);
	}
}
