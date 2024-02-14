package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.exceptions.NotFoundException;
import com.jvc.scmb.mappers.CustomerMapper;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.services.CustomerService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
	
	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;
	private final EmployeeRepository employeeRepository;
	
	@Value("${jwt.secret}")
	private String secret;
	
	//get all customers
	@Override
    public List<CustomerResponseDto> getAllCustomers(String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
		    //check that jwt belongs to an employee
		    if(!jwt.getBody().getSubject().equals("employee")) {
		    	throw new BadRequestException("only employees can make this request");
		    }
		    
		    List<Customer> foundCustomers = customerRepository.findAll();
		    List<Customer> activeCustomers = new ArrayList<>();
		    for(Customer user : foundCustomers) {
		    	if(user.getActive()) {
		    		activeCustomers.add(user);
		    	}
		    }
		    
		    return customerMapper.requestEntitiesToDtos(activeCustomers);
	    } catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
    }
	
	//get one customer
	@Override
	public CustomerResponseDto getOneCustomer(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
		    //check that jwt belongs to an employee
		    if(!jwt.getBody().getSubject().equals("employee")) {
		    	throw new BadRequestException("only employees can make this request");
		    }
		    
			Optional<Customer> customer = customerRepository.findById(id);
			if(customer.isEmpty()) {
				throw new NotFoundException("customer with that id not found");
			}
			
			Customer foundCustomer = customer.get();
			
			//return found customer
			return customerMapper.entityToDto(foundCustomer);
	    } catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
	}
	
	//add customer to DB
	@Override
	public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	
	        //credentials should be a valid employee; only an active employee can add a new employee
	        String username = (String)jwt.getBody().get("username");
	        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(username);
	        if(optionalUser.isEmpty()) {
	        	throw new NotAuthorizedException("user with provided credentials not found");
	        }
	        
	        //check that found user is active
	        Employee foundEmployee = optionalUser.get();
	        if(!foundEmployee.getActive()) {
	        	throw new NotAuthorizedException("non-active user");
	        }
	        
			//check customer info was provided
	        if(customerRequestDto.getFirstName() == null || customerRequestDto.getLastName() == null || 
	           customerRequestDto.getAddress() == null || customerRequestDto.getPhoneNumber() == null) {
	            throw new BadRequestException("one or more fields missing in request");
	        }
			
			//convert dto to new customer obj
			Customer newCustomer = new Customer();
			newCustomer = customerMapper.requestDtoToEntity(customerRequestDto);
			
			//set default username and password for customer if not provided
			if(customerRequestDto.getCredentials().getUsername().equals("") || customerRequestDto.getCredentials().getPassword().equals("")) {
				Credentials newCredentials = new Credentials();
				newCredentials.setUsername(newCustomer.getFirstName().substring(0, 1).toLowerCase() + newCustomer.getLastName().substring(0, 1).toLowerCase() + newCustomer.getLastName().substring(1));
				newCredentials.setPassword("password");
				newCustomer.setCredentials(newCredentials);
			}
			
			//add new customer to db, save and return
			return customerMapper.entityToDto(customerRepository.saveAndFlush(newCustomer));
	    } catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
	}
	
	//delete a customer
	@Override
	public CustomerResponseDto deleteCustomer(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	
	        //credentials should be a valid employee; only an active employee can delete customer
	        String username = (String)jwt.getBody().get("username");
	        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(username);
	        if(optionalUser.isEmpty()) {
	        	throw new NotAuthorizedException("user with provided credentials not found");
	        }
	
	        //check that found user is active
	        Employee loggedEmployee = optionalUser.get();
	        if(!loggedEmployee.getActive()) {
	        	throw new NotAuthorizedException("non-active user");
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
		}  catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
	}
	
	//patch customer
	@Override
	public CustomerResponseDto patchCustomer(Long id, CustomerRequestDto customerRequestDto, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
	        //find customer whose data is to be updated
	        Optional<Customer> optionalCus = customerRepository.findById(id);
	        if(optionalCus.isEmpty()) {
	        	throw new NotFoundException("user with provided id not found");
	        }
	        
	        Customer foundCustomer = optionalCus.get();
	        
		    //check that jwt belongs to the customer or an employee
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	;
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	if(!foundCustomer.getCredentials().getUsername().equals(jwt.getBody().get("username"))) {
		    		throw new BadRequestException("only an employee or the customer who placed the order can view its details");
		    	}
		    } else {
		    	throw new BadRequestException("only an employee or the customer who placed the order can view its details");
		    }
			
			//update customer data with new data
		    Customer newCustomerData = customerMapper.requestDtoToEntity(customerRequestDto);
		    if(!newCustomerData.getFirstName().equals("")) {
		    	foundCustomer.setFirstName(newCustomerData.getFirstName());
		    }
			if(!newCustomerData.getLastName().equals("")) {
				foundCustomer.setLastName(newCustomerData.getLastName());
			}
			if(!newCustomerData.getAddress().equals("")) {
				foundCustomer.setAddress(newCustomerData.getAddress());
			}
			if(!newCustomerData.getPhoneNumber().equals("")) {
				foundCustomer.setPhoneNumber(newCustomerData.getPhoneNumber());
			}
			
			//save customer info to db, save and return
			return customerMapper.entityToDto(customerRepository.saveAndFlush(foundCustomer));
		}  catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
	}
	
	public String JwtVerification(String token) {
		if(token == null) {
			throw new IllegalArgumentException("jwt authoriozation failed");
		}
		
		if(!token.startsWith("Bearer")){
			throw new IllegalArgumentException("jwt authoriozation failed");
		}
		
		//remove token prefix
		token = token.replace("Bearer ", "");
		token = token.replace("\"",""); 
		//System.out.println("TOKEN: " + token);
		return token;
	}
}
