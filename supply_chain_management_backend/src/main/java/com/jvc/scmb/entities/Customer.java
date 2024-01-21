package com.jvc.scmb.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "customer_table")
@Entity
@Data
@NoArgsConstructor
public class Customer {

	@Id
	@GeneratedValue
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String address;
	
	@OneToMany(mappedBy = "customer")
	private List<Order> orders = new ArrayList<>();
}
