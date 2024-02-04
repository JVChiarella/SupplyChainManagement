package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.StockRequestDto;
import com.jvc.scmb.dtos.StockResponseDto;

public interface StockService {

	StockResponseDto getStock(Long id, String token);

	List<StockResponseDto> getAllStockItems(String token);

	StockResponseDto addStock(StockRequestDto stockRequestDto, String token);

	StockResponseDto patchStock(Long id, StockRequestDto stockRequestDto, String token);

	StockResponseDto deleteStock(Long id, String token);

}
