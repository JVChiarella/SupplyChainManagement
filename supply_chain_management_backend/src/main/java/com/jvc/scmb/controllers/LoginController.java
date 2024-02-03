package com.jvc.scmb.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.services.JwtGenerator;
import com.jvc.scmb.services.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("login")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class LoginController {
	
	private final LoginService loginService;
	private final JwtGenerator jwtGenerator;
	
	@PostMapping("/customer")
	public CustomerResponseDto loginCustomer(@RequestBody CredentialsDto credentialsRequestDto) {
		return loginService.loginCustomer(credentialsRequestDto);
	}
	
	@PostMapping("/employee")
	public ResponseEntity<?> loginEmployee(@RequestBody CredentialsDto credentialsRequestDto) {
		Employee employee = loginService.loginEmployee(credentialsRequestDto);
		return new ResponseEntity<>(jwtGenerator.generateEmployeeToken(employee), HttpStatus.OK);
	}

}
