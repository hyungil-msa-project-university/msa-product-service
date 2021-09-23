package com.hyungil.productservice.domain.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	AddProductRequestDto addProductRequestDto = AddProductRequestDto.builder()
		.productName("상품")
		.build();

	@Test
	@DisplayName("상품 등록에 성공한다.")
	void addProduct() {
		productService.addProduct(addProductRequestDto);

		verify(productRepository, times(1)).save(any());
	}
}