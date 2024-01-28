package com.jvc.scmb.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRequestDto {
	//either 'employee' or 'customer'
	@NotNull(message = "type of user must be provided to perform request")
	private String type;
	
	@NotNull(message = "credentials must be provided to perform request")
	private CredentialsRequestDto credentialsRequestDto;
}