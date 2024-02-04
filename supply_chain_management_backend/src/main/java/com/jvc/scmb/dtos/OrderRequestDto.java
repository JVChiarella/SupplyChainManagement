package com.jvc.scmb.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderRequestDto {
	
	private CustomerRequestDto customerRequestDto;
	
	private List<OrderedItemDto> ordered_items;
}
