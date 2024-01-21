package com.jvc.scmb.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice_table")
@NoArgsConstructor
@Data
public class Invoice {
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne(cascade = CascadeType.MERGE)
	private Order order;
	
	private double totalPrice = 0.0;
	
	@ManyToOne
	private Employee employee;
	
	//unfulfilled / in-progress / fulfilled
	private String status;
	
	//custom constructor to calculate total price
	public Invoice(Order order) {
		this.order = order;
		this.status = "unfulfilled";
		this.employee = null;
		for(OrderedItem item : order.getOrdered_items()) {
			this.totalPrice += item.getAmount() * item.getStock().getPrice();
		}
		this.totalPrice = round(this.totalPrice, 2);
	}
	
	//function to round a double to specified number of decimal places
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
