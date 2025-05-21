package com.co.sales.products.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.co.sales.products.api.model.Products;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer>{

}
