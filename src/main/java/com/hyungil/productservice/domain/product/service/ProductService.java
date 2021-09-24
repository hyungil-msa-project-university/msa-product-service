package com.hyungil.productservice.domain.product.service;

import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.domain.entity.Product;
import com.hyungil.productservice.domain.product.dto.response.GetProductResponseDto;
import com.hyungil.productservice.domain.product.repository.ProductRepository;
import com.hyungil.productservice.global.error.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public void addProduct(AddProductRequestDto addProductRequestDto) {
		Product product = Product.from(addProductRequestDto);
		productRepository.save(product);
	}

	@Transactional(readOnly = true)
	public GetProductResponseDto getProduct(Long id) {
		Product product = FindByProductId(id);
		return GetProductResponseDto.from(product);
	}

	private Product FindByProductId(Long id) {
		return productRepository.findByProductId(id)
			.orElseThrow(() -> new InvalidRequestException("존재하지 않는 상품입니다."));
	}
}