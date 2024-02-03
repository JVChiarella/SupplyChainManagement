package com.jvc.scmb.services;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;
import com.jvc.scmb.entities.Employee;

public interface LoginService {

	CustomerResponseDto loginCustomer(CredentialsDto credentialsRequestDto);

	Employee loginEmployee(CredentialsDto credentialsRequestDto);

}
