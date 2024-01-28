package com.jvc.scmb.services;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;

public interface LoginService {

	CustomerResponseDto loginCustomer(CredentialsDto credentialsRequestDto);

	EmployeeResponseDto loginEmployee(CredentialsDto credentialsRequestDto);

}
