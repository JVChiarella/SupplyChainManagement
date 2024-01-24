package com.jvc.scmb.dtos;

import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvoiceRequestDto {
	
	@NotNull(message = "credentials must be provided to perform request")
	private Credentials credentials;
	
	private Order order;
	
	private Customer customer;
	
	private String status;
	
	private double totalPrice;
	
	private Employee employee;
}
