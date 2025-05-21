package com.co.sales.products.api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.co.sales.products.api.constants.ProductsConstants;
import com.co.sales.products.api.model.Products;
import com.co.sales.products.api.model.ProductsDTO;
import com.co.sales.products.api.model.ProductsResponse;
import com.co.sales.products.api.repository.ProductsRepository;

@Service
public class ProductsServiceImpl implements ProductsService {

	@Autowired
	ProductsRepository productsRepository;

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public List<Products> getAllProducts(int pageNumber, int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		Page<Products> rta = productsRepository.findAll(page);
		return rta.getContent();
	}

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public Optional<Products> getProductById(int id) {
		return productsRepository.findById(id);
	}

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public ProductsResponse createProduct(ProductsDTO product) {
		Products newProduct = new Products();
		newProduct.setNombre(product.nombreProducto());
		newProduct.setPrecio(product.precio());
		ProductsResponse response = new ProductsResponse();
		try {
			Products rtaProduct = productsRepository.save(newProduct);
			response.setProduct(rtaProduct);
			response.setResponseMessage(ProductsConstants.OK);
			System.out.println(ProductsConstants.PRODUCT_CREATED + rtaProduct.getId());
		} catch (Exception e) {
			response.setResponseMessage(ProductsConstants.ERROR_CREATING + e.getMessage());
			System.out.println(ProductsConstants.ERROR_CREATING + e.getMessage());
		}
		return response;
	}

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public ProductsResponse updateProduct(ProductsDTO updateProduct, int id) {
		ProductsResponse response = new ProductsResponse();
		try {
			Products product = productsRepository.findById(id).orElseThrow();
			product.setNombre(updateProduct.nombreProducto());
			product.setPrecio(updateProduct.precio());
			Products rtaProduct = productsRepository.save(product);
			response.setProduct(rtaProduct);
			response.setResponseMessage(ProductsConstants.OK);
			System.out.println(ProductsConstants.PRODUCT_UPDATE + rtaProduct.getId());
		} catch (NoSuchElementException nex) {
			response.setResponseMessage(ProductsConstants.NO_PRODUCT);
			System.out.println(ProductsConstants.NO_PRODUCT + " " + id);
		} catch (Exception e) {
			response.setResponseMessage(ProductsConstants.ERROR_UPDATE + e.getMessage());
			System.out.println(ProductsConstants.ERROR_UPDATE + e.getMessage());
		}
		return response;
	}

	@Override
	@Retryable
	@Transactional(timeout = 30)
	public ProductsResponse deleteProduct(int id) {
		ProductsResponse response = new ProductsResponse();
		try {
			productsRepository.deleteById(id);
			response.setResponseMessage(ProductsConstants.OK);
			System.out.println(ProductsConstants.PRODUCT_DELETE + id);
		} catch (Exception e) {
			response.setResponseMessage(ProductsConstants.ERROR_DELETE + e.getMessage());
			System.out.println(ProductsConstants.ERROR_DELETE + e.getMessage());
		}

		return response;
	}

}
