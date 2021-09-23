package com.hyungil.productservice.domain.product.controller;

import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.service.ProductService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addProduct(@Valid @RequestBody AddProductRequestDto addProductRequestDto) {
		productService.addProduct(addProductRequestDto);
	}
}