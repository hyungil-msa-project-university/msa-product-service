package com.hyungil.productservice.domain.product.service;

import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.domain.entity.Product;
import com.hyungil.productservice.domain.product.repository.ProductRepository;
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
}