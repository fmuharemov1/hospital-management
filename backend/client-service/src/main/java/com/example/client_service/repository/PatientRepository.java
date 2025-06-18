package com.example.client_service.repository;

import com.example.client_service.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT DISTINCT p FROM Patient p LEFT JOIN FETCH p.invoices")
    List<Patient> findAllWithInvoices();

    @Query("SELECT p FROM Patient p JOIN p.invoices i WHERE i.id = :invoiceId")
    Patient findByInvoiceId(Long invoiceId);
}
