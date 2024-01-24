package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.CredentialsRequestDto;
import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;

public interface CustomerService {

	List<CustomerResponseDto> getAllCustomers();

	CustomerResponseDto getOneCustomer(Long id);

	CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto);

	CustomerResponseDto deleteCustomer(Long id, CredentialsRequestDto credentialsRequestDto);

	CustomerResponseDto patchCustomer(Long id, CustomerRequestDto customerRequestDto);

}
