package com.jvc.scmb.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.StockRequestDto;
import com.jvc.scmb.dtos.StockResponseDto;
import com.jvc.scmb.services.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("stock")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class StockController {
	
	private final StockService StockService;
	
	//get one stock item by id
	@PostMapping("/{id}")
	public StockResponseDto getStock(@PathVariable Long id, @RequestBody EmployeeRequestDto employeeRequestDto) {
		return StockService.getStock(id, employeeRequestDto);
	}
	
	//get all items in stock
	@PostMapping()
	public List<StockResponseDto> getAllStockItems(@RequestBody EmployeeRequestDto employeeRequestDto){
		return StockService.getAllStockItems(employeeRequestDto);
	}
	
	//post a new item to stock
	@PostMapping("/new")
	public StockResponseDto addStock(@RequestBody StockRequestDto stockRequestDto) {
		return StockService.addStock(stockRequestDto);
	}
	
	//patch an item in stock
	@PatchMapping("/{id}")
	public StockResponseDto patchStock(@PathVariable Long id, @RequestBody StockRequestDto stockRequestDto) {
		return StockService.patchStock(id, stockRequestDto);
	}
	
	//delete stock item (soft delete via patch)
	@PatchMapping("/delete/{id}")
	public StockResponseDto deleteStock(@PathVariable Long id, @RequestBody EmployeeRequestDto employeeRequestDto) {
		return StockService.deleteStock(id, employeeRequestDto);
	}

}
