package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jvc.scmb.dtos.OrderedItemRequestDto;
import com.jvc.scmb.dtos.OrderedItemResponseDto;
import com.jvc.scmb.entities.OrderedItem;

@Mapper(componentModel = "spring")
public interface OrderedItemMapper {
	
	@Mapping(target = "stock_id", source = "stock.id")
	OrderedItemResponseDto entityToDto(OrderedItem orderedItem);
	
	OrderedItem requestDtoToEntity(OrderedItemRequestDto orderedItemRequestDto);
	
	@Mapping(target = "stock_id", source = "stock.id")
	List<OrderedItemResponseDto> requestEntitiesToDtos(List<OrderedItem> foundOrderedItems);

}
