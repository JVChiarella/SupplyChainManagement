package com.jvc.scmb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jvc.scmb.entities.Customer;
import com.jvc.scmb.entities.Employee;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByCredentialsUsername(String username);

}
