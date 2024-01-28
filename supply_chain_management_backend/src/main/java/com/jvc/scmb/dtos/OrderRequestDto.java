package com.jvc.scmb.dtos;

import java.util.List;

import com.jvc.scmb.entities.OrderedItem;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderRequestDto {
	
	private UserRequestDto userRequestDto;
	
	private List<OrderedItem> ordered_items;
}
