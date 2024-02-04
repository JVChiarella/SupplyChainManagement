package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jvc.scmb.dtos.OrderRequestDto;
import com.jvc.scmb.dtos.OrderResponseDto;
import com.jvc.scmb.dtos.OrderedItemDto;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.entities.Order;
import com.jvc.scmb.entities.OrderedItem;
import com.jvc.scmb.entities.Stock;
import com.jvc.scmb.exceptions.BadRequestException;
import com.jvc.scmb.mappers.CustomerMapper;
import com.jvc.scmb.mappers.OrderMapper;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.repositories.InvoiceRepository;
import com.jvc.scmb.repositories.OrderRepository;
import com.jvc.scmb.repositories.OrderedItemRepository;
import com.jvc.scmb.repositories.StockRepository;
import com.jvc.scmb.services.OrderService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
	
	@Value("${jwt.secret}")
	private String secret;
	
	//get one order
	@Override
	public OrderResponseDto getOrder(Long id, String token) {
		//verify jwt from header of request
		if(token == null) {
			throw new IllegalArgumentException("jwt authoriozation failed");
		}
		
		if(!token.startsWith("Bearer")){
			throw new IllegalArgumentException("jwt authoriozation failed");
		}
		
		//remove token prefix
		token = token.replace("Bearer ", "");
		token = token.replace("\"",""); 
		System.out.println("TOKEN: " + token);
		
		//verify token
		//SignatureAlgorithm sa = SignatureAlgorithm.HS256;
		//SecretKeySpec secretKeySpec = new SecretKeySpec("${jwt.secret}".getBytes(), sa.getJcaName());
		/*
		try {
			Jwts.parser().setSigningKey(secret).parse(token);
		    //Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
		    throw new IllegalArgumentException("Could not verify JWT token integrity!", e);
		}
		*/
		
	    Key hmacKey = new SecretKeySpec(secret.getBytes(), 
	                                    SignatureAlgorithm.HS256.getJcaName());

	    Jws<Claims> jwt = Jwts.parserBuilder()
	            .setSigningKey(hmacKey)
	            .build()
	            .parseClaimsJws(token);
		
		//look for order
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if(optionalOrder.isEmpty()) {
			throw new BadRequestException("order with provided id not found");
		}
		
		Order foundOrder = optionalOrder.get();
		
		return orderMapper.entityToDto(foundOrder);
	}
	
	//get all orders made by customer
	@Override
	public List<OrderResponseDto> getAllOrdersByCustomer(Long id) {
		//look up customer who's id was provided
		Optional<Customer> optionalCustomer = customerRepository.findById(id);
		if(optionalCustomer.isEmpty()) {
			throw new BadRequestException("customer with provided id not found");
		}
		
		Customer customer = optionalCustomer.get();
		List<Order> foundOrders = customer.getOrders();
		
		return orderMapper.requestEntitiesToDtos(foundOrders);
	}
	
	@Override
	public OrderResponseDto addOrder(OrderRequestDto orderRequestDto) {
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
		//find order in db
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if(optionalOrder.isEmpty()) {
			throw new BadRequestException("order with provided id not found");
		}
		
		Order order = optionalOrder.get();
		
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
	public OrderResponseDto deleteOrder(Long id) {
		//find order in db
		Optional<Order> optionalOrder = orderRepository.findById(id);
		if(optionalOrder.isEmpty()) {
			throw new BadRequestException("order with provided id not found");
		}
		
		Order order = optionalOrder.get();
		
		//return ordered items to stock
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
}
