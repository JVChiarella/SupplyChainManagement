package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.jvc.scmb.dtos.CustomerRequestDto;
import com.jvc.scmb.dtos.CustomerResponseDto;
import com.jvc.scmb.entities.Customer;

@Mapper(componentModel = "spring", uses = { CredentialsMapper.class } )
public interface CustomerMapper {
	CustomerResponseDto entityToDto(Customer customer);
	
	Customer requestDtoToEntity(CustomerRequestDto customerRequestDto);
	
	List<CustomerResponseDto> requestEntitiesToDtos(List<Customer> customers);
}
