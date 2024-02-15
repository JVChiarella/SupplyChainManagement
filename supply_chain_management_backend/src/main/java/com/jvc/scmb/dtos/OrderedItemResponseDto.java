package com.jvc.scmb.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderedItemResponseDto {

	private Long stock_id;
	
	private int amount;
}
