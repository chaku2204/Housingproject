package com.example.Backend.Repository;

import com.example.Backend.Model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Optional<Invoice> findByCustomerIdAndInvoiceNumber(Long customerId, Long invoiceNumber);
    Page<Invoice>  findAll(Specification spec, Pageable pageable);
}
