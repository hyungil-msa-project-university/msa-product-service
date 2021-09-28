package com.hyungil.productservice.domain.product.repository;

import com.hyungil.productservice.domain.product.domain.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findById(Long id);

}
