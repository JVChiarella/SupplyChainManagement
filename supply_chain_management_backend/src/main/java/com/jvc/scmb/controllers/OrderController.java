package com.jvc.scmb.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;
import com.jvc.scmb.services.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	//get one order by id
	@GetMapping("/{id}")
	public OrderResponseDto getOrder(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
		return orderService.getOrder(id, userRequestDto);
	}
	
	//get all orders from a customer (by customer id)
	@GetMapping("/customer/{id}")
	public List<OrderResponseDto> getAllOrdersByCustomer(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto){
		return orderService.getAllOrdersByCustomer(id, userRequestDto);
	}
	
	//post order
	@PostMapping()
	public OrderResponseDto addOrder(@RequestBody OrderRequestDto orderRequestDto) {
		return orderService.addOrder(orderRequestDto);
	}
	
	//patch order
	@PatchMapping("/{id}")
	public OrderResponseDto patchOrder(@PathVariable Long id, @RequestBody OrderRequestDto orderRequestDto) {
		return orderService.patchOrder(id, orderRequestDto);
	}
	
	//delete order (soft delete via patch)
	@PatchMapping("/delete/{id}")
	public OrderResponseDto deleteOrder(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
		return orderService.deleteOrder(id, userRequestDto);
	}

}
