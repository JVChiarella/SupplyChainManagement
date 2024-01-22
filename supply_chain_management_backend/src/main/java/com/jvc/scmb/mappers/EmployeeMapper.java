package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;
import com.jvc.scmb.entities.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	EmployeeResponseDto entityToDto(Employee employee);
	
	Employee requestDtoToEntity(EmployeeRequestDto employeeRequestDto);
	
	List<EmployeeResponseDto> requestEntitiesToDtos(List<Employee> employees);
}
