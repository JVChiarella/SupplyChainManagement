package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;
import com.jvc.scmb.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

	OrderResponseDto entityToDto(Order order);
	
	Order requestDtoToEntity(OrderRequestDto orderRequestDto);
	
	List<Order> requestEntitiesToDtos(List<OrderRequestDto> orderRequestDtos);
}
