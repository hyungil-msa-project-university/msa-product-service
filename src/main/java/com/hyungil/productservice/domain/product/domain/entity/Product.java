package com.hyungil.productservice.domain.product.domain.entity;

import com.hyungil.productservice.domain.model.BaseTimeEntity;
import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.dto.request.UpdateProductRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String productName;

	@Builder
	private Product(Long id, String productName) {
		this.id = id;
		this.productName = productName;
	}

	public static Product from(AddProductRequestDto addProductRequestDto) {
		return Product.builder()
			.productName(addProductRequestDto.getProductName())
			.build();
	}

	public void updateProduct(UpdateProductRequestDto updateProductRequestDto) {
		this.productName = updateProductRequestDto.getProductName();
	}

}