package com.hyungil.productservice.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hyungil.productservice.domain.product.domain.entity.Product;
import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductResponseDto;
import com.hyungil.productservice.domain.product.repository.ProductRepository;
import java.util.Optional;
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

	Product product = Product.builder()
		.productName("상품")
		.build();

	AddProductRequestDto addProductRequestDto = AddProductRequestDto.builder()
		.productName("상품")
		.build();

	@Test
	@DisplayName("상품 등록에 성공한다.")
	void addProduct() {
		productService.addProduct(addProductRequestDto);

		verify(productRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("특정 상품 조회에 성공한다.")
	void getPerson() {
		when(productRepository.findByProductId(1L)).thenReturn(Optional.of(product));

		GetProductResponseDto getProductResponseDto = productService.getProduct(1L);

		assertThat(getProductResponseDto.getProductName()).isEqualTo("상품");
	}
}