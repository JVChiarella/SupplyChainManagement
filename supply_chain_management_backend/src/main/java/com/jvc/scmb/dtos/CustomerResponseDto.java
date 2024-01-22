package com.jvc.scmb.dtos;

import java.util.List;

import com.jvc.scmb.entities.Order;

public class CustomerResponseDto {
	private String firstName;
	
	private String lastName;
	
	private String address;
	
	private String phoneNumber;
	
	private List<Order> orders;
}
