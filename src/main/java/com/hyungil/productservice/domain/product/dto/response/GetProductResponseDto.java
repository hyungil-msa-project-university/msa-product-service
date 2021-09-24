package com.hyungil.productservice.domain.product.dto.response;

import com.hyungil.productservice.domain.product.domain.entity.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetProductResponseDto {
    private String productName;

    @Builder
    public GetProductResponseDto(String productName){
        this.productName = productName;
    }

    public static GetProductResponseDto from(Product product) {
        return GetProductResponseDto.builder()
                .productName(product.getProductName())
                .build();
    }
}
