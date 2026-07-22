package com.ecommerce.product_service.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ecommerce.product_service.event.OrderItemEvent;
import com.ecommerce.product_service.event.OrderPlacedEvent;
import com.ecommerce.product_service.event.OrderStatusEvent;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
@Slf4j
public class ProductService {

	
	private final ProductRepository productRepository;
	private final ProductEventPublishService productEventService;
	private final CacheManager cacheManager;
	//Helper Functions
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
	
	// Retrieve all product that are set as TRUE.(not soft deleted)
	public List<ProductResponse> getAllProduct(){
		
		List<Product> allProductsThatAreActive = productRepository.findByIsActiveTrue();

		return allProductsThatAreActive.stream().map(this::mapProductToProductResponse).collect(Collectors.toList());
	}

	//Get Product by particular ID
	@Cacheable(
			value="productCache",
			key="#id",
			unless = "#result == null"
	)
	public ProductResponse getProductById(UUID id) {

		log.info("Fetching the product from DB with ID : {}",id);
		Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Can`t find Product with ID : "+id));
		
		if (!product.isActive()) {
	        throw new ProductNotFoundException("Can't find Product with ID: " + id);
	    }
		return mapProductToProductResponse(product);
	}

	//Creating / Adding new product to database.
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

	//Updating product(need to pass the UpdateProductRequest-> name ,description , price , stock , category )
	@CachePut(
			value = "productCache",
			key="#id",
			unless = "#result == null"
	)
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

	//Deleted product (Setting isActive = False i.e. soft Deleting product)
	@CacheEvict(
			value="productCache",
			key="#id"
	)
	public void deleteProduct(UUID id) {
		Product fetchedProduct = productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Can`t find Product with ID : "+id));
		
		fetchedProduct.setActive(false);
		
		productRepository.save(fetchedProduct);
		
		
	}

	//Used to update the quantity whenever an order is created by user in Order-service .
	@Transactional
    public void reduceInventory(OrderPlacedEvent event) {

		//  Validate
		for (OrderItemEvent item : event.items()) {

			Product product = productRepository.findById(item.productId())
					.orElseThrow(() ->
							new ProductNotFoundException(
									"Product not found : " + item.productId()));

			if (!product.isActive()) {
				productEventService.publish(
						new OrderStatusEvent(event.orderId(), "ORDER_FAILED"));
				return;
			}

			if (product.getStockQuantity() < item.quantity()) {
				productEventService.publish(
						new OrderStatusEvent(event.orderId(), "ORDER_FAILED"));
				return;
			}
		}

		// Reduce Inventory
		Cache cache = cacheManager.getCache("productCache");

		for (OrderItemEvent item : event.items()) {

			productRepository.decrementStock(
					item.productId(),
					item.quantity());

			if (cache != null) {
				cache.evict(item.productId());
			}
		}

		// Publish Success
		productEventService.publish(
				new OrderStatusEvent(event.orderId(), "ORDER_COMPLETED"));
    }
}
