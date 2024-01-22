package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;

public interface EmployeeService {

	EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto);

	List<EmployeeResponseDto> getAllEmployees();

}
