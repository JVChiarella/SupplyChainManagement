package com.jvc.scmb.dtos;

import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceRequestDto {
	
	private Order order;
	
	private String status;
	
	private double totalPrice;
	
	private Employee employee;
}
