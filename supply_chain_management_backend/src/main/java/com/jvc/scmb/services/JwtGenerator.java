package com.jvc.scmb.services;

import java.util.Map;

import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;

public interface JwtGenerator {
	Map<String, String> generateEmployeeToken(Employee employee);
	Map<String, String> generateCustomerToken(Customer customer);
}