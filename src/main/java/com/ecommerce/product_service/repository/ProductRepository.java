package com.ecommerce.product_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.product_service.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product,UUID> {
	
	List<Product> findByIsActiveTrue();


}
