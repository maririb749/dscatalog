package com.mariana.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mariana.dscatalog.repositories.ProductRepository;
import com.mariana.dscatalog.services.exceptions.DatabaseException;
import com.mariana.dscatalog.services.exceptions.ResouceNotFoundException;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	
	@Mock
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenDependentId() {
		
		Assertions.assertThrows(DatabaseException.class,() ->{
			service.delete(dependentId);
		});
		
	}
	
	
	
	
	@Test
	public void deleteShouldIdThrowsResouceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResouceNotFoundException.class,() ->{
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
				
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
			
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

}
