package com.jvc.scmb.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@GetMapping("/{id}")
	public StockResponseDto getStock(@PathVariable Long id, @RequestHeader (name="Authorization") String token) {
		return StockService.getStock(id, token);
	}
	
	//get all items in stock
	@GetMapping()
	public List<StockResponseDto> getAllStockItems(@RequestHeader (name="Authorization") String token){
		return StockService.getAllStockItems(token);
	}
	
	//post a new item to stock
	@PostMapping("/new")
	public StockResponseDto addStock(@RequestBody StockRequestDto stockRequestDto, @RequestHeader (name="Authorization") String token) {
		return StockService.addStock(stockRequestDto, token);
	}
	
	//patch an item in stock
	@PatchMapping("/patch/{id}")
	public StockResponseDto patchStock(@PathVariable Long id, @RequestBody StockRequestDto stockRequestDto, @RequestHeader (name="Authorization") String token) {
		return StockService.patchStock(id, stockRequestDto, token);
	}
	
	//delete stock item (soft delete via patch)
	@PatchMapping("/delete/{id}")
	public StockResponseDto deleteStock(@PathVariable Long id, @RequestHeader (name="Authorization") String token) {
		return StockService.deleteStock(id, token);
	}

}
