package com.jvc.scmb.dtos;

import com.jvc.scmb.entities.Credentials;
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
	
	@NotNull(message = "order number must be provided")
	private Order order;
	
	private double totalPrice;
	
	private String status;
	
	private Employee employee;
}
