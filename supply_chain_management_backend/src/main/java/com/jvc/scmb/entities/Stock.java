package com.jvc.scmb.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	
	private boolean active;
	
	@OneToMany(fetch = FetchType.EAGER,
			   cascade = CascadeType.MERGE,
			   mappedBy = "stock")
	private List<OrderedItem> ordered_items = new ArrayList<>();
	
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
        Stock other = (Stock) obj;
        return Objects.equals(id, other.getId());
    }
}
