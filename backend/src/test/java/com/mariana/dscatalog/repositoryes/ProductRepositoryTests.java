package com.mariana.dscatalog.repositoryes;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mariana.dscatalog.entities.Product;
import com.mariana.dscatalog.repositories.ProductRepository;

@DataJpaTest
public class ProductRepositoryTests {
	
	
	@Autowired
	private ProductRepository repository;
	
	@Test
	public void deleteShowldDeleteObjectWhenIdExists() {
		
		long exintingId = 1L;
		
		repository.deleteById(exintingId);
		
		Optional<Product> result = repository.findById(exintingId);
		
		Assertions.assertFalse(result.isPresent());
		
	}

}
