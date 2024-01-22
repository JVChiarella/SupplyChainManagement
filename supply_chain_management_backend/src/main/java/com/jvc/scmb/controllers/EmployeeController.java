package com.jvc.scmb.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;
import com.jvc.scmb.services.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {
	
	private final EmployeeService employeeService;
	
	@GetMapping()
	public List<EmployeeResponseDto> getAllEmployees(){
		return employeeService.getAllEmployees();
	}
	
	@PostMapping("/add")
	public EmployeeResponseDto addEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
		return employeeService.addEmployee(employeeRequestDto);
	}

}
