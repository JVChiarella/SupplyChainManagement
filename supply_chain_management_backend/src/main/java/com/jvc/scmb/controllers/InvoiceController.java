package com.jvc.scmb.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jvc.scmb.dtos.InvoiceResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;
import com.jvc.scmb.services.InvoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("invoices")
@RequiredArgsConstructor
@CrossOrigin(origins="*")
public class InvoiceController {
	
	private final InvoiceService invoiceService;

	@PostMapping("/{id}")
	public InvoiceResponseDto getInvoice(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
		return invoiceService.getInvoice(id, userRequestDto);
	}
}
