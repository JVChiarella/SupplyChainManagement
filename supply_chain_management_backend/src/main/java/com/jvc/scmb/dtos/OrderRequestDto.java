package com.jvc.scmb.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderRequestDto {
	
	private Long customer_id;
	
	private List<OrderedItemRequestDto> ordered_items;
}
