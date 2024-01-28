package com.jvc.scmb.entities;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
	@JsonBackReference("ordered_item->order")
	private Order order;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn
	@JsonBackReference("ordered_item->stock")
	private Stock stock;
	
	private int amount;
	
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderedItem other = (OrderedItem) obj;
        return Objects.equals(id, other.getId());
    }
}