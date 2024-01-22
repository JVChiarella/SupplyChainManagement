package com.jvc.scmb.services.impl;

import org.springframework.stereotype.Service;

import com.jvc.scmb.repositories.OrderRepository;
import com.jvc.scmb.services.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
}
