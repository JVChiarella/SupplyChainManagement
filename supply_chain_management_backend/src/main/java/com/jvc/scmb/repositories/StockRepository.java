package com.jvc.scmb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jvc.scmb.entities.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

}
