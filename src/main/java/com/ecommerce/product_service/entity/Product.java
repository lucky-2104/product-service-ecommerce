package com.ecommerce.product_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="products")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	@Column(name="id",nullable=false,updatable=false)
	private UUID id;
	
	@Column(name="name",nullable=false,updatable=true)
	private String name;
	
	@Column(name="description",nullable=false,updatable=true)
	private String description;
	
	@Column(name="price",nullable=false,updatable=true,precision=10,scale=2)
	private BigDecimal price;
	
	@Column(name="stock_quantity",nullable=false,updatable=true)
	private int stockQuantity;
	
	@Column(name="category",nullable=false,updatable=true)
	@Enumerated(EnumType.STRING)
	private Category category;
	
	@CreationTimestamp
	@Column(name="created_at",nullable=false,updatable=false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name="updated_at",nullable=false)
	private LocalDateTime updatedAt;
	
	@Builder.Default
	@Column(name="is_active",nullable=false)
	private boolean isActive=true;
	
	

}
