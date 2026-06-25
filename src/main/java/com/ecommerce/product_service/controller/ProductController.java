package com.ecommerce.product_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product_service.dto.request.CreateProductRequest;
import com.ecommerce.product_service.dto.request.UpdateProductRequest;
import com.ecommerce.product_service.dto.response.ProductResponse;
import com.ecommerce.product_service.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

	
	private final ProductService productService; 
	
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts(){
		
		return  ResponseEntity.status(HttpStatus.OK).body(productService.getAllProduct());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(id));
	}
	
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> registerProduct(@RequestBody @Valid CreateProductRequest request){
		
		ProductResponse createdProduct = productService.createProduct(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
		
	}
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductResponse> updateProduct(@RequestBody @Valid UpdateProductRequest request,@PathVariable("id") UUID id){
		
		ProductResponse updatedProduct = productService.updateProduct(id,request);
		
		return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
		
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteProduct(@PathVariable("id") UUID id){
		
		productService.deleteProduct(id);
		
		return ResponseEntity.noContent().build();
		
	}
	
	
}
