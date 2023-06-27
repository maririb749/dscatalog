package com.mariana.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mariana.dscatalog.dto.ProductDTO;
import com.mariana.dscatalog.entities.Category;
import com.mariana.dscatalog.entities.Product;
import com.mariana.dscatalog.repositories.CategoryRepository;
import com.mariana.dscatalog.repositories.ProductRepository;
import com.mariana.dscatalog.services.exceptions.DatabaseException;
import com.mariana.dscatalog.services.exceptions.ResouceNotFoundException;
import com.mariana.dscatalog.tests.Factory;

import jakarta.persistence.EntityNotFoundException;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Category category;
	ProductDTO productDTO;
	private Product product;
	
	
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		product = Factory.createProduct();
		category = Factory.createCategory();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		
		
	}
	
	@Test
	public void updateShouldThrowResouceNotFoundExceptionWhenIdDoesNotExist(){
							
		Assertions.assertThrows(ResouceNotFoundException.class,() ->{
			 service.update(nonExistingId, productDTO);
			
		});
	
}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists(){
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists(){
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void findByIdShouldTowResouceNotFoundExceptionWhenIdDoesNotExist(){
				
		Assertions.assertThrows(ResouceNotFoundException.class,() ->{
			 service.findById(nonExistingId);
			
		});
		
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable peageble = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(peageble);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(peageble);
		
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
