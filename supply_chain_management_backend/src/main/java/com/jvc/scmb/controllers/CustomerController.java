package com.jvc.scmb.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;
import com.jvc.scmb.services.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class CustomerController {
	
	private final CustomerService customerService;
	
	//get all customers
	@GetMapping()
	public List<CustomerResponseDto> getAllCustomers(){
		return customerService.getAllCustomers();
	}
	
	//get one customer
	@GetMapping("/{id}")
	public CustomerResponseDto getOneCustomer(@PathVariable Long id) {
		return customerService.getOneCustomer(id);
	}
	
	//add a new customer
	//pass current customer's credentials in requestDto for validation; everything else is new customer's info
	@PostMapping("/add")
	public CustomerResponseDto addCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
		return customerService.addCustomer(customerRequestDto);
	}
	
	//delete an customer via patch -- soft delete
	@PatchMapping("/delete/{id}")
	public CustomerResponseDto deleteCustomer(@PathVariable Long id, @RequestBody CredentialsDto credentialsRequestDto) {
		return customerService.deleteCustomer(id, credentialsRequestDto);
	}
	
	//patch an customer's info
	//pass current customer's credentials in requestDto for validation; everything else is patching customer's info
	@PatchMapping("/patch/{id}")
	public CustomerResponseDto patchCustomer(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
		return customerService.patchCustomer(id, userRequestDto);
	}

}
