package com.jvc.scmb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jvc.scmb.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
