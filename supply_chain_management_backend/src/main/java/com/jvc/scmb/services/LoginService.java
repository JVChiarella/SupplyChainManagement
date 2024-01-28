package com.jvc.scmb.services;

import com.jvc.scmb.dtos.CredentialsRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;

public interface LoginService {

	CustomerResponseDto loginCustomer(CredentialsRequestDto credentialsRequestDto);

	EmployeeResponseDto loginEmployee(CredentialsRequestDto credentialsRequestDto);

}
