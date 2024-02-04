package com.jvc.scmb.controllers;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;
import com.jvc.scmb.services.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class OrderController {
	
	private final OrderService orderService;
	
	//get one order by id
	@GetMapping("/{id}")
	public OrderResponseDto getOrder(@PathVariable Long id, @RequestHeader (name="Authorization") String token) {
		return orderService.getOrder(id, token);
	}
	
	//get all orders from a customer (by customer id)
	@GetMapping("/customer/{id}")
	public List<OrderResponseDto> getAllOrdersByCustomer(@PathVariable Long id, @RequestHeader (name="Authorization") String token){
		return orderService.getAllOrdersByCustomer(id, token);
	}
	
	//post order
	@PostMapping("/new")
	public OrderResponseDto addOrder(@RequestBody OrderRequestDto orderRequestDto, @RequestHeader (name="Authorization") String token) {
		return orderService.addOrder(orderRequestDto, token);
	}
	
	//patch order
	@PatchMapping("/{id}")
	public OrderResponseDto patchOrder(@PathVariable Long id, @RequestBody OrderRequestDto orderRequestDto, @RequestHeader (name="Authorization") String token) {
		return orderService.patchOrder(id, orderRequestDto, token);
	}
	
	//delete order (soft delete via patch)
	@PatchMapping("/delete/{id}")
	public OrderResponseDto deleteOrder(@PathVariable Long id,@RequestHeader (name="Authorization") String token) {
		return orderService.deleteOrder(id, token);
	}

}
