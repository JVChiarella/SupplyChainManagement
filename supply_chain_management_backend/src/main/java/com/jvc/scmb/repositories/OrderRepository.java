package com.jvc.scmb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jvc.scmb.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}

