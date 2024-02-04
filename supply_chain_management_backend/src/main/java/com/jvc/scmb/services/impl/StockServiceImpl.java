package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.List;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.StockRequestDto;
import com.jvc.scmb.dtos.StockResponseDto;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Stock;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.mappers.StockMapper;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.repositories.StockRepository;
import com.jvc.scmb.services.StockService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

	private final StockRepository stockRepository;
	private final StockMapper stockMapper;
	private final EmployeeRepository employeeRepository;
	private final CustomerRepository customerRepository;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Override
	public StockResponseDto getStock(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
        
	    	//credentials should be a valid employee; only an active employee can look at stock
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
	        
			//look for stock item
			Optional<Stock> optionalStock = stockRepository.findById(id);
			if(optionalStock.isEmpty()) {
				throw new BadRequestException("stock with provided id not found");
			}
			
			Stock foundStock = optionalStock.get();
			return stockMapper.entityToDto(foundStock);
			
	    } catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
	}

	@Override
	public List<StockResponseDto> getAllStockItems(String token){
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
        
	    	//credentials should be a valid employee; only an active employee can look at stock
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
			
			//get all stock and return
			List<Stock> stockItems = stockRepository.findAll();
			return stockMapper.requestEntitiesToDtos(stockItems);
			
	    } catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
	}

	@Override
	public StockResponseDto addStock(StockRequestDto stockRequestDto, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
        
	    	//credentials should be a valid employee; only an active employee can add items to stock
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
	        	throw new NotAuthorizedException("logged in user is not an admin -- cannot modify stock");
	        }
			
			//convert dto to entity and post to db
			Stock stock = stockMapper.requestDtoToEntity(stockRequestDto);
			return stockMapper.entityToDto(stockRepository.saveAndFlush(stock));
			
	    } catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
	}

	@Override
	public StockResponseDto patchStock(Long id, StockRequestDto stockRequestDto, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
        
	    	//credentials should be a valid employee; only an active employee can modify stock
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
	        	throw new NotAuthorizedException("logged in user is not an admin -- -- cannot modify stock");
	        }
	        
			//look for stock item
			Optional<Stock> optionalStock = stockRepository.findById(id);
			if(optionalStock.isEmpty()) {
				throw new BadRequestException("stock with provided id not found");
			}
			
			Stock foundStock = optionalStock.get();
			
			//apply changes, convert entity to dto and return
			Stock newStock = stockMapper.requestDtoToEntity(stockRequestDto);
			foundStock.setCount(newStock.getCount());
			foundStock.setDescription(newStock.getDescription());
			foundStock.setName(newStock.getName());
			foundStock.setPrice(newStock.getPrice());
			return stockMapper.entityToDto(stockRepository.saveAndFlush(foundStock));
	    } catch (Exception e) {
	    	throw new BadRequestException("invalid jwt in request");
	    }
	}

	@Override
	public StockResponseDto deleteStock(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
	    	//credentials should be a valid employee; only an active employee can delete stock
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
	        	throw new NotAuthorizedException("logged in user is not an admin -- -- cannot modify stock");
	        }
			
			//look for stock item
			Optional<Stock> optionalStock = stockRepository.findById(id);
			if(optionalStock.isEmpty()) {
				throw new BadRequestException("stock with provided id not found");
			}
			
			Stock foundStock = optionalStock.get();
			stockRepository.delete(foundStock);
			return stockMapper.entityToDto(foundStock);
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
