package com.co.sales.products.api.service;

import java.util.List;
import java.util.Optional;

import com.co.sales.products.api.model.Products;
import com.co.sales.products.api.model.ProductsDTO;
import com.co.sales.products.api.model.ProductsResponse;

public interface ProductsService {

	public abstract List<Products> getAllProducts(int pageNumber, int pageSize);
	public abstract Optional<Products> getProductById(int id);
	public abstract ProductsResponse createProduct(ProductsDTO product);
	public abstract ProductsResponse updateProduct(ProductsDTO products, int id);
	public abstract ProductsResponse deleteProduct(int id);
}
