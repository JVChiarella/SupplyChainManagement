package com.jvc.scmb.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CredentialsRequestDto {
	
	@NotNull(message = "username must be provided")
	private String username;
	
	@NotNull(message = "password must be provided")
	private String password;

}
