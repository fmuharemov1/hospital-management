package ba.unsa.etf.hospital.model;

import java.io.Serializable;

public class PaymentRollbackEvent implements Serializable {
    private Long fakturaId;

    public PaymentRollbackEvent() {}

    public PaymentRollbackEvent(Long fakturaId) {
        this.fakturaId = fakturaId;
    }

    public Long getFakturaId() {
        return fakturaId;
    }

    public void setFakturaId(Long fakturaId) {
        this.fakturaId = fakturaId;
    }

    @Override
    public String toString() {
        return "PaymentRollbackEvent{fakturaId=" + fakturaId + "}";
    }
}
