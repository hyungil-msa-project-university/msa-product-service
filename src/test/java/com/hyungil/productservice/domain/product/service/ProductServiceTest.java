package com.hyungil.productservice.domain.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hyungil.productservice.domain.product.domain.entity.Product;
import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.dto.request.UpdateProductRequestDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductResponseDto;
import com.hyungil.productservice.domain.product.repository.ProductRepository;
import com.hyungil.productservice.global.error.exception.NotFoundRequestException;
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

	UpdateProductRequestDto updateProductRequestDto = UpdateProductRequestDto.builder()
		.productName("변경된 상품")
		.build();

	@Test
	@DisplayName("상품 등록에 성공한다.")
	void addProduct() {

		productService.addProduct(addProductRequestDto);

		verify(productRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("특정 상품 조회에 성공한다.")
	void getProduct() {

		Long id = 1L;

		when(productRepository.findByProductId(1L)).thenReturn(Optional.of(product));

		GetProductResponseDto getProductResponseDto = productService.getProduct(id);

		assertThat(getProductResponseDto.getProductName()).isEqualTo("상품");
	}

	@Test
	@DisplayName("특정 id를 가진 상품이 존재하지 않아 조회에 실패한다.")
	void updateIfProductNotFound() {

		Long id = 1L;

		when(productRepository.findByProductId(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundRequestException.class,
			() -> productService.updateProduct(id, updateProductRequestDto));

		verify(productRepository, times(1)).findByProductId(id);
	}

	@Test
	@DisplayName("상품 수정에 성공한다.")
	void updateProduct() {

		Long id = 1L;

		when(productRepository.findByProductId(id)).thenReturn(Optional.of(product));

		productService.updateProduct(id, updateProductRequestDto);

		assertThat(product.getProductName()).isEqualTo(updateProductRequestDto.getProductName());

		verify(productRepository, atLeastOnce()).findByProductId(1L);
	}

	@Test
	@DisplayName("특정 id를 가진 상품이 존재하지 않아 조회에 실패한다.")
	public void getProductIfNotFound() {

		Long id = 1L;

		given(productRepository.findByProductId(id)).willReturn(Optional.empty());

		assertThrows(NotFoundRequestException.class, () -> productService.getProduct(id));

		verify(productRepository, times(1)).findByProductId(id);
	}

}