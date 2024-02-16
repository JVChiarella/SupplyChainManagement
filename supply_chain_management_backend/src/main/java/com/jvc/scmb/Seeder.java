package com.jvc.scmb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jvc.scmb.entities.Credentials;
import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;
import com.jvc.scmb.entities.Invoice;
import com.jvc.scmb.entities.Order;
import com.jvc.scmb.entities.OrderedItem;
import com.jvc.scmb.entities.Stock;
import com.jvc.scmb.repositories.CustomerRepository;
import com.jvc.scmb.repositories.EmployeeRepository;
import com.jvc.scmb.repositories.InvoiceRepository;
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
	private final InvoiceRepository invoiceRepository;
	private final EmployeeRepository employeeRepository;
	
	@Override
	public void run(String... args) throws Exception {
		//-- Stock --
		Stock item1 = new Stock();
		item1.setName("Blinds");
		item1.setDescription("42\" x 72\" Blinds");
		item1.setCount(100);
		item1.setPrice(29.99);
		item1.setActive(true);
		
		Stock item2 = new Stock();
		item2.setName("Ice Melt");
		item2.setDescription("50lbs Bag of Ice Melt");
		item2.setCount(50);
		item2.setPrice(49.99);
		item2.setActive(true);
		
		Stock item3 = new Stock();
		item3.setName("AAA Batteries");
		item3.setDescription("24 Pack of AAA Batteries");
		item3.setCount(1000);
		item3.setPrice(12.99);
		item3.setActive(true);
		
		Stock item4 = new Stock();
		item4.setName("AA Batteries");
		item4.setDescription("24 Pack of AA Batteries");
		item4.setCount(1000);
		item4.setPrice(12.99);
		item4.setActive(true);
		
		Stock item5 = new Stock();
		item5.setName("9V Batteries");
		item5.setDescription("12 Pack of 9V Batteries");
		item5.setCount(500);
		item5.setPrice(14.99);
		item5.setActive(true);
		
		Stock item6 = new Stock();
		item6.setName("Rags");
		item6.setDescription("6 Pack of Shop Rags");
		item6.setCount(1000);
		item6.setPrice(7.99);
		item6.setActive(true);
		
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));;
		
		//-- Customers --
		Customer customer1 = new Customer();
		customer1.setFirstName("Jason");
		customer1.setLastName("Chiarella");
		customer1.setAddress("666 Devil Way, Shithole, MS 69696");
		customer1.setPhoneNumber("111-111-1111");
		customer1.setActive(true);
		Credentials credentials1 = new Credentials();
		credentials1.setUsername("jcustomer");
		credentials1.setPassword("password");
		customer1.setCredentials(credentials1);
		customerRepository.saveAndFlush(customer1);
		
		Customer customer2 = new Customer();
		customer2.setFirstName("Testy");
		customer2.setLastName("Test");
		customer2.setAddress("123 Test St., Testtown, TS 10101");
		customer2.setPhoneNumber("222-222-2222");
		customer2.setActive(true);
		Credentials credentials2 = new Credentials();
		credentials2.setUsername("tcustomer");
		credentials2.setPassword("password");
		customer2.setCredentials(credentials2);
		customerRepository.saveAndFlush(customer2);
		
		//-- Employees --
		Employee employee1 = new Employee();
		employee1.setActive(true);
		employee1.setAdmin(true);
		Credentials credentials3 = new Credentials();
		credentials3.setUsername("jchiarella");
		credentials3.setPassword("jasonspassword");
		employee1.setCredentials(credentials3);
		employee1.setFirstName("Jason");
		employee1.setLastName("Chiarella");
		
		employeeRepository.saveAndFlush(employee1);
		
		Employee employee2 = new Employee();
		employee2.setActive(true);
		employee2.setAdmin(false);
		Credentials credentials4 = new Credentials();
		credentials4.setUsername("ttesty");
		credentials4.setPassword("testspassword");
		employee2.setCredentials(credentials4);
		employee2.setFirstName("Test");
		employee2.setLastName("Testy");
		
		employeeRepository.saveAndFlush(employee2);
		
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
		oi1.setActive(true);
		oi2.setStock(item3);
		oi2.setOrder(order1);
		oi2.setAmount(10);
		oi2.setActive(true);
		oi3.setStock(item6);
		oi3.setOrder(order1);
		oi3.setAmount(20);
		oi3.setActive(true);
		order1.setOrdered_items(Arrays.asList(oi1, oi2, oi3));
		
		//change counts in stock table
		item2.setCount(item2.getCount()-10);
		item3.setCount(item3.getCount()-20);
		item6.setCount(item6.getCount()-5);

		orderRepository.saveAndFlush(order1);
		orderedItemRepository.saveAllAndFlush(Arrays.asList(oi1, oi2, oi3));
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		
		//create invoice
		Invoice invoice1 = new Invoice(order1);
		invoice1.setEmployee(employee1);
		invoice1.setStatus("in progress");
		
		//assign employee to invoice
		List<Invoice> newList1 = new ArrayList<>();
		newList1 = employee1.getInvoices();
		newList1.add(invoice1);
		employee1.setInvoices(newList1);
		order1.setInvoice(invoice1);
		
		invoiceRepository.saveAndFlush(invoice1);
		employeeRepository.saveAndFlush(employee1);
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
		oi4.setActive(true);
		oi5.setStock(item5);
		oi5.setOrder(order2);
		oi5.setAmount(24);
		oi5.setActive(true);
		oi6.setStock(item6);
		oi6.setOrder(order2);
		oi6.setAmount(11);
		oi6.setActive(true);
		order2.setOrdered_items(Arrays.asList(oi4, oi5, oi6));
		
		//change counts in stock table
		item4.setCount(item4.getCount()-100);
		item5.setCount(item5.getCount()-24);
		item6.setCount(item6.getCount()-15);
		
		orderRepository.saveAndFlush(order2);
		orderedItemRepository.saveAllAndFlush(Arrays.asList(oi4, oi5, oi6));
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		
		//create invoice
		Invoice invoice2 = new Invoice(order2);
		invoice2.setEmployee(employee1);
		invoice2.setStatus("in progress");
		
		//assign employee to invoice
		List<Invoice> newList2 = new ArrayList<>();
		newList2 = employee1.getInvoices();
		newList2.add(invoice2);
		employee1.setInvoices(newList2);
		order2.setInvoice(invoice2);
		
		invoiceRepository.saveAndFlush(invoice2);
		employeeRepository.saveAndFlush(employee1);
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
		oi7.setActive(true);
		oi8.setStock(item3);
		oi8.setOrder(order3);
		oi8.setAmount(20);
		oi8.setActive(true);
		order3.setOrdered_items(Arrays.asList(oi7, oi8));

		//change counts in stock table
		item1.setCount(item2.getCount()-1);
		item3.setCount(item3.getCount()-20);
		
		orderRepository.saveAndFlush(order3);
		orderedItemRepository.saveAllAndFlush(Arrays.asList(oi7, oi8));
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		
		//create invoice
		Invoice invoice3 = new Invoice(order3);
		invoice3.setEmployee(employee1);
		invoice3.setStatus("in progress");
		
		//assign employee to invoice
		List<Invoice> newList3 = new ArrayList<>();
		newList3 = employee1.getInvoices();
		newList3.add(invoice3);
		employee1.setInvoices(newList3);
		order3.setInvoice(invoice3);
		
		invoiceRepository.saveAndFlush(invoice3);
		employeeRepository.saveAndFlush(employee1);
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
		oi9.setActive(true);
		oi10.setStock(item2);
		oi10.setOrder(order4);
		oi10.setAmount(14);
		oi10.setActive(true);
		oi11.setStock(item3);
		oi11.setOrder(order4);
		oi11.setAmount(9);
		oi11.setActive(true);
		oi12.setStock(item4);
		oi12.setOrder(order4);
		oi12.setAmount(20);
		oi12.setActive(true);
		oi13.setStock(item5);
		oi13.setOrder(order4);
		oi13.setAmount(5);
		oi13.setActive(true);
		order4.setOrdered_items(Arrays.asList(oi9, oi10, oi11, oi12, oi13));
		
		//change counts in stock table
		item1.setCount(item1.getCount()-7);
		item2.setCount(item2.getCount()-14);
		item3.setCount(item3.getCount()-9);
		item4.setCount(item4.getCount()-20);
		item5.setCount(item5.getCount()-5);
		
		orderRepository.saveAndFlush(order4);
		orderedItemRepository.saveAllAndFlush(Arrays.asList(oi9, oi10, oi11, oi12, oi13));
		stockRepository.saveAllAndFlush(Arrays.asList(item1, item2, item3, item4, item5, item6));
		
		//create invoice
		Invoice invoice4 = new Invoice(order4);
		invoice4.setEmployee(employee2);
		invoice4.setStatus("in progress");
		
		//assign employee to invoice
		List<Invoice> newList4 = new ArrayList<>();
		newList4 = employee2.getInvoices();
		newList4.add(invoice4);
		employee2.setInvoices(newList4);
		order4.setInvoice(invoice4);
		
		invoiceRepository.saveAndFlush(invoice4);
		employeeRepository.saveAndFlush(employee2);
		orderRepository.saveAndFlush(order4);
	}
}