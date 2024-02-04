package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;

public interface OrderService {

	OrderResponseDto getOrder(Long id, String token);

	List<OrderResponseDto> getAllOrdersByCustomer(Long id, String token);

	OrderResponseDto addOrder(OrderRequestDto orderRequestDto, String token);

	OrderResponseDto patchOrder(Long id, OrderRequestDto orderRequestDto, String token);

	OrderResponseDto deleteOrder(Long id, String token);

}
