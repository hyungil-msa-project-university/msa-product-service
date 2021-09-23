package com.hyungil.productservice.domain.product.domain.entity;

import com.hyungil.productservice.domain.BaseTimeEntity;
import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
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
	private Long productId;

	private String productName;

	@Builder
	private Product(Long productId, String productName) {
		this.productId = productId;
		this.productName = productName;
	}

	public static Product from(AddProductRequestDto addProductRequestDto) {
		return Product.builder()
			.productName(addProductRequestDto.getProductName())
			.build();
	}

}