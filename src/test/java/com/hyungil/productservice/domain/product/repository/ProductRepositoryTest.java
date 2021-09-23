package com.hyungil.productservice.domain.product.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.hyungil.productservice.domain.product.domain.entity.Product;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	Product product = Product.builder()
		.productName("상품").build();

	@Test
	void save() {

		productRepository.save(product);

		Product result = productRepository.findById(product.getProductId()).get();

		assertThat(product).isEqualTo(result);
	}
}