package ba.unsa.etf.hospital.dto;

import ba.unsa.etf.hospital.model.Faktura;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class PatientInvoiceDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private List<Faktura> invoices;
}
