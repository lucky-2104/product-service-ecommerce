package com.ecommerce.product_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.product_service.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product,UUID> {
	
	List<Product> findByIsActiveTrue();

	@Modifying
	@Query("UPDATE  Product p SET p.stockQuantity = p.stockQuantity - :quantity "+
			"WHERE p.id = :productId AND p.stockQuantity >= :quantity")
	int decrementStock(@Param("productId") UUID productId , @Param("quantity") Integer quantity);



}
