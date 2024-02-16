package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.EmployeeResponseDto;
import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.exceptions.NotFoundException;
import com.jvc.scmb.mappers.EmployeeMapper;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.services.EmployeeService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
	
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;
	
	@Value("${jwt.secret}")
	private String secret;
	
	//get all employees
	@Override
    public List<EmployeeResponseDto> getAllEmployees(String token) {
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
		    List<Employee> foundEmployees = employeeRepository.findAll();
		    List<Employee> activeEmployees = new ArrayList<>();
		    for(Employee user : foundEmployees) {
		    	if(user.getActive()) {
		    		activeEmployees.add(user);
		    	}
		    }
    	 
	    	return employeeMapper.requestEntitiesToDtos(activeEmployees);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
	    }
    }
	
	//get one employee
	@Override
	public EmployeeResponseDto getOneEmployee(Long id, String token) {
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
    	 
			Optional<Employee> employee = employeeRepository.findById(id);
			if(employee.isEmpty()) {
				throw new NotFoundException("employee with that id not found");
			}
			
			Employee foundEmployee = employee.get();
			
			//return found employee
			return employeeMapper.entityToDto(foundEmployee);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
	    }
	}
	
	//add employee to DB
	@Override
	public EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
			//check name of employee was provided
	        if(employeeRequestDto.getFirstName() == null || employeeRequestDto.getLastName() == null) {
	            throw new BadRequestException("one or more fields missing in request");
	        }
	
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
			
			//convert dto to new Employee obj
			Employee newEmployee = new Employee();
			newEmployee = employeeMapper.requestDtoToEntity(employeeRequestDto);
			
			//set default username and password for new employee if username and pass not provided
			if(employeeRequestDto.getCredentials().getUsername().equals("") && employeeRequestDto.getCredentials().getPassword().equals("")) {
				Credentials newCredentials = new Credentials();
				newCredentials.setUsername((newEmployee.getFirstName().substring(0, 1).toLowerCase() + newEmployee.getLastName().substring(0, 1).toLowerCase() + newEmployee.getLastName().substring(1)));
				newCredentials.setPassword("password");
				newEmployee.setCredentials(newCredentials);
			}
			
			//add new employee to db, save and return
			return employeeMapper.entityToDto(employeeRepository.saveAndFlush(newEmployee));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
	    }
	}
	
	//delete an employee
	@Override
	public EmployeeResponseDto deleteEmployee(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	
	        //credentials should be a valid employee; only an active admin employee can delete an employee
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
	        	throw new NotAuthorizedException("logged in user is not an admin -- cannot delete an employee");
	        }
	        
	        //find employee to delete
			Optional<Employee> employee = employeeRepository.findById(id);
			if(employee.isEmpty()) {
				throw new NotFoundException("employee with that id not found");
			}
			
			Employee foundEmployee = employee.get();
			
			//delete employee (via status flag), save to db, and return deleted employee
			foundEmployee.setActive(false);
			return employeeMapper.entityToDto(employeeRepository.saveAndFlush(foundEmployee));
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw new BadRequestException(e.getMessage());
	    }
	}
	
	//patch employee
	@Override
	public EmployeeResponseDto patchEmployee(Long id, EmployeeRequestDto employeeRequestDto, String token) {
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
	        Employee loggedEmployee = optionalUser.get();
	        if(!loggedEmployee.getActive()) {
	        	throw new NotAuthorizedException("non-active user");
	        }
	        
	        //check that logged in user is an admin
	        if(!loggedEmployee.getAdmin()) {
	        	throw new NotAuthorizedException("logged in user is not an admin -- cannot delete an employee");
	        }
	        
	        //find employee whose data is to be updated
	        optionalUser = employeeRepository.findById(id);
	        if(optionalUser.isEmpty()) {
	        	throw new NotFoundException("user with provided id not found");
	        }
	        
	        Employee foundEmployee = optionalUser.get();
			
			//update employee data with provided new data
	        Employee newEmployeeData = employeeMapper.requestDtoToEntity(employeeRequestDto);
			if(!(newEmployeeData.getAdmin() == null)) {
				foundEmployee.setAdmin(newEmployeeData.getAdmin());
			}
			if(!newEmployeeData.getFirstName().equals("")) {
				foundEmployee.setFirstName(newEmployeeData.getFirstName());
			}
			if(!newEmployeeData.getLastName().equals("")) {
				foundEmployee.setLastName(newEmployeeData.getLastName());
			}
			if(!newEmployeeData.getPhoneNumber().equals("")) {
				foundEmployee.setPhoneNumber(newEmployeeData.getPhoneNumber());
			}
			
			//add new employee to db, save and return
			return employeeMapper.entityToDto(employeeRepository.saveAndFlush(foundEmployee));
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
