package com.jvc.scmb.services;

import com.jvc.scmb.dtos.EmployeeRequestDto;
import com.jvc.scmb.dtos.InvoiceResponseDto;

public interface InvoiceService {

	InvoiceResponseDto getInvoice(Long id, String token);

	InvoiceResponseDto assignEmployee(Long id, String token, EmployeeRequestDto employeeRequestDto);

}
