package com.ecommerce.product_service.exception;

import java.util.function.Supplier;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(String message) {
		
		super(message);
	}


}
