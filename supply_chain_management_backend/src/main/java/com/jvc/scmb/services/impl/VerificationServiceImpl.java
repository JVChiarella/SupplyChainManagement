package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.services.VerificationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class VerificationServiceImpl implements VerificationService {
	
	@Value("${jwt.secret}")
	private String secret;
	
	public Map<String, String> verifyJWT(String token){
		//verify jwt from header of request
		if(token == null) {
			throw new IllegalArgumentException("jwt authoriozation failed");
		}
		
		if(!token.startsWith("Bearer")){
			throw new IllegalArgumentException("jwt authoriozation failed");
		}
		
		//remove token prefix
		token = token.replace("Bearer ", "");
		token = token.replace("\"",""); 
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
			Jws<Claims> jwt = Jwts.parserBuilder()
			       .setSigningKey(key)
			       .build()
			       .parseClaimsJws(token);
	    	 
	    	Map<String, String> response = new HashMap<>();
		    response.put("message", "verified successfully");
		    return response;
	    } catch(Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
		    
		
	}
}
