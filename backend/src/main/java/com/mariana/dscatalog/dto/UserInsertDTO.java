package com.mariana.dscatalog.dto;

import com.mariana.dscatalog.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
	private static final long serialVersionUID = 1L;
	
	private String password;
	
	UserInsertDTO(){
		super();
	
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
