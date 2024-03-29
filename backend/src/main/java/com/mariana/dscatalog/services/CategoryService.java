package com.mariana.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mariana.dscatalog.dto.CategoryDTO;
import com.mariana.dscatalog.entities.Category;
import com.mariana.dscatalog.repositories.CategoryRepository;
import com.mariana.dscatalog.services.exceptions.DatabaseException;
import com.mariana.dscatalog.services.exceptions.ResouceNotFoundException;

import jakarta.persistence.EntityNotFoundException;



@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
	    List<Category>list = repository.findAll();
	    return list.stream().map(x -> new CategoryDTO(x)).toList();
	    	
	    }
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResouceNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
		
		
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
		
	}
	
	@Transactional
	public CategoryDTO update(Long id,CategoryDTO dto) {
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		}
		catch (EntityNotFoundException e ) {
			throw new ResouceNotFoundException("Id not foun" + id);
			
		}
		
		
	}

	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResouceNotFoundException("Id not found");
		}
		try {
	        	repository.deleteById(id);    		
		}
	    	catch (DataIntegrityViolationException e) {
	        	throw new DatabaseException("Integrity violation");
	   	}
	}

		
}


