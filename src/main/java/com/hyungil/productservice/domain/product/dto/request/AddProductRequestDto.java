package com.hyungil.productservice.domain.product.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddProductRequestDto {

	@NotBlank(message = "상품 이름은 필수 입력입니다.")
	private String productName;

	@Builder
	public AddProductRequestDto(String productName) {
		this.productName = productName;
	}
}