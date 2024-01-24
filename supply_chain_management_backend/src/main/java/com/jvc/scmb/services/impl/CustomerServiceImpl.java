package com.jvc.scmb.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.exceptions.NotFoundException;
import com.jvc.scmb.dtos.CredentialsRequestDto;
import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.mappers.CustomerMapper;
import com.jvc.scmb.mappers.EmployeeMapper;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.services.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
	
	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;
	
	//get all customers
	@Override
    public List<CustomerResponseDto> getAllCustomers() {
        return customerMapper.requestEntitiesToDtos(customerRepository.findAll());
    }
	
	//get one customer
	@Override
	public CustomerResponseDto getOneCustomer(Long id) {
		Optional<Customer> customer = customerRepository.findById(id);
		if(customer.isEmpty()) {
			throw new NotFoundException("customer with that id not found");
		}
		
		Customer foundcustomer = customer.get();
		
		//return found customer
		return customerMapper.entityToDto(foundcustomer);
	}
	
	//add customer to DB
	@Override
	public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) {
		//check credentials, username, password, and name were provided
        if(customerRequestDto.getCredentials() == null || customerRequestDto.getCredentials().getUsername() == null ||
                customerRequestDto.getCredentials().getPassword() == null || customerRequestDto.getFirstName() == null ||
                customerRequestDto.getLastName() == null || customerRequestDto.getAddress() == null || customerRequestDto.getPhoneNumber() == null) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid customer; only an active customer can add a new customer
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(customerRequestDto.getCredentials().getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee foundEmployee = optionalUser.get();
        if(!foundEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of customer making new customer request
        if(!foundEmployee.getCredentials().getPassword().equals(customerRequestDto.getCredentials().getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
		
		//convert dto to new customer obj
		Customer newCustomer = new Customer();
		newCustomer = customerMapper.requestDtoToEntity(customerRequestDto);
		
		//set default username and password for new customer
		Credentials newCredentials = new Credentials();
		newCredentials.setUsername(newCustomer.getFirstName().substring(0, 1) + newCustomer.getLastName());
		newCredentials.setPassword("password");
		newCustomer.setCredentials(newCredentials);
		
		//add new customer to db, save and return
		return customerMapper.entityToDto(customerRepository.saveAndFlush(newCustomer));
	}
	
	//delete a customer
	@Override
	public CustomerResponseDto deleteCustomer(Long id, CredentialsRequestDto credentialsRequestDto) {
		//check valid credentials were provided
        if(credentialsRequestDto.getUsername() == null || credentialsRequestDto.getPassword() == null) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid customer; only an active admin customer can delete an customer
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(credentialsRequestDto.getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee loggedEmployee = optionalUser.get();
        if(!loggedEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of customer making new customer request
        if(!loggedEmployee.getCredentials().getPassword().equals(credentialsRequestDto.getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
        
        //check that logged in user is an admin
        if(!loggedEmployee.getAdmin()) {
        	throw new NotAuthorizedException("logged in user is not an admin -- cannot delete an customer");
        }
        
        //find customer to delete
		Optional<Customer> customer = customerRepository.findById(id);
		if(customer.isEmpty()) {
			throw new NotFoundException("customer with that id not found");
		}
		
		Customer foundcustomer = customer.get();
		
		//delete customer (via status flag), save to db, and return deleted customer
		foundcustomer.setActive(false);
		return customerMapper.entityToDto(customerRepository.saveAndFlush(foundcustomer));
	}
	
	//patch customer
	@Override
	public CustomerResponseDto patchCustomer(Long id, CustomerRequestDto customerRequestDto) {
		//check credentials, username, password, and customer data was provided
        if(customerRequestDto.getCredentials() == null || customerRequestDto.getCredentials().getUsername() == null ||
                customerRequestDto.getCredentials().getPassword() == null || customerRequestDto.getFirstName() == null ||
                customerRequestDto.getLastName() == null) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid customer; only an active customer can add a new customer
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(customerRequestDto.getCredentials().getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee loggedEmployee = optionalUser.get();
        if(!loggedEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of customer making patch customer request
        if(!loggedEmployee.getCredentials().getPassword().equals(customerRequestDto.getCredentials().getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
        
        //check that logged in user is an admin
        if(!loggedEmployee.getAdmin()) {
        	throw new NotAuthorizedException("logged in user is not an admin -- cannot delete an customer");
        }
        
        //find customer whose data is to be updated
        Optional<Customer> optionalCus = customerRepository.findById(id);
        if(optionalCus.isEmpty()) {
        	throw new NotFoundException("user with provided id not found");
        }
        
        Customer foundCustomer = optionalCus.get();
		
		//update customer data with new data
		foundCustomer.setActive(customerRequestDto.getActive());
		foundCustomer.setFirstName(customerRequestDto.getFirstName());
		foundCustomer.setLastName(customerRequestDto.getLastName());
		foundCustomer.setAddress(customerRequestDto.getAddress());
		foundCustomer.setPhoneNumber(customerRequestDto.getPhoneNumber());
		
		//add new customer to db, save and return
		return customerMapper.entityToDto(customerRepository.saveAndFlush(foundCustomer));
	}
}
