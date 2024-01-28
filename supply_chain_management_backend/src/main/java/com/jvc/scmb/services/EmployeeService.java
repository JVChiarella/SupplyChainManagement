package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;

public interface EmployeeService {

	List<EmployeeResponseDto> getAllEmployees();

	EmployeeResponseDto getOneEmployee(Long id);
	
	EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto);

	EmployeeResponseDto deleteEmployee(Long id, CredentialsDto credentialsRequestDto);

	EmployeeResponseDto patchEmployee(Long id, EmployeeRequestDto employeeRequestDto);

}
