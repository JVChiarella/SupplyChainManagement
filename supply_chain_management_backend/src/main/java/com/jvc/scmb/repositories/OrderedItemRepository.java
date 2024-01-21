package com.jvc.scmb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jvc.scmb.entities.OrderedItem;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long>{

}
