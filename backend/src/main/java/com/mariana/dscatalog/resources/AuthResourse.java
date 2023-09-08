package com.mariana.dscatalog.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mariana.dscatalog.dto.EmailDTO;
import com.mariana.dscatalog.dto.NewPasswordDTO;
import com.mariana.dscatalog.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResourse {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping(value = "/recover-token")
	public ResponseEntity<Void> createRcoverToken(@Valid @RequestBody EmailDTO body){
		  authService.createRecoverToken(body);
		  return ResponseEntity.noContent().build();
		
	}
	@PutMapping(value = "/new-password")
	public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO body){
		  authService.saveNewPassword(body);
		  return ResponseEntity.noContent().build();
		
	}
	
	
}
