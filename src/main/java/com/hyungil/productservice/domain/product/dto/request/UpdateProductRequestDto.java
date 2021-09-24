package com.hyungil.productservice.domain.product.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductRequestDto {
	private String productName;

	@Builder
	public UpdateProductRequestDto(String productName) {
		this.productName = productName;
	}
}
