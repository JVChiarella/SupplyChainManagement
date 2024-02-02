package com.jvc.scmb.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;
import com.jvc.scmb.dtos.OrderedItemDto;
import com.jvc.scmb.dtos.UserRequestDto;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.entities.Order;
import com.jvc.scmb.entities.OrderedItem;
import com.jvc.scmb.entities.Stock;
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
		Boolean customerFlag = false;
		if(checkCredentials(userRequestDto).equals("customer")){
			customerFlag = true;
		}
			
		//look for order
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if(optionalOrder.isEmpty()) {
			throw new BadRequestException("order with provided id not found");
		}
		
		Order foundOrder = optionalOrder.get();
		
		//if user is a customer, make sure it is the customer that placed the order
		if(customerFlag) {
			Optional<Customer> optCus = customerRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
			Customer customer = optCus.get();
			if(!foundOrder.getCustomer().equals(customer)) {
				throw new NotAuthorizedException("only the customer who placed the order or an employee can view it");
			}
		}
		return orderMapper.entityToDto(foundOrder);
	}
	
	//get all orders made by customer
	@Override
	public List<OrderResponseDto> getAllOrdersByCustomer(Long id, UserRequestDto userRequestDto) {
		//check credentials
		Boolean customerFlag = false;
		if(checkCredentials(userRequestDto).equals("customer")){
			customerFlag = true;
		}
			
		//look up customer who's id was provided
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if(optionalCustomer.isEmpty()) {
			throw new BadRequestException("customer with provided id not found");
		}
		
		Customer customer = optionalCustomer.get();
		List<Order> foundOrders = customer.getOrders();
		
		//if user is a customer, make sure it is the customer that placed the order
		if(customerFlag) {
			Optional<Customer> optCus = customerRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
			Customer customer2 = optCus.get();
			if(!customer.equals(customer2)) {
				throw new NotAuthorizedException("only the customer who placed the order or an employee can view it");
			}
		}
		return orderMapper.requestEntitiesToDtos(foundOrders);
	}
	
	@Override
	public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
		//check credentials - only a customer can place an order
		if(!checkCredentials(orderRequestDto.getUserRequestDto()).equals("customer")) {
			throw new BadRequestException("only a customer can place an order");
		}
		
		//create new order and set customer from db
		Order newOrder = orderMapper.requestDtoToEntity(orderRequestDto);
		Optional<Customer> optCus = customerRepository.findByCredentialsUsername(orderRequestDto.getUserRequestDto().getCredentials().getUsername());
		Customer customer = optCus.get();
		newOrder.setCustomer(customer);
		
		//loop through ordered items and add to ordered items array + update stock numbers
		List<OrderedItem> items = new ArrayList<>();
		List<Stock> stockChanges = new ArrayList<>();
		for(OrderedItemDto oi : orderRequestDto.getOrdered_items()) {
			OrderedItem item = new OrderedItem();
			Optional<Stock> optStock = stockRepository.findById(oi.getStock_id());
			if(optStock.isEmpty()) {
				throw new IllegalArgumentException("invalid stock item id passed");
			}
			Stock stock = optStock.get();
			item.setStock(stock);
			item.setAmount(oi.getAmount());
			item.setOrder(newOrder);
			items.add(item);
			
			//subtract amount from stock and add stock item to changes array to save later
			stock.setCount(stock.getCount()-oi.getAmount());
			stockChanges.add(stock);
		}
		newOrder.setOrdered_items(items);
		
		//create invoice, save all to database and return
		orderRepository.saveAndFlush(newOrder);
		orderedItemRepository.saveAllAndFlush(items);
		stockRepository.saveAllAndFlush(stockChanges);
		
		Invoice newInvoice = new Invoice(newOrder);
		invoiceRepository.saveAndFlush(newInvoice);
		newOrder.setInvoice(newInvoice);
		return orderMapper.entityToDto(orderRepository.saveAndFlush(newOrder));
	}

	//bug here. not currently working -----------------------------------------
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
			Optional<Customer> optCus = customerRepository.findByCredentialsUsername(orderRequestDto.getUserRequestDto().getCredentials().getUsername());
			Customer customer = optCus.get();
			if(!customer.equals(order.getCustomer())) {
				throw new NotAuthorizedException("only the customer who placed the order or an employee can view it");
			}
		}
		
		//return all old items to stock
		List<OrderedItem> itemsToDelete = new ArrayList<>();
		List<Stock> stockChanges = new ArrayList<>();
		for(OrderedItem item : order.getOrdered_items()) {
			Long item_id = item.getStock().getId();
			Optional<Stock> optStock = stockRepository.findById(item_id);
			if(optStock.isEmpty()) {
				throw new IllegalArgumentException("current order contains invalid stock id. this shouldn't be possible but check anyway");
			}
			
			Stock stock = optStock.get();
			stock.setCount(stock.getCount() + item.getAmount());
			stockChanges.add(stock);
			itemsToDelete.add(item);
		}
		//remove ordered items list from order and update all changes to db
		order.getOrdered_items().removeAll(itemsToDelete);
		orderRepository.save(order);
		orderedItemRepository.deleteAll(itemsToDelete);
		stockRepository.saveAll(stockChanges);
		
		//bug is in here (concurrent modification?)--------------------------------------
		//update order to new items and update stock
		//loop through ordered items and add to ordered items array + update stock numbers
		List<OrderedItem> items = new ArrayList<>();
		stockChanges.clear();
		for(OrderedItemDto oi : orderRequestDto.getOrdered_items()) {
			OrderedItem item = new OrderedItem();
			Optional<Stock> optStock = stockRepository.findById(oi.getStock_id());
			if(optStock.isEmpty()) {
				throw new IllegalArgumentException("invalid stock item id passed");
			}
			Stock stock = optStock.get();
			item.setStock(stock);
			item.setAmount(oi.getAmount());
			item.setOrder(order);
			items.add(item);
			
			//subtract amount from stock and add stock item to changes array to save later
			stock.setCount(stock.getCount()-oi.getAmount());
			stockChanges.add(stock);
		}
		order.setOrdered_items(items);
		
		//create invoice, save all to database and return
		orderRepository.saveAndFlush(order);
		orderedItemRepository.saveAllAndFlush(items);
		stockRepository.saveAllAndFlush(stockChanges);
		
		Invoice newInvoice = new Invoice(order);
		invoiceRepository.saveAndFlush(newInvoice);
		order.setInvoice(newInvoice);
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
			Optional<Customer> optCus = customerRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername());
			Customer customer = optCus.get();
			if(!customer.equals(order.getCustomer())) {
				throw new NotAuthorizedException("only the customer who placed the order or an employee can view it");
			}
		}
		
		//return ordered items to stock
		//return all old items to stock
		for(OrderedItem item : order.getOrdered_items()) {
			Long item_id = item.getStock().getId();
			Optional<Stock> optStock = stockRepository.findById(item_id);
			if(optStock.isEmpty()) {
				throw new IllegalArgumentException("current order contains invalid stock id. this shouldn't be possible but check anyway");
			}
			
			Stock stock = optStock.get();
			stock.setCount(stock.getCount() + item.getAmount());
			orderedItemRepository.delete(item);
			stockRepository.saveAndFlush(stock);
		}
		
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
