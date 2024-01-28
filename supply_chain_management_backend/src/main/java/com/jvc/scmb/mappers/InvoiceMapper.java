package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.jvc.scmb.dtos.InvoiceRequestDto;
import com.jvc.scmb.dtos.InvoiceResponseDto;
import com.jvc.scmb.entities.Invoice;

@Mapper(componentModel = "spring", uses = { OrderMapper.class } )
public interface InvoiceMapper {

	InvoiceResponseDto entityToDto(Invoice invoice);
	
	Invoice requestDtoToEntity(InvoiceRequestDto invoiceRequestDto);
	
	List<Invoice> requestEntitiesToDtos(List<InvoiceRequestDto> invoiceRequestDtos);
}
