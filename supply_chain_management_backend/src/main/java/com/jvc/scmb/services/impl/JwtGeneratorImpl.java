package com.jvc.scmb.services.impl;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.services.JwtGenerator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtGeneratorImpl implements JwtGenerator{
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${app.jwttoken.message}")
	private String message;
	
	@Override
	public Map<String, String> generateEmployeeToken(Employee employee) {
		//Key key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_16LE), SignatureAlgorithm.HS256.getJcaName());
		Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
		Instant now = Instant.now();
		
		String jwtToken = Jwts.builder()
		.claim("username",  employee.getCredentials().getUsername())
		.claim("password",  employee.getCredentials().getPassword())
		.setSubject("employee")
		.setId(UUID.randomUUID().toString())
		.setIssuedAt(Date.from(now))
		.signWith(key)
		.compact();
		
	    Map<String, String> jwtTokenGen = new HashMap<>();
	    jwtTokenGen.put("token", jwtToken);
	    jwtTokenGen.put("message", message);
	    return jwtTokenGen;
	}
	
	@Override
	public Map<String, String> generateCustomerToken(Customer customer) {
		Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
		Instant now = Instant.now();
		
		String jwtToken = Jwts.builder()
		.claim("username",  customer.getCredentials().getUsername())
		.claim("password",  customer.getCredentials().getPassword())
		.setSubject("customer")
		.setId(UUID.randomUUID().toString())
		.setIssuedAt(Date.from(now))
		.signWith(key)
		.compact();
		
	    Map<String, String> jwtTokenGen = new HashMap<>();
	    jwtTokenGen.put("token", jwtToken);
	    jwtTokenGen.put("message", message);
	    return jwtTokenGen;
	}
}
