package com.jvc.scmb.dtos;

import java.util.List;

import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeRequestDto {
	@NotNull(message = "credentials must be provided to perform request")
	private Credentials credentials;
	
	@NotNull(message = "employee's first name must be provided")
	private String firstName;
	
	@NotNull(message = "employee's last name must be provided")
	private String lastName;
	
	private Boolean active;
	
	private Boolean admin;
	
	private List<Order> orders;
}
