package com.jvc.scmb.dtos;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.entities.OrderedItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderResponseDto {
	private Long id; 
	
	private Long customer_id;

	@JsonIgnoreProperties("id")
	private List<OrderedItem> ordered_items;
	
    private Timestamp date;
    
    private Invoice invoice;
}
