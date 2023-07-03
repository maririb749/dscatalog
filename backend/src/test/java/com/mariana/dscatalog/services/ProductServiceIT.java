package com.mariana.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mariana.dscatalog.repositories.ProductRepository;
import com.mariana.dscatalog.services.exceptions.ResouceNotFoundException;

@SpringBootTest
public class ProductServiceIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProduct;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProduct = 25L;
	
	}
	
   @Test
	public void deleteShoudDeleteResouceWhenIdExists() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProduct -1, repository.count());
		
	}
   
   @Test
 	public void deleteShoudThrowResouceNotFoundExceptionWhenIdDoesNotExists() {
 		
 		Assertions.assertThrows(ResouceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
 	}
	
	
}
