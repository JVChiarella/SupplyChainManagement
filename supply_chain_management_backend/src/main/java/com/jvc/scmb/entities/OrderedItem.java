package com.jvc.scmb.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ordered_items_table")
@Data
@NoArgsConstructor
public class OrderedItem {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn
	private Order order;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn
	private Stock stock;
	
	private int amount;
}