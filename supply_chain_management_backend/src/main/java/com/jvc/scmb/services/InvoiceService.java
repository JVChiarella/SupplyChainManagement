package com.jvc.scmb.services;

import com.jvc.scmb.dtos.InvoiceResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;

public interface InvoiceService {

	InvoiceResponseDto getInvoice(Long id, UserRequestDto userRequestDto);

}
