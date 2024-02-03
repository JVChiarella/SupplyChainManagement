package com.jvc.scmb.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.mappers.CustomerMapper;
import com.jvc.scmb.mappers.EmployeeMapper;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.services.LoginService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
	
	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;
	
	@Override
	public Customer loginCustomer(CredentialsDto credentialsRequestDto) {
		//check credentials, username, password, and name were provided
        if(credentialsRequestDto.getUsername() == null || credentialsRequestDto.getPassword() == null ) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid customer; only an active customer can add a new customer
        Optional<Customer> optionalUser = customerRepository.findByCredentialsUsername(credentialsRequestDto.getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Customer foundCustomer = optionalUser.get();
        if(!foundCustomer.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of customer making new customer request
        if(!foundCustomer.getCredentials().getPassword().equals(credentialsRequestDto.getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
        
        //return customer
        return foundCustomer;
	}

	@Override
	public Employee loginEmployee(CredentialsDto credentialsRequestDto) {
		//check credentials, username, password, and name were provided
        if(credentialsRequestDto.getUsername() == null || credentialsRequestDto.getPassword() == null ) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid employee; only an active employee can add a new employee
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(credentialsRequestDto.getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee foundEmployee = optionalUser.get();
        if(!foundEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of employee making new employee request
        if(!foundEmployee.getCredentials().getPassword().equals(credentialsRequestDto.getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
        
        //return employee
        return foundEmployee;
	}

}
