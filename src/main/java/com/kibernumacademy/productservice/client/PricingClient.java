package com.kibernumacademy.productservice.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="pricing-service") // Identifica el servicio de precios en Eureka
public interface PricingClient {
  
  @GetMapping("/price/{id}") // End Point del servicio de precios 
  Map<String, Object> getPrice(@PathVariable Long id);
}
