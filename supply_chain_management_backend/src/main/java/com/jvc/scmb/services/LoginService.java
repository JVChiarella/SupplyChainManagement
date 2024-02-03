package com.jvc.scmb.services;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;

public interface LoginService {

	Customer loginCustomer(CredentialsDto credentialsRequestDto);

	Employee loginEmployee(CredentialsDto credentialsRequestDto);

}
