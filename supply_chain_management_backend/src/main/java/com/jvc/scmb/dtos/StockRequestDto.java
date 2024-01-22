package com.jvc.scmb.dtos;

import com.jvc.scmb.entities.Credentials;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockRequestDto {
	
	@NotNull(message = "credentials must be provided to perform request")
	private Credentials credentials; 
	
	@NotNull(message = "name of stock item must be provided")
	private String name;
	
	private String description;
	
	private int count; 
	
	private double price;
}
