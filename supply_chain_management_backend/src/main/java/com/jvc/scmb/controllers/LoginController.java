package com.jvc.scmb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;
import com.jvc.scmb.services.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {
	
	private final LoginService loginService;
	
	@GetMapping("/customer")
	public CustomerResponseDto loginCustomer(@RequestBody CredentialsDto credentialsRequestDto) {
		return loginService.loginCustomer(credentialsRequestDto);
	}
	
	@GetMapping("/employee")
	public EmployeeResponseDto loginEmployee(@RequestBody CredentialsDto credentialsRequestDto) {
		return loginService.loginEmployee(credentialsRequestDto);
	}

}
