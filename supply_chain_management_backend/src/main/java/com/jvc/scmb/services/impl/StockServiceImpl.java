package com.jvc.scmb.services.impl;

import org.springframework.stereotype.Service;

import com.jvc.scmb.repositories.StockRepository;
import com.jvc.scmb.services.StockService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

	private final StockRepository stockRepository;
}
