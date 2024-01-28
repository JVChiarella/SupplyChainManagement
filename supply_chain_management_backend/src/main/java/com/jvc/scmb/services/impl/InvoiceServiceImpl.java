package com.jvc.scmb.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.InvoiceResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.mappers.InvoiceMapper;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.repositories.InvoiceRepository;
import com.jvc.scmb.services.InvoiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService{

	private final InvoiceRepository invoiceRepository;
	private final InvoiceMapper invoiceMapper;
	private final EmployeeRepository employeeRepository;
	private final CustomerRepository customerRepository;
	
	@Override
	public InvoiceResponseDto getInvoice(Long id, UserRequestDto userRequestDto) {
		//check credentials
		checkCredentials(userRequestDto);
			
		//look for invoice
		Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
		if(optionalInvoice.isEmpty()) {
			throw new BadRequestException("invoice with provided id not found");
		}
		
		//convert invoice to dto and return
		Invoice foundInvoice = optionalInvoice.get();
		return invoiceMapper.entityToDto(foundInvoice);
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
