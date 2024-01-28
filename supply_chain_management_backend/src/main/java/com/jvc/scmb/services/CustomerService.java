package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;

public interface CustomerService {

	List<CustomerResponseDto> getAllCustomers();

	CustomerResponseDto getOneCustomer(Long id);

	CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto);

	CustomerResponseDto deleteCustomer(Long id, CredentialsDto credentialsRequestDto);

	CustomerResponseDto patchCustomer(Long id, UserRequestDto userRequestDto);

}
