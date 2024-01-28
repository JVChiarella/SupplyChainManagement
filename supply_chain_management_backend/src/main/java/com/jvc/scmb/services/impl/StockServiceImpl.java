package com.jvc.scmb.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.EmployeeRequestDto;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

	private final StockRepository stockRepository;
	private final StockMapper stockMapper;
	private final EmployeeRepository employeeRepository;
	private final CustomerRepository customerRepository;
	
	@Override
	public StockResponseDto getStock(Long id, EmployeeRequestDto employeeRequestDto) {
		//check credentials
		checkCredentials(employeeRequestDto);
			
		//look for stock item
		Optional<Stock> optionalStock = stockRepository.findById(id);
		if(optionalStock.isEmpty()) {
			throw new BadRequestException("stock with provided id not found");
		}
		
		Stock foundStock = optionalStock.get();
		return stockMapper.entityToDto(foundStock);
	}

	@Override
	public List<StockResponseDto> getAllStockItems(EmployeeRequestDto employeeRequestDto){
		//check credentials
		checkCredentials(employeeRequestDto);
			
		//get all stock and return
		List<Stock> stockItems = stockRepository.findAll();
		return stockMapper.requestEntitiesToDtos(stockItems);
	}

	@Override
	public StockResponseDto addStock(StockRequestDto stockRequestDto) {
		//check credentials were provided
        if(stockRequestDto.getCredentials().getUsername() == null || 
        		stockRequestDto.getCredentials().getPassword() == null ) {
            throw new BadRequestException("one or more fields missing in request");
        }
        
        //credentials should be a valid employee
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(stockRequestDto.getCredentials().getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee foundEmployee = optionalUser.get();
        if(!foundEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of employee making request
        if(!foundEmployee.getCredentials().getPassword().equals(stockRequestDto.getCredentials().getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
			
		//convert dto to entity and post to db
		Stock stock = stockMapper.requestDtoToEntity(stockRequestDto);
		return stockMapper.entityToDto(stockRepository.saveAndFlush(stock));
	}

	@Override
	public StockResponseDto patchStock(Long id, StockRequestDto stockRequestDto) {
		//check credentials were provided
        if(stockRequestDto.getCredentials().getUsername() == null || 
        		stockRequestDto.getCredentials().getPassword() == null ) {
            throw new BadRequestException("one or more fields missing in request");
        }
        
        //credentials should be a valid employee
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(stockRequestDto.getCredentials().getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee foundEmployee = optionalUser.get();
        if(!foundEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of employee making request
        if(!foundEmployee.getCredentials().getPassword().equals(stockRequestDto.getCredentials().getPassword())) {
            throw new NotAuthorizedException("password incorrect");
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
	}

	@Override
	public StockResponseDto deleteStock(Long id, EmployeeRequestDto employeeRequestDto) {
		//check credentials
		checkCredentials(employeeRequestDto);
			
		//look for stock item
		Optional<Stock> optionalStock = stockRepository.findById(id);
		if(optionalStock.isEmpty()) {
			throw new BadRequestException("stock with provided id not found");
		}
		
		Stock foundStock = optionalStock.get();
		stockRepository.delete(foundStock);
		return stockMapper.entityToDto(foundStock);
	}
	
	public void checkCredentials(EmployeeRequestDto employeeRequestDto) {
		//check credentials were provided
        if(employeeRequestDto.getCredentials().getUsername() == null || 
        		employeeRequestDto.getCredentials().getPassword() == null ) {
            throw new BadRequestException("one or more fields missing in request");
        }
        
        //credentials should be a valid employee
        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(employeeRequestDto.getCredentials().getUsername());
        if(optionalUser.isEmpty()) {
        	throw new NotAuthorizedException("user with provided credentials not found");
        }

        //check that found user is active
        Employee foundEmployee = optionalUser.get();
        if(!foundEmployee.getActive()) {
        	throw new NotAuthorizedException("non-active user");
        }
        
        //check password of employee making request
        if(!foundEmployee.getCredentials().getPassword().equals(employeeRequestDto.getCredentials().getPassword())) {
            throw new NotAuthorizedException("password incorrect");
        }
        
        return;
	}
}
