package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;

public interface EmployeeService {

	List<EmployeeResponseDto> getAllEmployees(String token);

	EmployeeResponseDto getOneEmployee(Long id, String token);
	
	EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto, String token);

	EmployeeResponseDto deleteEmployee(Long id, String token);

	EmployeeResponseDto patchEmployee(Long id, EmployeeRequestDto employeeRequestDto, String token);

}
