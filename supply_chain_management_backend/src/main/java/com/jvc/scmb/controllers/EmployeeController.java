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
import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;
import com.jvc.scmb.services.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class EmployeeController {
	
	private final EmployeeService employeeService;
	
	//get all employees
	@GetMapping()
	public List<EmployeeResponseDto> getAllEmployees(){
		return employeeService.getAllEmployees();
	}
	
	//get one employee
	@GetMapping("/{id}")
	public EmployeeResponseDto getOneEmployee(@PathVariable Long id) {
		return employeeService.getOneEmployee(id);
	}
	
	//add a new employee
	//pass current employee's credentials in requestDto for validation; everything else is new employee's info
	@PostMapping("/add")
	public EmployeeResponseDto addEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
		return employeeService.addEmployee(employeeRequestDto);
	}
	
	//delete an employee via patch -- soft delete
	@PatchMapping("/delete/{id}")
	public EmployeeResponseDto deleteEmployee(@PathVariable Long id, @RequestBody CredentialsDto credentialsRequestDto) {
		return employeeService.deleteEmployee(id, credentialsRequestDto);
	}
	
	//patch an employee's info
	//pass current employee's credentials in requestDto for validation; everything else is patching employee's info
	@PatchMapping("/patch/{id}")
	public EmployeeResponseDto patchEmployee(@PathVariable Long id, @RequestBody EmployeeRequestDto employeeRequestDto) {
		return employeeService.patchEmployee(id, employeeRequestDto);
	}

}
