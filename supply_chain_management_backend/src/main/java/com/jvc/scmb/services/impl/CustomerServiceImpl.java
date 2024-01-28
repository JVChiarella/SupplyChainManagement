package com.jvc.scmb.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.CredentialsDto;
import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;
import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.exceptions.NotFoundException;
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
		
		Customer foundCustomer = customer.get();
		
		//return found customer
		return customerMapper.entityToDto(foundCustomer);
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
		
		//convert dto to new customer obj
		Customer newCustomer = new Customer();
		newCustomer = customerMapper.requestDtoToEntity(customerRequestDto);
		
		//set default username and password for new customer
		Credentials newCredentials = new Credentials();
		newCredentials.setUsername(customerRequestDto.getCredentials().getUsername());
		newCredentials.setPassword(customerRequestDto.getCredentials().getPassword());
		newCustomer.setCredentials(newCredentials);
		
		//add new customer to db, save and return
		return customerMapper.entityToDto(customerRepository.saveAndFlush(newCustomer));
	}
	
	//delete a customer
	@Override
	public CustomerResponseDto deleteCustomer(Long id, CredentialsDto credentialsRequestDto) {
		//check valid credentials were provided
        if(credentialsRequestDto.getUsername() == null || credentialsRequestDto.getPassword() == null) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //credentials should be a valid customer; only an active admin employee can delete an customer
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(credentialsRequestDto.getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee loggedEmployee = optionalUser.get();
        if(!loggedEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of employee making new customer request
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
	public CustomerResponseDto patchCustomer(Long id, UserRequestDto userRequestDto) {
		//check credentials, username, password, and customer data was provided
        if(userRequestDto.getCredentials() == null || userRequestDto.getCredentials().getUsername() == null ||
                userRequestDto.getCredentials().getPassword() == null || userRequestDto.getCustomerRequestDto().getFirstName() == null ||
                userRequestDto.getCustomerRequestDto().getLastName() == null || userRequestDto.getCustomerRequestDto().getActive() == null ||
                userRequestDto.getCustomerRequestDto().getPhoneNumber() == null || userRequestDto.getCustomerRequestDto().getAddress() == null ||
                userRequestDto.getCustomerRequestDto().getCredentials() == null) {
            throw new BadRequestException("one or more fields missing in request");
        }

        //check credentials
        checkCredentials(userRequestDto);
        
        //find customer whose data is to be updated
        Optional<Customer> optionalCus = customerRepository.findById(id);
        if(optionalCus.isEmpty()) {
        	throw new NotFoundException("user with provided id not found");
        }
        
        Customer foundCustomer = optionalCus.get();
		
		//update customer data with new data
		foundCustomer.setActive(userRequestDto.getCustomerRequestDto().getActive());
		foundCustomer.setFirstName(userRequestDto.getCustomerRequestDto().getFirstName());
		foundCustomer.setLastName(userRequestDto.getCustomerRequestDto().getLastName());
		foundCustomer.setAddress(userRequestDto.getCustomerRequestDto().getAddress());
		foundCustomer.setPhoneNumber(userRequestDto.getCustomerRequestDto().getPhoneNumber());
		foundCustomer.getCredentials().setUsername(userRequestDto.getCustomerRequestDto().getCredentials().getUsername());
		foundCustomer.getCredentials().setPassword(userRequestDto.getCustomerRequestDto().getCredentials().getPassword());
		
		//save customer info to db, save and return
		return customerMapper.entityToDto(customerRepository.saveAndFlush(foundCustomer));
	}
	
	private void checkCredentials(UserRequestDto userRequestDto) {
		//check credentials were provided
        if(userRequestDto.getCredentials().getUsername() == null || userRequestDto.getCredentials().getPassword() == null ) {
            throw new BadRequestException("one or more fields missing in request");
        }
        
		//check what type of user is making request
		if(userRequestDto.getType().equals("employee")) {
	        //credentials should be a valid employee
	        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
	        if(optionalUser.isEmpty()) {
	        	throw new NotAuthorizedException("user with provided credentials not found");
	        }

	        //check that found user is active
	        Employee foundEmployee = optionalUser.get();
	        if(!foundEmployee.getActive()) {
	        	throw new NotAuthorizedException("non-active user");
	        }
	        
	        //check password of employee making request
	        if(!foundEmployee.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
	            throw new NotAuthorizedException("password incorrect");
	        }
	        return;
		} else if(userRequestDto.getType().equals("customer")) {
	        //credentials should be a valid customer
	        Optional<Customer> optionalUser = customerRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
	        if(optionalUser.isEmpty()) {
	        	throw new NotAuthorizedException("user with provided credentials not found");
	        }

	        //check that found user is active
	        Customer foundCustomer = optionalUser.get();
	        if(!foundCustomer.getActive()) {
	        	throw new NotAuthorizedException("non-active user");
	        }
	        
	        //check password of employee making request
	        if(!foundCustomer.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
	            throw new NotAuthorizedException("password incorrect");
	        }
	        return;
			
		} else {
			throw new BadRequestException("invalid type provided");
		}
	}
}
