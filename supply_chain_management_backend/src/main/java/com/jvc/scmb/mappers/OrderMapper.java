package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;
import com.jvc.scmb.entities.Order;

@Mapper(componentModel = "spring", uses = { CustomerMapper.class, InvoiceMapper.class, CredentialsMapper.class} )
public interface OrderMapper {

	OrderResponseDto entityToDto(Order order);
	
	@Mapping(target = "customer", source = "customerRequestDto")
	Order requestDtoToEntity(OrderRequestDto orderRequestDto);
	
	List<OrderResponseDto> requestEntitiesToDtos(List<Order> foundOrders);
}
