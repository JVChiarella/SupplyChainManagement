package com.jvc.scmb.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockResponseDto {
	private String name;
	
	private String description;
	
	private int count; 
	
	private double price;
}
