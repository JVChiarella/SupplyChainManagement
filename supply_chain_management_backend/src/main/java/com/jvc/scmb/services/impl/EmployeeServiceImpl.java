package com.jvc.scmb.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.exceptions.NotFoundException;
import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;
import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.mappers.EmployeeMapper;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.services.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;
	
	//get all employees
	@Override
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeMapper.requestEntitiesToDtos(employeeRepository.findAll());
    }
	
	//get one employee
	@Override
	public EmployeeResponseDto getOneEmployee(Long id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if(employee.isEmpty()) {
			throw new NotFoundException("employee with that id not found");
		}
		
		Employee foundEmployee = employee.get();
		
		//return found employee
		return employeeMapper.entityToDto(foundEmployee);
	}
	
	//add employee to DB
	@Override
	public EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto) {
		//check credentials, username, password, and name were provided
        if(employeeRequestDto.getCredentials() == null || employeeRequestDto.getCredentials().getUsername() == null ||
                employeeRequestDto.getCredentials().getPassword() == null || employeeRequestDto.getFirstName() == null ||
                employeeRequestDto.getLastName() == null) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid employee; only an active employee can add a new employee
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(employeeRequestDto.getCredentials().getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee foundEmployee = optionalUser.get();
        if(!foundEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of employee making new employee request
        if(!foundEmployee.getCredentials().getPassword().equals(employeeRequestDto.getCredentials().getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
		
		//convert dto to new Employee obj
		Employee newEmployee = new Employee();
		newEmployee = employeeMapper.requestDtoToEntity(employeeRequestDto);
		
		//set default username and password for new employee
		Credentials newCredentials = new Credentials();
		newCredentials.setUsername(newEmployee.getFirstName().substring(0, 1) + newEmployee.getLastName());
		newCredentials.setPassword("password");
		newEmployee.setCredentials(newCredentials);
		
		//add new employee to db, save and return
		return employeeMapper.entityToDto(employeeRepository.saveAndFlush(newEmployee));
	}
	
	//delete an employee
	@Override
	public EmployeeResponseDto deleteEmployee(Long id, CredentialsDto credentialsRequestDto) {
		//check valid credentials were provided
        if(credentialsRequestDto.getUsername() == null || credentialsRequestDto.getPassword() == null) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid employee; only an active admin employee can delete an employee
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(credentialsRequestDto.getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee loggedEmployee = optionalUser.get();
        if(!loggedEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of employee making new employee request
        if(!loggedEmployee.getCredentials().getPassword().equals(credentialsRequestDto.getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
        
        //check that logged in user is an admin
        if(!loggedEmployee.getAdmin()) {
        	throw new NotAuthorizedException("logged in user is not an admin -- cannot delete an employee");
        }
        
        //find employee to delete
		Optional<Employee> employee = employeeRepository.findById(id);
		if(employee.isEmpty()) {
			throw new NotFoundException("employee with that id not found");
		}
		
		Employee foundEmployee = employee.get();
		
		//delete employee (via status flag), save to db, and return deleted employee
		foundEmployee.setActive(false);
		return employeeMapper.entityToDto(employeeRepository.saveAndFlush(foundEmployee));
	}
	
	//patch employee
	@Override
	public EmployeeResponseDto patchEmployee(Long id, EmployeeRequestDto employeeRequestDto) {
		//check credentials, username, password, and employee data was provided
        if(employeeRequestDto.getCredentials() == null || employeeRequestDto.getCredentials().getUsername() == null ||
                employeeRequestDto.getCredentials().getPassword() == null || employeeRequestDto.getFirstName() == null ||
                employeeRequestDto.getLastName() == null) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid employee; only an active employee can add a new employee
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(employeeRequestDto.getCredentials().getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee loggedEmployee = optionalUser.get();
        if(!loggedEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of employee making patch employee request
        if(!loggedEmployee.getCredentials().getPassword().equals(employeeRequestDto.getCredentials().getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
        
        //check that logged in user is an admin
        if(!loggedEmployee.getAdmin()) {
        	throw new NotAuthorizedException("logged in user is not an admin -- cannot delete an employee");
        }
        
        //find employee whose data is to be updated
        optionalUser = employeeRepository.findById(id);
        if(optionalUser.isEmpty()) {
        	throw new NotFoundException("user with provided id not found");
        }
        
        Employee foundEmployee = optionalUser.get();
		
		//update employee data with new data
		foundEmployee.setActive(employeeRequestDto.getActive());
		foundEmployee.setAdmin(employeeRequestDto.getAdmin());
		foundEmployee.setFirstName(employeeRequestDto.getFirstName());
		foundEmployee.setLastName(employeeRequestDto.getLastName());
		
		//add new employee to db, save and return
		return employeeMapper.entityToDto(employeeRepository.saveAndFlush(foundEmployee));
	}
}
