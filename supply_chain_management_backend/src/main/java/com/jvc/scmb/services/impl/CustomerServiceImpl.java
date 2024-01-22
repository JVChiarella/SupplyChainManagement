package com.jvc.scmb.services.impl;

import org.springframework.stereotype.Service;

import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.services.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
}
