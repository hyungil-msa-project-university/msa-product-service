package com.hyungil.productservice.domain.product.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddProductRequestDto {

	@NotBlank(message = "잘못된 상품 이름입니다.")
	private String productName;

	@Builder
	public AddProductRequestDto(String productName) {
		this.productName = productName;
	}
}