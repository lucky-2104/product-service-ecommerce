package com.ecommerce.product_service.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ecommerce.product_service.event.OrderItemEvent;
import com.ecommerce.product_service.event.OrderPlacedEvent;
import com.ecommerce.product_service.event.OrderStatusEvent;
import com.ecommerce.product_service.exception.InsufficientInventoryException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.ecommerce.product_service.dto.request.CreateProductRequest;
import com.ecommerce.product_service.dto.request.UpdateProductRequest;
import com.ecommerce.product_service.dto.response.ProductResponse;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	
	private final ProductRepository productRepository;
	private final ProductEventPublishService productEventService;
	
	
	private ProductResponse mapProductToProductResponse(Product product){

		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.stockQuantity(product.getStockQuantity())
				.category(product.getCategory())
				.isActive(product.isActive())
				.build();
	}
	

	public List<ProductResponse> getAllProduct(){
		
		List<Product> allProductsThatAreActive = productRepository.findByIsActiveTrue();

		return allProductsThatAreActive.stream().map(this::mapProductToProductResponse).collect(Collectors.toList());
	}
	
	public ProductResponse getProductById(UUID id) {
		
		Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Can`t find Product with ID : "+id));
		
		if (!product.isActive()) {
	        throw new ProductNotFoundException("Can't find Product with ID: " + id);
	    }
		return mapProductToProductResponse(product);
	}
	
	public ProductResponse createProduct(CreateProductRequest request) {
		
		
		Product product = Product.builder()
				.name(request.name())
				.description(request.description())
				.price(request.price())
				.stockQuantity(request.stockQuantity())
				.category(request.category())
				.build();
		
		Product savedProduct = productRepository.save(product);
		
		return mapProductToProductResponse(savedProduct);
		
	}
	
	public ProductResponse updateProduct(UUID id,UpdateProductRequest request) {
		
		Product fetchedProduct = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Can`t find Product with ID : "+id));
		
		fetchedProduct.setName(request.name());
		fetchedProduct.setPrice(request.price());
		fetchedProduct.setDescription(request.description());
		fetchedProduct.setStockQuantity(request.stockQuantity());
		fetchedProduct.setCategory(request.category());

		
		Product updatedProduct = productRepository.save(fetchedProduct);
		
		return mapProductToProductResponse(updatedProduct);
		
		
	}
	
	public void deleteProduct(UUID id) {
		Product fetchedProduct = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Can`t find Product with ID : "+id));
		
		fetchedProduct.setActive(false);
		
		productRepository.save(fetchedProduct);
		
		
	}


	@Transactional
    public void reduceInventory(OrderPlacedEvent event) {

		for(OrderItemEvent item: event.items()){

			UUID productId = item.productId();
			Integer stock = item.quantity();
			int isValid = productRepository.decrementStock(productId,stock);
			if(isValid == 0){
				productEventService.publish(new OrderStatusEvent(event.orderId(),"ORDER_FAILED"));
			}
			else{
				productEventService.publish(new OrderStatusEvent(event.orderId(),"ORDER_COMPLETED"));
			}
		}


    }
}
