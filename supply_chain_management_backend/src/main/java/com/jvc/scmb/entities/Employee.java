package com.jvc.scmb.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_table")
@NoArgsConstructor
@Data
public class Employee {
	@Id
	@GeneratedValue
	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	@Embedded
	private Credentials credentials;
	
	private String phoneNumber;
	
	private Boolean active;
	
	private Boolean admin;
	
	@OneToMany(mappedBy = "employee")
	private List<Invoice> invoices = new ArrayList<>();
	
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
        Employee other = (Employee) obj;
        return Objects.equals(id, other.getId());
    }
}
