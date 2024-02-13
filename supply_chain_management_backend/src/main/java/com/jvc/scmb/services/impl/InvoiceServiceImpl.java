package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.InvoiceResponseDto;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.mappers.InvoiceMapper;
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
