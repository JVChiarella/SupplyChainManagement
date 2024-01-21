package com.jvc.scmb.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_table")
@NoArgsConstructor
@Data
public class Order {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn
	private Customer customer;
	
	@OneToMany(fetch = FetchType.EAGER,
			   cascade = CascadeType.MERGE,
			   mappedBy = "order")

	private List<OrderedItem> ordered_items = new ArrayList<>();
	
	@CreatedDate
    private Timestamp date = Timestamp.valueOf(LocalDateTime.now());

}
