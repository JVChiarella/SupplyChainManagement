package com.jvc.scmb.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.jvc.scmb.dtos.InvoiceRequestDto;
import com.jvc.scmb.dtos.InvoiceResponseDto;
import com.jvc.scmb.entities.Invoice;

@Mapper(componentModel = "spring", uses = { OrderMapper.class } )
public interface InvoiceMapper {

	@Mapping(target = "employee_id", source = "employee.id")
	InvoiceResponseDto entityToDto(Invoice invoice);
	
	Invoice requestDtoToEntity(InvoiceRequestDto invoiceRequestDto);
	
	@Mapping(target = "employee_id", source = "employee.id")
	List<InvoiceResponseDto> requestEntitiesToDtos(List<Invoice> invoices);
}
