package com.jvc.scmb.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestDto {
	//either 'employee' or 'customer'
	private String type;
	
	private CredentialsDto credentials;
	
	//optional data; user is either an employee or customer. one object should be provided for patch, etc. requests
	private EmployeeRequestDto employeeRequestDto;
	
	private CustomerRequestDto customerRequestDto;
}