package com.jvc.scmb.dtos;

import java.sql.Timestamp;
import java.util.List;

import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.entities.OrderedItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderResponseDto {
	private Customer customer;

	private List<OrderedItem> ordered_items;
	
    private Timestamp date;
	
	private Invoice invoice;
}
