package com.hyungil.productservice.domain.product.service;

import static com.hyungil.productservice.global.commom.util.constant.ErrorMessageConstant.NOT_CHANGE_PRODUCT_NAME;
import static com.hyungil.productservice.global.commom.util.constant.ErrorMessageConstant.NOT_FOUND_PRODUCT;

import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.domain.entity.Product;
import com.hyungil.productservice.domain.product.dto.request.UpdateProductRequestDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductResponseDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductsResponseDto;
import com.hyungil.productservice.domain.product.repository.ProductRepository;
import com.hyungil.productservice.global.error.exception.NotFoundRequestException;
import com.hyungil.productservice.global.error.exception.DuplicateRequestException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
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

	@Transactional(readOnly = true)
	public List<GetProductsResponseDto> getProductsByPagination(Pageable pageable) {

		return Stream.of(productRepository.findAll(pageable))
			.flatMap(Streamable::stream)
			.map(GetProductsResponseDto::from)
			.collect(Collectors.toList());
	}


	@Transactional
	public void updateProduct(Long id, UpdateProductRequestDto updateProductRequestDto) {

		Product product = FindByProductId(id);

		duplicateProductNameCheck(product, updateProductRequestDto);

		product.updateProduct(updateProductRequestDto);
	}

	@Transactional
	public void deleteProduct(Long id) {

		FindByProductId(id);

		productRepository.deleteById(id);
	}

	private Product FindByProductId(Long id) {

		return productRepository.findById(id)
			.orElseThrow(() -> new NotFoundRequestException(NOT_FOUND_PRODUCT));
	}

	private void duplicateProductNameCheck(Product product,
		UpdateProductRequestDto updateProductRequestDto) {

		String updateName = updateProductRequestDto.getProductName();
		String productName = product.getProductName();

		if (productName.equals(updateName)) {
			throw new DuplicateRequestException(NOT_CHANGE_PRODUCT_NAME);
		}
	}

}