package com.jvc.scmb.services.impl;

import org.springframework.stereotype.Service;

import com.jvc.scmb.repositories.InvoiceRepository;
import com.jvc.scmb.services.InvoiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService{

	private final InvoiceRepository invoiceRepository;
}
