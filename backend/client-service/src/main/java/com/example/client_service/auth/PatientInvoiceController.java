package com.example.client_service.auth;

import com.example.client_service.model.Invoice;
import com.example.client_service.model.Patient;
import com.example.client_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PatientInvoiceController {

    private final PatientRepository patientRepository;

    @GetMapping("/patients-with-invoices")
    public ResponseEntity<List<Patient>> getPatientsWithInvoices() {
        return ResponseEntity.ok(patientRepository.findAllWithInvoices());
    }

    @PostMapping("/invoices")
    public ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice) {
        Patient patient = patientRepository.findById(invoice.getPatient().getId()
                )
                .orElseThrow(() -> new RuntimeException("Pacijent nije pronađen"));

        patient.getInvoices().add(invoice);
        patientRepository.save(patient);

        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice updatedInvoice) {
        Patient patient = patientRepository.findByInvoiceId(id);
        patient.getInvoices().removeIf(i -> i.getId().equals(id));
        patient.getInvoices().add(updatedInvoice);
        patientRepository.save(patient);
        return ResponseEntity.ok(updatedInvoice);
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        Patient patient = patientRepository.findByInvoiceId(id);
        patient.getInvoices().removeIf(i -> i.getId().equals(id));
        patientRepository.save(patient);
        return ResponseEntity.noContent().build();
    }
}
