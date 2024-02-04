package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;

public interface CustomerService {

	List<CustomerResponseDto> getAllCustomers(String token);

	CustomerResponseDto getOneCustomer(Long id, String token);

	CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto, String token);

	CustomerResponseDto deleteCustomer(Long id, String token);

	CustomerResponseDto patchCustomer(Long id, CustomerRequestDto customerRequestDto, String token);

}
