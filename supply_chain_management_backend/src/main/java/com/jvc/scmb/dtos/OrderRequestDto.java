package com.jvc.scmb.dtos;

import java.util.List;

import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.entities.OrderedItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRequestDto {
	
	@NotNull(message = "credentials must be provided to perform request")
	private Credentials credentials; 
	
	private Customer customer;
	
	private List<OrderedItem> ordered_items;
	
	private Invoice invoice;
}
