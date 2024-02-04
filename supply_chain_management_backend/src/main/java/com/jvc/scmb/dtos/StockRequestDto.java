package com.jvc.scmb.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockRequestDto {
	
	@NotNull(message = "name of stock item must be provided")
	private String name;
	
	private String description;
	
	private int count; 
	
	private double price;
}
