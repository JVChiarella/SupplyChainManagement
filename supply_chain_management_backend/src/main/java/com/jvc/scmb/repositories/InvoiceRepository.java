package com.jvc.scmb.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jvc.scmb.entities.Invoice;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
	Optional<List<Invoice>> findByOrderCustomerId(Long id);

	Optional<List<Invoice>> findByEmployeeId(Long id);
}
