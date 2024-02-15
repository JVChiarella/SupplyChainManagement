package com.jvc.scmb.services.impl;

import java.security.Key;
import java.util.ArrayList;
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
import com.jvc.scmb.mappers.OrderMapper;
import com.jvc.scmb.repositories.CustomerRepository;
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
	private final InvoiceRepository invoiceRepository;
	private final OrderedItemRepository orderedItemRepository;
	private final StockRepository stockRepository;
	
	@Value("${jwt.secret}")
	private String secret;
	
	//get one order
	@Override
	public OrderResponseDto getOrder(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
		    
			//look for order
			Optional<Order> optionalOrder = orderRepository.findById(id);
			if(optionalOrder.isEmpty()) {
				throw new BadRequestException("order with provided id not found");
			}
			
			Order foundOrder = optionalOrder.get();
		    
		    //check that jwt belongs to the customer who placed the order or an employee
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	;
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	if(!foundOrder.getCustomer().getCredentials().getUsername().equals(jwt.getBody().get("username"))) {
		    		throw new BadRequestException("only an employee or the customer who placed the order can view its details");
		    	}
		    } else {
		    	throw new BadRequestException("only an employee or the customer who placed the order can view its details");
		    }
			
			return orderMapper.entityToDto(foundOrder);
	    } catch (Exception e) {
	    	throw new BadRequestException(e.getMessage());
	    }
	}
	
	//get all orders made by customer
	@Override
	public List<OrderResponseDto> getAllOrdersByCustomer(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
		    
	 		//look up customer who's id was provided
	 		Optional<Customer> optionalCustomer = customerRepository.findById(id);
	 		if(optionalCustomer.isEmpty()) {
	 			throw new BadRequestException("customer with provided id not found");
	 		}
	 		
			Customer customer = optionalCustomer.get();
			List<Order> foundOrders = customer.getOrders();
		    
		    //check that jwt belongs to the customer who placed the order or an employee
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	;
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	if(!customer.getCredentials().getUsername().equals(jwt.getBody().get("username"))) {
		    		throw new BadRequestException("only an employee or the customer who placed the orders can view their details");
		    	}
		    } else {
		    	throw new BadRequestException("only an employee or the customer who placed the orders can view their details");
		    }
			
			return orderMapper.requestEntitiesToDtos(foundOrders);
	    } catch (Exception e) {
	    	throw new BadRequestException(e.getMessage());
	    }
	}
	
	@Override
	public OrderResponseDto addOrder(OrderRequestDto orderRequestDto, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
		    //check that jwt belongs to a customer
	    	 if(jwt.getBody().getSubject().equals("customer")) {
		    	;
		    } else {
		    	throw new BadRequestException("only a customer can place an order");
		    }
    	 
			//find customer from db
			Optional<Customer> optCus = customerRepository.findById(orderRequestDto.getCustomer_id());
			Customer customer = optCus.get();
			
			//check that jwt and customer making request match
			if(!jwt.getBody().get("username").equals(customer.getCredentials().getUsername())) {
		    	throw new BadRequestException("customer placing order and owner of jwt do not match");
		    }
			
			//check that customer is active
			if(!customer.getActive()) {
				throw new BadRequestException("only active customers can place orders");
			}
			
			//create new order from dto
			Order newOrder = orderMapper.requestDtoToEntity(orderRequestDto);
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
				
				//check that enough count of stock is available
				if(stock.getCount() == 0) {
					throw new BadRequestException("no stock available for item with id " + stock.getId());
				} else if(stock.getCount()-oi.getAmount() < 0) {
					//give customer all remaining stock of the item
					item.setAmount(stock.getCount());
					stock.setCount(0);
				} else {
					item.setAmount(oi.getAmount());
					stock.setCount(stock.getCount()-oi.getAmount());
				}
				item.setStock(stock);
				item.setOrder(newOrder);
				items.add(item);
				
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
	    } catch (Exception e) {
	    	throw new BadRequestException(e.getMessage());
	    }
	}

	//bug here. not currently working -----------------------------------------
	@Override
	public OrderResponseDto patchOrder(Long id, OrderRequestDto orderRequestDto, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
			//find order in db
			Optional<Order> optionalOrder = orderRepository.findById(id);
			if(optionalOrder.isEmpty()) {
				throw new BadRequestException("order with provided id not found");
			}
			
			Order order = optionalOrder.get();
			
		    //check that jwt belongs to the customer who placed the order or an employee
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	;
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	if(!order.getCustomer().getCredentials().getUsername().equals(jwt.getBody().get("username"))) {
		    		throw new BadRequestException("only an employee or the customer who placed the order can view its details");
		    	}
		    } else {
		    	throw new BadRequestException("only an employee or the customer who placed the order can view its details");
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
			
			invoiceRepository.delete(order.getInvoice());
			order.setInvoice(null);
			
			itemsToDelete = null;
			stockChanges = null;
			
			//bug is in here (concurrent modification?)--------------------------------------
			//update order to new items and update stock
			//loop through ordered items and add to ordered items array + update stock numbers
			List<OrderedItem> items = order.getOrdered_items();
			List<Stock> stockChanges2 = new ArrayList<>();
			for(OrderedItemDto oi : orderRequestDto.getOrdered_items()) {
				OrderedItem item = new OrderedItem();
				Optional<Stock> optStock = stockRepository.findById(oi.getStock_id());
				if(optStock.isEmpty()) {
					throw new IllegalArgumentException("invalid stock item id passed");
				}
				Stock stock = optStock.get();
				
				//check that enough count of stock is available
				if(stock.getCount() == 0) {
					throw new BadRequestException("no stock available for item with id " + stock.getId());
				} else if(stock.getCount()-oi.getAmount() < 0) {
					//give customer all remaining stock of the item
					item.setAmount(stock.getCount());
					stock.setCount(0);
				} else {
					item.setAmount(oi.getAmount());
					stock.setCount(stock.getCount()-oi.getAmount());
				}
				item.setStock(stock);
				item.setOrder(order);
				items.add(item);
				
				stockChanges2.add(stock);
			}
			order.setOrdered_items(items);
			
			//create invoice, save all to database and return
			orderedItemRepository.saveAllAndFlush(items);
			orderRepository.saveAndFlush(order);
			stockRepository.saveAllAndFlush(stockChanges2);
			//e.printStackTrace();
			
			Invoice newInvoice = new Invoice(order);
			invoiceRepository.saveAndFlush(newInvoice);
			order.setInvoice(newInvoice);
			
			return orderMapper.entityToDto(orderRepository.saveAndFlush(order));
			
	    } catch (Exception e) {
	    	throw new BadRequestException(e.getMessage());
	    }
	}

	//bug here with JWT authentication - not working. worked before JWT authentication added-----------------------------------------
	@Override
	public OrderResponseDto deleteOrder(Long id, String token) {
		//verify jwt from header of request
		token = JwtVerification(token);
		
	    Key key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	    try {
	    	 Jws<Claims> jwt = Jwts.parserBuilder()
		            .setSigningKey(key)
		            .build()
		            .parseClaimsJws(token);
	    	 
			//find order in db
			Optional<Order> optionalOrder = orderRepository.findById(id);
			if(optionalOrder.isEmpty()) {
				throw new BadRequestException("order with provided id not found");
			}
			
			Order order = optionalOrder.get();
			
		    //check that jwt belongs to the customer who placed the order or an employee
		    if(jwt.getBody().getSubject().equals("employee")) {
		    	;
		    } else if(jwt.getBody().getSubject().equals("customer")) {
		    	if(!order.getCustomer().getCredentials().getUsername().equals(jwt.getBody().get("username"))) {
		    		throw new BadRequestException("only an employee or the customer who placed the order can view its details");
		    	}
		    } else {
		    	throw new BadRequestException("only an employee or the customer who placed the order can view its details");
		    }
			
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
			order.setOrdered_items(null);
			Invoice invoice = order.getInvoice();
			invoice.setStatus("cancelled");
			invoiceRepository.saveAndFlush(invoice);
			return orderMapper.entityToDto(orderRepository.saveAndFlush(order));
	    } catch (Exception e) {
	    	throw new BadRequestException(e.getMessage());
	    }
	}
	
	public String JwtVerification(String token) {
		if(token == null) {
			throw new IllegalArgumentException("jwt authoriozation failed");
		}
		
		if(!token.startsWith("Bearer")){
			throw new IllegalArgumentException("jwt authoriozation failed");
		}
		
		//remove token prefix
		token = token.replace("Bearer ", "");
		token = token.replace("\"",""); 
		//System.out.println("TOKEN: " + token);
		return token;
	}
}
