package com.ecommerce.product_service.dto.request;

import java.math.BigDecimal;

import com.ecommerce.product_service.entity.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
		
		@NotBlank(message="Name of Product can`t be blank")
		String name,
		
		@NotBlank(message="Include the description for the specified Product")
		@Size(min=10,message="Should include more that 10 characters")
		String description,
		
		@Positive(message = "Price must be strictly greater than 0")
		BigDecimal price,
		
		@Positive(message="Cant be null Set the quantity of product")
		int stockQuantity,
		
		@NotNull(message="Define the category of product")
		Category category
		
		
		) {

}
