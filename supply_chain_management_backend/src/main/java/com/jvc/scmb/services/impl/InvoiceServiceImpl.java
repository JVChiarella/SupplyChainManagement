package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.InvoiceResponseDto;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.mappers.InvoiceMapper;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.repositories.InvoiceRepository;
import com.jvc.scmb.services.InvoiceService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService{

	private final InvoiceRepository invoiceRepository;
	private final InvoiceMapper invoiceMapper;
	private final EmployeeRepository employeeRepository;
	private final CustomerRepository customerRepository;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Override
	public InvoiceResponseDto getInvoice(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
			//look for invoice
			Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
			if(optionalInvoice.isEmpty()) {
				throw new BadRequestException("invoice with provided id not found");
			}
			Invoice foundInvoice = optionalInvoice.get();
	    	 
		    //check that jwt belongs to the customer who placed the order or an employee
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	;
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	if(!foundInvoice.getOrder().getCustomer().getCredentials().getUsername().equals(jwt.getBody().get("username"))) {
		    		throw new BadRequestException("only an employee or the customer who placed the orders can view their details");
		    	}
		    } else {
		    	throw new BadRequestException("only an employee or the customer who placed the orders can view their details");
		    }
    	 
			return invoiceMapper.entityToDto(foundInvoice);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
	    }
	}
	
	public InvoiceResponseDto assignEmployee(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
	    	 //find employee in db
	    	 Optional<Employee> optEmployee = employeeRepository.findByCredentialsUsername((String)jwt.getBody().get("username"));
	    	 if(optEmployee.isEmpty()) {
	    		 throw new BadRequestException("employee with provided username not found"); //shouldnt be possible
	    	 }
	    	 
	    	 //check that employee is active
	    	 Employee foundEmployee = optEmployee.get();
	    	 if(!foundEmployee.getActive()) {
	    		 throw new BadRequestException("employee with provided username is not active");
	    	 }
	    	 
	    	 //find invoice in db
	    	 Optional<Invoice> optInvoice = invoiceRepository.findById(id);
	    	 if(optInvoice.isEmpty()) {
	    		 throw new BadRequestException("invoice with provided id not found");
	    	 }
	    	 
	    	 //check invoice status
	    	 Invoice invoice = optInvoice.get();
	    	 
	    	 if(invoice.getStatus().equals("unfulfilled")) {
	    		 invoice.setStatus("in progress");
	    	 } else if(invoice.getStatus().equals("fulfilled")) {
	    		 throw new BadRequestException("this order has already been completed");
	    	 }
	    	 
	    	 if(invoice.getEmployee() != null) {
	    		 throw new BadRequestException("this invoice already belongs to an employee");
	    	 }
	    	 
	    	 //update invoice assignee and employee invoice list
	    	 invoice.setEmployee(foundEmployee);
	    	 List<Invoice> newList = new ArrayList<>();
	    	 List<Invoice> oldList = foundEmployee.getInvoices();
	    	 for(Invoice inv : oldList) {
	    		 //check that this invoice is not already in employee's list; shouldn't be possible but check anyway
	    		 if(inv.equals(invoice)) {
	    			 throw new BadRequestException("this order is already being fulfilled by this employee");
	    		 }
	    		 newList.add(inv);
	    	 }
	    	 newList.add(invoice);
	    	 oldList = null;
	    	 foundEmployee.setInvoices(newList);
	    	 
	    	 //save and return
	    	 employeeRepository.saveAndFlush(foundEmployee);
	    	 return invoiceMapper.entityToDto(invoiceRepository.saveAndFlush(invoice));
	    	 
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
	    }
	}
	
	public List<InvoiceResponseDto> getAllInvoicesByCustomer(Long id, String token){
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
	    	 //find customer in db
	    	 Optional<Customer> optCustomer = customerRepository.findById(id);
	    	 if(optCustomer.isEmpty()) {
	    		 throw new BadRequestException("customer with provided username not found");
	    	 }
	    	 Customer customer = optCustomer.get();
	    	 
		    //check that jwt belongs to the customer or an admin employee
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	if((boolean)jwt.getBody().get("admin")) {
		    		;
		    	} else {
		    		throw new BadRequestException("only admin employees can make this request");
		    	}
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	if(jwt.getBody().get("username").equals(customer.getCredentials().getUsername())) {
		    		;
		    	} else {
		    		throw new BadRequestException("only the customer who placed the orders can view their details");
		    	}
		    } else {
		    	throw new BadRequestException("only an admin employee or the customer who placed the orders can view their details");
		    }
	    	 
	    	//get all invoices by customer id
	    	Optional<List<Invoice>> optInvoices = invoiceRepository.findByOrderCustomerId(id);
	    	if(optInvoices.isEmpty()) {
	    		throw new BadRequestException("no invoices found belonging to customer with this id");
	    	}
	    	 
	    	//get invoices and return
	    	List<Invoice> invoices = optInvoices.get();
	    	return invoiceMapper.requestEntitiesToDtos(invoices);
	    	 
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
	    }
	}

	public List<InvoiceResponseDto> getAllInvoicesByEmployee(Long id, String token){
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
	    	 //find employee in db
	    	 Optional<Employee> optEmployee = employeeRepository.findById(id);
	    	 if(optEmployee.isEmpty()) {
	    		 throw new BadRequestException("employee with provided id not found");
	    	 }
	    	 
	    	 //check that employee is active
	    	 Employee foundEmployee = optEmployee.get();
	    	 if(!foundEmployee.getActive()) {
	    		 throw new BadRequestException("employee with provided username is not active");
	    	 }
	    	 
		     //check that jwt belongs to the employee requested
		     if(jwt.getBody().getSubject().equals("employee")) {
		    	 if(jwt.getBody().get("username").equals(foundEmployee.getCredentials().getUsername())) {
		    		 ;
		    	 } else if ((boolean)jwt.getBody().get("admin")) {
		    		 ;
		    	 } else {
		    		 throw new BadRequestException("jwt mismatch; only the employee themselves or an admin can view their invoices");
		    	 }
		     } else {
		    	throw new BadRequestException("only an employee can perform this request");
		     }

	    	 //get all invoices by employee id
	    	 Optional<List<Invoice>> optInvoices = invoiceRepository.findByEmployeeId(id);
	    	 if(optInvoices.isEmpty()) {
	    		 throw new BadRequestException("no invoices found belonging to employee with this id");
	    	 }
	    	 
	    	//get invoices and return
	    	 List<Invoice> invoices = optInvoices.get();
	    	 return invoiceMapper.requestEntitiesToDtos(invoices);
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
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
		return token;
	}
}
