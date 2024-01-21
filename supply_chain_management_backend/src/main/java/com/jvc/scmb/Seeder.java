package com.jvc.scmb;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Order;
import com.jvc.scmb.entities.OrderedItem;
import com.jvc.scmb.entities.Stock;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.OrderRepository;
import com.jvc.scmb.repositories.OrderedItemRepository;
import com.jvc.scmb.repositories.StockRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
	private final CustomerRepository customerRepository;
	private final OrderRepository orderRepository;
	private final OrderedItemRepository orderedItemRepository;
	private final StockRepository stockRepository;
	
	@Override
	public void run(String... args) throws Exception {
		//-- Stock --
		Stock item1 = new Stock();
		item1.setName("Blinds");
		item1.setDescription("42\" x 72\" Blinds");
		item1.setCount(100);
		
		Stock item2 = new Stock();
		item2.setName("Ice Melt");
		item2.setDescription("50lbs Bag of Ice Melt");
		item2.setCount(50);
		
		Stock item3 = new Stock();
		item3.setName("AAA Batteries");
		item3.setDescription("24 Pack of AAA Batteries");
		item3.setCount(1000);
		
		Stock item4 = new Stock();
		item4.setName("AA Batteries");
		item4.setDescription("24 Pack of AA Batteries");
		item4.setCount(1000);
		
		Stock item5 = new Stock();
		item5.setName("9V Batteries");
		item5.setDescription("12 Pack of 9V Batteries");
		item5.setCount(500);
		
		Stock item6 = new Stock();
		item6.setName("Rags");
		item6.setDescription("6 Pack of Shop Rags");
		item6.setCount(1000);
		
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));;
		
		//-- Customers --
		Customer customer1 = new Customer();
		customer1.setFirstName("Jason");
		customer1.setLastName("Chiarella");
		customer1.setAddress("666 Devil Way, Shithole, MS 69696");
		customerRepository.saveAndFlush(customer1);
		
		Customer customer2 = new Customer();
		customer2.setFirstName("Testy");
		customer2.setLastName("Test");
		customer2.setAddress("123 Test St., Testtown, TS 10101");
		customerRepository.saveAndFlush(customer2);
		
		 //-- Orders --
		//---------------------------------------
		Order order1 = new Order();
		order1.setCustomer(customer1);

		//order items
		OrderedItem oi1 = new OrderedItem();
		OrderedItem oi2 = new OrderedItem();
		OrderedItem oi3 = new OrderedItem();
		oi1.setStock(item2);
		oi1.setOrder(order1);
		oi1.setAmount(5);
		oi2.setStock(item3);
		oi2.setOrder(order1);
		oi2.setAmount(10);
		oi3.setStock(item6);
		oi3.setOrder(order1);
		oi3.setAmount(20);
		order1.setOrdered_items(Arrays.asList(oi1, oi2, oi3));
		
		//change counts in stock table
		item2.setCount(item2.getCount()-10);
		item3.setCount(item3.getCount()-20);
		item6.setCount(item6.getCount()-5);

		orderRepository.save(order1);
		orderedItemRepository.saveAllAndFlush(Arrays.asList(oi1, oi2, oi3));
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		orderRepository.saveAndFlush(order1);
		
		//---------------------------------------
		Order order2 = new Order();
		order2.setCustomer(customer2);
		
		//order items
		OrderedItem oi4 = new OrderedItem();
		OrderedItem oi5 = new OrderedItem();
		OrderedItem oi6 = new OrderedItem();
		oi4.setStock(item4);
		oi4.setOrder(order2);
		oi4.setAmount(100);
		oi5.setStock(item5);
		oi5.setOrder(order2);
		oi5.setAmount(24);
		oi6.setStock(item6);
		oi6.setOrder(order2);
		oi6.setAmount(11);
		order2.setOrdered_items(Arrays.asList(oi4, oi5, oi6));
		
		//change counts in stock table
		item4.setCount(item4.getCount()-100);
		item5.setCount(item5.getCount()-24);
		item6.setCount(item6.getCount()-15);
		
		orderRepository.save(order2);
		orderedItemRepository.saveAllAndFlush(Arrays.asList(oi4, oi5, oi6));
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		orderRepository.saveAndFlush(order2);
		
		//---------------------------------------
		Order order3 = new Order();
		order3.setCustomer(customer2);
		
		//order items
		OrderedItem oi7 = new OrderedItem();
		OrderedItem oi8 = new OrderedItem();
		oi7.setStock(item2);
		oi7.setOrder(order3);
		oi7.setAmount(1);
		oi8.setStock(item3);
		oi8.setOrder(order3);
		oi8.setAmount(20);
		order3.setOrdered_items(Arrays.asList(oi7, oi8));

		//change counts in stock table
		item1.setCount(item2.getCount()-1);
		item3.setCount(item3.getCount()-20);
		
		orderRepository.save(order3);
		orderedItemRepository.saveAllAndFlush(Arrays.asList(oi7, oi8));
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		orderRepository.saveAndFlush(order3);
		
		//---------------------------------------
		Order order4 = new Order();
		order4.setCustomer(customer1);
		
		//order items
		OrderedItem oi9 = new OrderedItem();
		OrderedItem oi10 = new OrderedItem();
		OrderedItem oi11 = new OrderedItem();
		OrderedItem oi12 = new OrderedItem();
		OrderedItem oi13 = new OrderedItem();
		oi9.setStock(item1);
		oi9.setOrder(order4);
		oi9.setAmount(7);
		oi10.setStock(item2);
		oi10.setOrder(order4);
		oi10.setAmount(14);
		oi11.setStock(item3);
		oi11.setOrder(order4);
		oi11.setAmount(9);
		oi12.setStock(item4);
		oi12.setOrder(order4);
		oi12.setAmount(20);
		oi13.setStock(item5);
		oi13.setOrder(order4);
		oi13.setAmount(5);
		order4.setOrdered_items(Arrays.asList(oi9, oi10, oi11, oi12, oi13));
		
		//change counts in stock table
		item1.setCount(item1.getCount()-7);
		item2.setCount(item2.getCount()-14);
		item3.setCount(item3.getCount()-9);
		item4.setCount(item4.getCount()-20);
		item5.setCount(item5.getCount()-5);
		
		orderRepository.save(order4);
		orderedItemRepository.saveAllAndFlush(Arrays.asList(oi9, oi10, oi11, oi12, oi13));
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		orderRepository.saveAndFlush(order4);
		
	}
}