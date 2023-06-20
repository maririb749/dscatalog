package com.mariana.dscatalog.repositoryes;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mariana.dscatalog.entities.Product;
import com.mariana.dscatalog.repositories.ProductRepository;
import com.mariana.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	
	@Autowired
	private ProductRepository repository;
	
	private long exintingId;
	private long nonExintingId;
	private long countTotalProducts;
	
	
	@BeforeEach
	void setUp() throws Exception{
		      exintingId = 1L;
		      countTotalProducts = 25L;
		
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
		
	}
	
	
	@Test
	public void deleteShowldDeleteObjectWhenIdExists() {
		
		repository.deleteById(exintingId);
		
		Optional<Product> result = repository.findById(exintingId);
		
		Assertions.assertFalse(result.isPresent());
		
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
		
		 Optional<Product> result = repository.findById(exintingId);
		 
		 Assertions.assertTrue(result.isPresent());
		
	}
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExists() {
		
		 Optional<Product> result = repository.findById(nonExintingId);
		 
		 Assertions.assertTrue(result.isEmpty());
		
	}

}
