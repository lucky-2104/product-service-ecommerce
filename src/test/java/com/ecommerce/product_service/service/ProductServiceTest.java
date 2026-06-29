package com.ecommerce.product_service.service;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.ProductRepository;



@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
	
	@Mock 
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	
	
	@Test
	public void getAllProduct_shouldReturn200() {
		
		//3 A`s arrange act and assert and then can verify too
		
		
		String productName="Anything";
		String productDescription="This is product Description";
		
		//Arrange
		Product product = Product
				.builder()
				.name(productName)
				.description(productDescription)
				.isActive(true)
				.stockQuantity(20)
				.category(Category.ELECTRONICS)
				.build();
 		
		//Act+assert
		when(productRepository.findByIsActiveTrue()).thenReturn(List.of(product));
		
		
		
		
		
	}

}

