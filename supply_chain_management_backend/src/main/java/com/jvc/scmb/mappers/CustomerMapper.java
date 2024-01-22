package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.entities.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
	CustomerResponseDto entityToDto(Customer customer);
	
	Customer requestDtoToEntity(CustomerRequestDto customerRequestDto);
	
	List<Customer> requestEntitiesToDtos(List<CustomerRequestDto> customerRequestDtos);
}
