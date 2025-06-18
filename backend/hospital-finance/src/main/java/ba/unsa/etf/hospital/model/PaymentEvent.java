package ba.unsa.etf.hospital.model;

import java.io.Serializable;

public class PaymentEvent implements Serializable {
    private Long korisnikId;
    private Long terminId;
    private Double iznos;

    public PaymentEvent() {}

    public PaymentEvent(Long korisnikId, Long terminId, Double iznos) {
        this.korisnikId = korisnikId;
        this.terminId = terminId;
        this.iznos = iznos;
    }

    public Long getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Long korisnikId) {
        this.korisnikId = korisnikId;
    }

    public Long getTerminId() {
        return terminId;
    }

    public void setTerminId(Long terminId) {
        this.terminId = terminId;
    }

    public Double getIznos() {
        return iznos;
    }

    public void setIznos(Double iznos) {
        this.iznos = iznos;
    }

    @Override
    public String toString() {
        return "PaymentEvent{" +
                "korisnikId=" + korisnikId +
                ", terminId=" + terminId +
                ", iznos=" + iznos +
                '}';
    }
}
