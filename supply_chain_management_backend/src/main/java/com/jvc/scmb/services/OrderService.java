package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;

public interface OrderService {

	OrderResponseDto getOrder(Long id, UserRequestDto userRequestDto);

	List<OrderResponseDto> getAllOrdersByCustomer(Long id, UserRequestDto userRequestDto);

	OrderResponseDto addOrder(OrderRequestDto orderRequestDto);

	OrderResponseDto patchOrder(Long id, OrderRequestDto orderRequestDto);

	OrderResponseDto deleteOrder(Long id, UserRequestDto userRequestDto);

}
