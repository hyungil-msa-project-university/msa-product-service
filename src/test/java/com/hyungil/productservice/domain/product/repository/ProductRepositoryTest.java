package com.hyungil.productservice.domain.product.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.hyungil.productservice.domain.product.domain.entity.Product;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
	@DisplayName("상품 Entity를 데이터베이스에 저장 성공")
	void save() {

		productRepository.save(product);

		Product saveProduct = productRepository.findByProductId(product.getProductId()).get();

		assertThat(product).isEqualTo(saveProduct);
	}

	@Test
	@DisplayName("상품 Entity를 데이터베이스에 저장 실패")
	void saveFailureTest() {

		try {
			productRepository.save(null);
		} catch (InvalidDataAccessApiUsageException e) {
			assertNotNull(e);
		}
	}

	@Test
	@DisplayName("상품 엔티티의 아이디로 검색해서 데이터베이스에서 상품 가져오기 성공")
	public void findByProductIdSuccessTest() {

		Product savedProduct = productRepository.save(product);

		Optional<Product> productFindByName = productRepository
			.findByProductId(product.getProductId());

		productFindByName.ifPresent(
			value -> assertEquals(savedProduct.getProductName(), value.getProductName()));
	}

	@Test
	@DisplayName("상품 엔티티의 아이디로 검색해서 데이터베이스에서 상품 가져오기 실패")
	public void findByProductIdFailureTest() {

		Optional<Product> productFindByName = productRepository.findByProductId(1L);

		assertEquals(Optional.empty(), productFindByName);
	}

	@Test
	@DisplayName("상품 엔티티의 아이디로 검색해서 데이터베이스에서 상품 지우기 성공")
	public void deleteByIdSuccessTest() {

		productRepository.save(product);

		try {
			productRepository.deleteById(1L);
		} catch (EmptyResultDataAccessException e) {
			assertNotNull(e);
		}

		Optional<Product> deletedProduct = productRepository.findByProductId(1L);

		assertThat(deletedProduct).isEmpty();
	}

	@Test
	@DisplayName("상품 엔티티의 아이디로 검색해서 데이터베이스에서 상품 지우기 실패")
	public void deleteByIdFailureTest() {

		try {
			productRepository.deleteById(1L);
		} catch (EmptyResultDataAccessException e) {
			assertNotNull(e);
		}

	}

}