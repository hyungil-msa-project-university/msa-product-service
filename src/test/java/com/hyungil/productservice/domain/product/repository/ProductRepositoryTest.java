package com.hyungil.productservice.domain.product.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hyungil.productservice.domain.product.domain.entity.Product;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	Product product = Product.builder()
		.productName("상품").build();

	@Test
	void save() {

		productRepository.save(product);

		Product saveProduct = productRepository.findByProductId(product.getProductId()).get();

		assertThat(product).isEqualTo(saveProduct);
	}

	@Test
	public void findByEmailSuccessTest() {

		Product savedProduct = productRepository.save(product);

		Optional<Product> productFindByName = productRepository
			.findByProductId(product.getProductId());

		productFindByName.ifPresent(
			value -> assertEquals(savedProduct.getProductName(), value.getProductName()));
	}

	@Test
	public void findByEmailFailureTest() {

		Optional<Product> productFindByName = productRepository.findByProductId(1L);

		assertEquals(Optional.empty(), productFindByName);
	}

}