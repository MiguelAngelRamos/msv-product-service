package com.kibernumacademy.productservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kibernumacademy.productservice.client.PricingClient;
import com.kibernumacademy.productservice.model.Product;
import com.kibernumacademy.productservice.service.ProductService;

import feign.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/products")
public class ProductController {
  
  private final ProductService productService;
  private final PricingClient pricingClient;

  public ProductController(ProductService productService, PricingClient pricingClient) {
    this.productService = productService;
    this.pricingClient = pricingClient;
  }

  // Obtiene todos los productos
  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  // Obtiene un producto por su ID y llama al al micro-servicio de precios (pricing)
  @GetMapping("/{id}")
  @CircuitBreaker(name = "pricingService", fallbackMethod = "fallbackGetProduct")
  public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    Product product = productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    Map<String, Object> priceResponse = pricingClient.getPrice(id); // obtener el precio del producto al microservicio de precios
    product.setPrice((double) priceResponse.get("price"));
    return ResponseEntity.ok(product);
  }

  // Cuando el microservicio de Precios no responda se ejecuta este método
  public ResponseEntity<Product> fallbackGetProduct(Long id, Throwable throwable) {
    Product product = new Product();
    product.setId(id);
    product.setName("Nombre de precio desconocido");
    product.setDescription("Microservicio de precios no disponible");
    product.setPrice(0.0);

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
           .body(product);
    // return ResponseEntity.ok(product);
  }

  // crear el producto 
  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product saveProduct = productService.saveProduct(product);
    return ResponseEntity.ok(saveProduct);
  }
  
}
