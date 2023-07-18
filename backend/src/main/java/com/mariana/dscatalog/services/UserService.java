package com.mariana.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mariana.dscatalog.dto.RoleDTO;
import com.mariana.dscatalog.dto.UserDTO;
import com.mariana.dscatalog.dto.UserInsertDTO;
import com.mariana.dscatalog.dto.UserUpdateDTO;
import com.mariana.dscatalog.entities.Role;
import com.mariana.dscatalog.entities.User;
import com.mariana.dscatalog.projections.userDetailsProjection;
import com.mariana.dscatalog.repositories.RoleRepository;
import com.mariana.dscatalog.repositories.UserRepository;
import com.mariana.dscatalog.services.exceptions.DatabaseException;
import com.mariana.dscatalog.services.exceptions.ResouceNotFoundException;

import jakarta.persistence.EntityNotFoundException;



@Service
public class UserService implements UserDetailsService{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable){
	    Page<User>list = repository.findAll(pageable);
	    return list.map(x -> new UserDTO(x));
	    	
	    }
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResouceNotFoundException("Entity not found"));
		return new UserDTO(entity);
		
		
	}
	
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
		
	}
	
	

	@Transactional
	public UserDTO update(Long id,UserUpdateDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		}
		catch (EntityNotFoundException e ) {
			throw new ResouceNotFoundException("Id not foun" + id);
			
		}
		
		
	}
    @Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if(!repository.existsById(id)) {
			throw new ResouceNotFoundException("resource not found");
		}
		try {
	        repository.deleteById(id);    		
		}
		catch (DataIntegrityViolationException e) {
	        	throw new DatabaseException("Integrity violation");
	   	}
    }
		
		private void copyDtoToEntity(UserDTO dto, User entity) {
			
			 entity.setFirstName(dto.getFirstName());
			 entity.setLastName(dto.getLastName());
			 entity.setEmail(dto.getEmail());
			 
			 entity.getRoles().clear();
			 for (RoleDTO roletDto : dto.getRoles()) {
				 Role role = roleRepository.getReferenceById(roletDto.getId());
				 entity.getRoles().add(role);
				 
			 }
		}

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         
			List<userDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
            if (result.size() == 0) {
            	throw new UsernameNotFoundException("Email not found");
            }
			
			User user = new User();
			user.setEmail(result.get(0).getUserName());
			user.setPassword(result.get(0).getPassword());
			for (userDetailsProjection projection : result) {
				user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
			}
			return user;
		}

	}

		



