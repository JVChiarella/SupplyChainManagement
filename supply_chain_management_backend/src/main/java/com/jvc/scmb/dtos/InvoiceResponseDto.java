package com.jvc.scmb.dtos;

import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceResponseDto {
	private Order order;
	
	private double totalPrice;
	
	private String status;
	
	private Employee employee;
}
