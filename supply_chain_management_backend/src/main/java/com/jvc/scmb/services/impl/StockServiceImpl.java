package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.StockRequestDto;
import com.jvc.scmb.dtos.StockResponseDto;
import com.jvc.scmb.entities.Customer;
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
        
		    //check if user is employee or customer
	        String username = (String)jwt.getBody().get("username");
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(username);
		    	Employee loggedUser = optionalUser.get();
		    	
		        //check that found user is active
		        if(!loggedUser.getActive()) {
		        	throw new NotAuthorizedException("non-active user");
		        }
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	Optional<Customer> optionalUser = customerRepository.findByCredentialsUsername(username);
		    	Customer loggedUser = optionalUser.get();
		    	
		        //check that found user is active
		        if(!loggedUser.getActive()) {
		        	throw new NotAuthorizedException("non-active user");
		        }
		    } else {
		    	throw new BadRequestException("logged in user not found");
		    }
	        
			//look for stock item
			Optional<Stock> optionalStock = stockRepository.findById(id);
			if(optionalStock.isEmpty()) {
				throw new BadRequestException("stock with provided id not found");
			}
			
			Stock foundStock = optionalStock.get();
			return stockMapper.entityToDto(foundStock);
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
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
        
		    //check if user is employee or customer
	        String username = (String)jwt.getBody().get("username");
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(username);
		    	Employee loggedUser = optionalUser.get();
		    	
		        //check that found user is active
		        if(!loggedUser.getActive()) {
		        	throw new NotAuthorizedException("non-active user");
		        }
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	Optional<Customer> optionalUser = customerRepository.findByCredentialsUsername(username);
		    	Customer loggedUser = optionalUser.get();
		    	
		        //check that found user is active
		        if(!loggedUser.getActive()) {
		        	throw new NotAuthorizedException("non-active user");
		        }
		    } else {
		    	throw new BadRequestException("logged in user not found");
		    }
			
			//get all active stock items and return
			List<Stock> stockItems = stockRepository.findAll();
			List<Stock> activeStock = new ArrayList<>();
			for(Stock item : stockItems) {
				if(item.isActive()) {
					activeStock.add(item);
				}
			}
			return stockMapper.requestEntitiesToDtos(activeStock);
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
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
			stock.setActive(true);
			return stockMapper.entityToDto(stockRepository.saveAndFlush(stock));
			
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
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
			
			//apply supplied changes, convert entity to dto and return
			//if nums were not provided on frontend, they were set to -1 to avoid java default value of 0
			Stock newStock = stockMapper.requestDtoToEntity(stockRequestDto);
			if(!newStock.getName().equals("")) {
				foundStock.setName(newStock.getName());
			}
			if(!(newStock.getCount() < 0)) {
				foundStock.setCount(newStock.getCount());
			}
			if(!newStock.getDescription().equals("")) {
				foundStock.setDescription(newStock.getDescription());
			}
			if(!(newStock.getPrice() < 0)) {
				foundStock.setPrice(newStock.getPrice());
			}
			
			return stockMapper.entityToDto(stockRepository.saveAndFlush(foundStock));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
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
			foundStock.setActive(false);
			return stockMapper.entityToDto(stockRepository.saveAndFlush(foundStock));
	    } catch (Exception e) {
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
