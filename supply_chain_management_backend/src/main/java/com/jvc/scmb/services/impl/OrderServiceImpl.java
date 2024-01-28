package com.jvc.scmb.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;
import com.jvc.scmb.dtos.UserRequestDto;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.entities.Order;
import com.jvc.scmb.entities.OrderedItem;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.exceptions.NotAuthorizedException;
import com.jvc.scmb.mappers.CustomerMapper;
import com.jvc.scmb.mappers.OrderMapper;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.repositories.InvoiceRepository;
import com.jvc.scmb.repositories.OrderRepository;
import com.jvc.scmb.repositories.OrderedItemRepository;
import com.jvc.scmb.repositories.StockRepository;
import com.jvc.scmb.services.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;
	private final EmployeeRepository employeeRepository;
	private final InvoiceRepository invoiceRepository;
	private final OrderedItemRepository orderedItemRepository;
	private final StockRepository stockRepository;
	
	//get one order
	@Override
	public OrderResponseDto getOrder(Long id, UserRequestDto userRequestDto) {
		//check credentials
		checkCredentials(userRequestDto);
			
		//look for order
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if(optionalOrder.isEmpty()) {
			throw new BadRequestException("order with provided id not found");
		}
		
		//convert order to dto and return
		Order foundOrder = optionalOrder.get();
		return orderMapper.entityToDto(foundOrder);
	}
	
	//get one order
	@Override
	public List<OrderResponseDto> getAllOrdersByCustomer(Long id, UserRequestDto userRequestDto) {
		//check credentials
		checkCredentials(userRequestDto);
			
		//look for order
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if(optionalCustomer.isEmpty()) {
			throw new BadRequestException("customer with provided id not found");
		}
		
		//convert order to dto and return
		List<Order> foundOrders = optionalCustomer.get().getOrders();
		return orderMapper.requestEntitiesToDtos(foundOrders);
	}
	
	@Override
	public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
		//check credentials - only a customer can place an order
		if(!checkCredentials(orderRequestDto.getUserRequestDto()).equals("customer")) {
			throw new BadRequestException("only a customer can place an order");
		}
		
		//add order to db, create invoice, save and return
		Order newOrder = orderMapper.requestDtoToEntity(orderRequestDto);
		newOrder.setCustomer(customerMapper.requestDtoToEntity(orderRequestDto.getUserRequestDto().getCustomerRequestDto()));
		Invoice newInvoice = new Invoice(newOrder);
		invoiceRepository.saveAndFlush(newInvoice);
		newOrder.setInvoice(newInvoice);
		return orderMapper.entityToDto(orderRepository.saveAndFlush(newOrder));
	}

	@Override
	public OrderResponseDto patchOrder(Long id, OrderRequestDto orderRequestDto) {
		//check credentials
		Boolean customerFlag = false;
		if(checkCredentials(orderRequestDto.getUserRequestDto()).equals("customer")){
			customerFlag = true;
		}
		
		//find order in db
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if(optionalOrder.isEmpty()) {
			throw new BadRequestException("order with provided id not found");
		}
		
		Order order = optionalOrder.get();
		
		//if user is a customer, make sure it is the customer that placed the order
		if(customerFlag) {
			if(!order.getCustomer().equals(customerMapper.requestDtoToEntity(orderRequestDto.getUserRequestDto().getCustomerRequestDto()))) {
				throw new NotAuthorizedException("only the customer who placed the order can make changes to it");
			}
		}
		
		//return all old items to stock
		
		//update order to new items and update stock
		List<OrderedItem> updatedItems = orderRequestDto.getOrdered_items();
		order.setOrdered_items(updatedItems);
		
		//delete old invoice and generate new invoice
		Invoice invoice = order.getInvoice();
		invoiceRepository.delete(invoice);
		order.setInvoice(null);
		Invoice newInvoice = new Invoice(order);
		order.setInvoice(newInvoice);
		
		//save and return
		orderRepository.saveAndFlush(order);
		orderedItemRepository.saveAllAndFlush(updatedItems);
		//stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		invoiceRepository.saveAndFlush(newInvoice);
		return orderMapper.entityToDto(orderRepository.saveAndFlush(order));
		
	}

	@Override
	public OrderResponseDto deleteOrder(Long id, UserRequestDto userRequestDto) {
		//check credentials
		Boolean customerFlag = false;
		if(checkCredentials(userRequestDto).equals("customer")){
			customerFlag = true;
		}
		
		//find order in db
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if(optionalOrder.isEmpty()) {
			throw new BadRequestException("order with provided id not found");
		}
		
		Order order = optionalOrder.get();
		
		//if user is a customer, make sure it is the customer that placed the order
		if(customerFlag) {
			if(!order.getCustomer().equals(customerMapper.requestDtoToEntity(userRequestDto.getCustomerRequestDto()))) {
				throw new NotAuthorizedException("only the customer who placed the order can make changes to it");
			}
		}
		
		//return ordered items to stock
		
		//soft delete order and return
		Invoice invoice = order.getInvoice();
		invoice.setStatus("cancelled");
		invoiceRepository.saveAndFlush(invoice);
		return orderMapper.entityToDto(order);
		
	}
	
	public String checkCredentials(UserRequestDto userRequestDto) {
		//check credentials were provided
        if(userRequestDto.getCredentials().getUsername() == null || 
        		userRequestDto.getCredentials().getPassword() == null ) {
            throw new BadRequestException("one or more fields missing in request");
        }
        
		//check what type of user is making request
		if(userRequestDto.getType().equals("employee")) {
	        //credentials should be a valid employee
	        Optional<Employee> optionalUser = employeeRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
	        if(optionalUser.isEmpty()) {
	        	throw new NotAuthorizedException("user with provided credentials not found");
	        }

	        //check that found user is active
	        Employee foundEmployee = optionalUser.get();
	        if(!foundEmployee.getActive()) {
	        	throw new NotAuthorizedException("non-active user");
	        }
	        
	        //check password of employee making request
	        if(!foundEmployee.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
	            throw new NotAuthorizedException("password incorrect");
	        }
	        
	        String result = "employee";
	        return result;
		} else if(userRequestDto.getType().equals("customer")) {
	        //credentials should be a valid customer
	        Optional<Customer> optionalUser = customerRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
	        if(optionalUser.isEmpty()) {
	        	throw new NotAuthorizedException("user with provided credentials not found");
	        }

	        //check that found user is active
	        Customer foundCustomer = optionalUser.get();
	        if(!foundCustomer.getActive()) {
	        	throw new NotAuthorizedException("non-active user");
	        }
	        
	        //check password of employee making request
	        if(!foundCustomer.getCredentials().getPassword().equals(userRequestDto.getCredentials().getPassword())) {
	            throw new NotAuthorizedException("password incorrect");
	        }
	        
	        String result = "customer";
	        return result;
		} else {
			throw new BadRequestException("invalid type provided");
		}
	}
}
