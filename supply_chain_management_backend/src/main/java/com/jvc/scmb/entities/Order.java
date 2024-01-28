package com.jvc.scmb.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	@JsonBackReference("order->customer")
	private Customer customer;
	
	@OneToMany(fetch = FetchType.EAGER,
			   cascade = CascadeType.MERGE,
			   mappedBy = "order")
	private List<OrderedItem> ordered_items = new ArrayList<>();
	
	@CreatedDate
    private Timestamp date = Timestamp.valueOf(LocalDateTime.now());
	
	@OneToOne(mappedBy = "order")
	@JsonBackReference("order->invoice")
	private Invoice invoice;
	
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
        Order other = (Order) obj;
        return Objects.equals(id, other.getId());
    }
}
