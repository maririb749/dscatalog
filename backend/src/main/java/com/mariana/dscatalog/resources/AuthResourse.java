package com.mariana.dscatalog.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mariana.dscatalog.dto.EmailDTO;
import com.mariana.dscatalog.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResourse {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping(value = "/recover-token")
	public ResponseEntity<Void> RecoverToken(@Valid @RequestBody EmailDTO body){
		  authService.recoverToken(body);
		  return ResponseEntity.noContent().build();
		
	}
	
	
}
