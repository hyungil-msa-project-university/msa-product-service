package com.hyungil.productservice.domain.product.dto.response;

import com.hyungil.productservice.domain.product.domain.entity.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetProductsResponseDto {

	private Long productId;
	private String productName;

	@Builder
	public GetProductsResponseDto(Long productId, String productName){
		this.productId = productId;
		this.productName = productName;
	}

	public static GetProductsResponseDto from(Product product) {
		return GetProductsResponseDto.builder()
			.productId(product.getId())
			.productName(product.getProductName())
			.build();
	}

}
