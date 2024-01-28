package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.StockRequestDto;
import com.jvc.scmb.dtos.StockResponseDto;

public interface StockService {

	StockResponseDto getStock(Long id, EmployeeRequestDto employeeRequestDto);

	List<StockResponseDto> getAllStockItems(EmployeeRequestDto employeeRequestDto);

	StockResponseDto addStock(StockRequestDto stockRequestDto);

	StockResponseDto patchStock(Long id, StockRequestDto stockRequestDto);

	StockResponseDto deleteStock(Long id, EmployeeRequestDto employeeRequestDto);

}
