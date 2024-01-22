package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.jvc.scmb.dtos.StockRequestDto;
import com.jvc.scmb.dtos.StockResponseDto;
import com.jvc.scmb.entities.Stock;

@Mapper(componentModel = "spring")
public interface StockMapper {
	StockResponseDto entityToDto(Stock stock);
	
	Stock requestDtoToEntity(StockRequestDto stockRequestDto);
	
	List<Stock> requestEntitiesToDtos(List<StockRequestDto> stockRequestDtos);
}
