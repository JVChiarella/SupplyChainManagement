package com.jvc.scmb.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_table")
@NoArgsConstructor
@Data
public class Stock {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String description;
	
	private int count;
	
	private double price;
	
	@OneToMany(fetch = FetchType.EAGER,
			   cascade = CascadeType.MERGE,
			   mappedBy = "stock")
	private List<OrderedItem> ordered_items = new ArrayList<>();
}
