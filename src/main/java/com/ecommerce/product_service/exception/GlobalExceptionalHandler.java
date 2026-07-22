package com.ecommerce.product_service.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ecommerce.product_service.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionalHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<?> handlesProductNotFoundException(ProductNotFoundException ex)
	{
		ErrorResponse error = ErrorResponse
				.builder()
				.message(ex.getMessage())
				.status(HttpStatus.NOT_FOUND.value())
				.timestamp(LocalDateTime.now())
				.build();
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handlesMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getFieldErrors().forEach(fieldError ->
	            errors.put(fieldError.getField(), fieldError.getDefaultMessage())
	    );
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handlesAccessDeniedException(AccessDeniedException ex){
		ErrorResponse error = ErrorResponse
				.builder()
				.message(ex.getMessage())
				.status(HttpStatus.FORBIDDEN.value())
				.timestamp(LocalDateTime.now())
				.build();
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
		
		
		
	}
	@ExceptionHandler(InsufficientInventoryException.class)
	public ResponseEntity<?> handlesInsufficientInventoryException(InsufficientInventoryException ex){
		ErrorResponse error = ErrorResponse.builder()
				.message(ex.getMessage())
				.status(HttpStatus.CONFLICT.value())
				.timestamp(LocalDateTime.now())
				.build();
		return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handlesGenericException(Exception ex){
		

		ErrorResponse error = new ErrorResponse(
				
				"Unexpected Error Occurred : " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				LocalDateTime.now()
				);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		
	}
	
}
