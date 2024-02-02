package com.jvc.scmb.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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
@CrossOrigin(origins="*")
public class LoginController {
	
	private final LoginService loginService;
	
	@PostMapping("/customer")
	public CustomerResponseDto loginCustomer(@RequestBody CredentialsDto credentialsRequestDto) {
		return loginService.loginCustomer(credentialsRequestDto);
	}
	
	@PostMapping("/employee")
	public EmployeeResponseDto loginEmployee(@RequestBody CredentialsDto credentialsRequestDto) {
		return loginService.loginEmployee(credentialsRequestDto);
	}

}
