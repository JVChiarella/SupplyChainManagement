package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;

public interface OrderService {

	OrderResponseDto getOrder(Long id, String token);

	List<OrderResponseDto> getAllOrdersByCustomer(Long id);

	OrderResponseDto addOrder(OrderRequestDto orderRequestDto);

	OrderResponseDto patchOrder(Long id, OrderRequestDto orderRequestDto);

	OrderResponseDto deleteOrder(Long id);

}
