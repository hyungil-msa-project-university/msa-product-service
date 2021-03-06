package com.hyungil.productservice.domain.product.api;

import com.hyungil.productservice.domain.product.dto.request.AddProductRequestDto;
import com.hyungil.productservice.domain.product.dto.request.UpdateProductRequestDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductResponseDto;
import com.hyungil.productservice.domain.product.dto.response.GetProductsResponseDto;
import com.hyungil.productservice.domain.product.service.ProductService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductApi {

	private final ProductService productService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addProduct(@Valid @RequestBody AddProductRequestDto addProductRequestDto) {
		productService.addProduct(addProductRequestDto);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public GetProductResponseDto getProduct(@PathVariable Long id) {
		return productService.getProduct(id);
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<GetProductsResponseDto> getProductsByPagination(
		@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
		return productService.getProductsByPagination(pageable);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public void updateProduct(@PathVariable Long id,
		@RequestBody UpdateProductRequestDto updateProductRequestDto) {
		productService.updateProduct(id, updateProductRequestDto);
	}

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
	}
}