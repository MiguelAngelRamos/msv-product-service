package com.kibernumacademy.productservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kibernumacademy.productservice.model.Product;
import com.kibernumacademy.productservice.repository.ProductRepository;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  // Buscar por id
  public Optional<Product> getProductById(Long id) {
    return productRepository.findById(id);
  }

  // Guardar un producto
  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  // Listar los productos
  public List<Product> getAllProducts() {
      return productRepository.findAll();
  }
}
