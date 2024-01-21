package com.jvc.scmb.entities;

import java.util.ArrayList;
import java.util.List;

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
	
	private Boolean active;
	
	private Boolean admin;
	
	@OneToMany(mappedBy = "employee")
	private List<Invoice> invoices = new ArrayList<>();
}
