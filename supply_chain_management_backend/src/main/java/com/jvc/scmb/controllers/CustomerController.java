package com.jvc.scmb.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
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
	public List<CustomerResponseDto> getAllCustomers(@RequestHeader (name="Authorization") String token){
		return customerService.getAllCustomers(token);
	}
	
	//get one customer
	@GetMapping("/{id}")
	public CustomerResponseDto getOneCustomer(@PathVariable Long id, @RequestHeader (name="Authorization") String token) {
		return customerService.getOneCustomer(id, token);
	}
	
	//add a new customer
	@PostMapping("/add")
	public CustomerResponseDto addCustomer(@RequestBody CustomerRequestDto customerRequestDto, @RequestHeader (name="Authorization") String token) {
		return customerService.addCustomer(customerRequestDto, token);
	}
	
	//delete an customer via patch -- soft delete
	@PatchMapping("/delete/{id}")
	public CustomerResponseDto deleteCustomer(@PathVariable Long id, @RequestHeader (name="Authorization") String token) {
		return customerService.deleteCustomer(id, token);
	}
	
	//patch an customer's info
	@PatchMapping("/patch/{id}")
	public CustomerResponseDto patchCustomer(@PathVariable Long id, @RequestBody CustomerRequestDto customerRequestDto, @RequestHeader (name="Authorization") String token) {
		return customerService.patchCustomer(id, customerRequestDto, token);
	}

}
