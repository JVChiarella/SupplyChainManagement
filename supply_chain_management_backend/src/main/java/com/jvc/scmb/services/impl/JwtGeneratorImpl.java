package com.jvc.scmb.services.impl;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.services.JwtGenerator;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtGeneratorImpl implements JwtGenerator{
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${app.jwttoken.message}")
	private String message;
	
	@Override
	public Map<String, String> generateEmployeeToken(Employee employee) {
	    //String jwtToken="";
	    //jwtToken = Jwts.builder().setSubject(employee.getCredentials().getUsername()).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secret").compact();
		Key hmacKey = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

		Instant now = Instant.now();
		String jwtToken = Jwts.builder()
		.claim("username",  employee.getCredentials().getUsername())
		.claim("password",  employee.getCredentials().getPassword())
		.setSubject(employee.getCredentials().getUsername())
		.setId(UUID.randomUUID().toString())
		.setIssuedAt(Date.from(now))
		.signWith(hmacKey)
		.compact();
		
	    Map<String, String> jwtTokenGen = new HashMap<>();
	    jwtTokenGen.put("token", jwtToken);
	    jwtTokenGen.put("message", message);
	    return jwtTokenGen;
	}
	
	@Override
	public Map<String, String> generateCustomerToken(Customer customer) {
	    String jwtToken="";
	    jwtToken = Jwts.builder().setSubject(customer.getCredentials().getUsername()).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secret").compact();
	    Map<String, String> jwtTokenGen = new HashMap<>();
	    jwtTokenGen.put("token", jwtToken);
	    jwtTokenGen.put("message", message);
	    return jwtTokenGen;
	}
	
  public boolean validateJwtToken(String authToken) {
	    try {
	      Jwts.parser().setSigningKey(secret.getBytes()).parse(authToken);
	      return true;
	    } catch (MalformedJwtException e) {
	      throw new BadRequestException("Invalid JWT token: {}" + e.getMessage());
	    } catch (ExpiredJwtException e) {
		  throw new BadRequestException("JWT token is expired: {}" + e.getMessage());
	    } catch (UnsupportedJwtException e) {
		  throw new BadRequestException("JWT token is unsupported: {}" + e.getMessage());
	    } catch (IllegalArgumentException e) {
		  throw new IllegalArgumentException("JWT claims string is empty: {}" + e.getMessage());
	    }
  	}
}
