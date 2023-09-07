package com.mariana.dscatalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {
	
	@JsonProperty(value = "email")
	@NotBlank(message= "Campo Obrigatório")
	@Email(message = "Email inválido")
	private String email;
	
  public EmailDTO() {
	  
  }

	public EmailDTO(String email) {
	  this.email = email;
  }

  public String getEmail() {
	return email;
  }

}

  
	






