package com.kibernumacademy.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kibernumacademy.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
  
}
