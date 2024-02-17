package com.jvc.scmb.services;

import java.util.List;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.InvoiceResponseDto;

public interface InvoiceService {

	InvoiceResponseDto getInvoice(Long id, String token);

	InvoiceResponseDto assignEmployee(Long id, String token, EmployeeRequestDto employeeRequestDto);

	List<InvoiceResponseDto> getAllInvoicesByCustomer(Long id, String token);

	List<InvoiceResponseDto> getAllInvoicesByEmployee(Long id, String token);

}
