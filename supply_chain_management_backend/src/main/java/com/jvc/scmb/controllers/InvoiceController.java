package com.jvc.scmb.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.InvoiceResponseDto;
import com.jvc.scmb.services.InvoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("invoices")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class InvoiceController {
	
	private final InvoiceService invoiceService;

	//get one invoice
	@GetMapping("/{id}")
	public InvoiceResponseDto getInvoice(@PathVariable Long id, @RequestHeader (name="Authorization") String token) {
		return invoiceService.getInvoice(id, token);
	}
	
	//get all invoices belonging to customer
	@GetMapping("/customer/{id}")
	public List<InvoiceResponseDto> getAllInvoicesByCustomer(@PathVariable Long id, @RequestHeader (name="Authorization") String token) {
		return invoiceService.getAllInvoicesByCustomer(id, token);
	}
	
	//get all invoices in progress by employee
	@GetMapping("/employee/{id}")
	public List<InvoiceResponseDto> getAllInvoicesByEmployee(@PathVariable Long id, @RequestHeader (name="Authorization") String token) {
		return invoiceService.getAllInvoicesByEmployee(id, token);
	}
	
	@PatchMapping("/patch/{id}")
	public InvoiceResponseDto assignEmployee(@PathVariable Long id, @RequestHeader (name="Authorization") String token, @RequestBody EmployeeRequestDto employeeRequestDto) {
		return invoiceService.assignEmployee(id, token, employeeRequestDto);
	}
}
