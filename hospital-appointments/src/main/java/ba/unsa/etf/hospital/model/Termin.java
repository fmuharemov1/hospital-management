package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private UUID terminUuid;

    @NotNull(message = "Polje Pacijent ne smije biti prazno")
    @ManyToOne
    @JoinColumn(name = "pacijent_id")
    private Korisnik pacijent;

    @NotNull(message = "Polje Osoblje ne smije biti prazno")
    @ManyToOne
    @JoinColumn(name = "osoblje_id")
    private Korisnik osoblje;

    @OneToOne
    @JoinColumn(name = "obavijest_id")
    private Obavijest obavijest;

    @NotNull(message = "Polje status ne smije biti prazno")
    private String status;

    @NotNull(message = "Polje Datum i vrijeme ne smije biti prazno")
    private LocalDateTime datumVrijeme;

    @NotNull(message = "Polje trajanje ne smije biti prazno")
    private Integer trajanje;

    private String meet_link;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTerminUuid() {
        return terminUuid;
    }

    public void setTerminUuid(UUID terminUuid) {
        this.terminUuid = terminUuid;
    }

    public Korisnik getPacijent() {
        return pacijent;
    }

    public void setPacijent(Korisnik pacijent) {
        this.pacijent = pacijent;
    }

    public Korisnik getOsoblje() {
        return osoblje;
    }

    public void setOsoblje(Korisnik osoblje) {
        this.osoblje = osoblje;
    }

    public Obavijest getObavijest() {
        return obavijest;
    }

    public void setObavijest(Obavijest obavijest) {
        this.obavijest = obavijest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDatumVrijeme() {
        return datumVrijeme;
    }

    public void setDatumVrijeme(LocalDateTime datumVrijeme) {
        this.datumVrijeme = datumVrijeme;
    }

    public Integer getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(Integer trajanje) {
        this.trajanje = trajanje;
    }

    public String getMeet_link() {
        return meet_link;
    }

    public void setMeet_link(String meet_link) {
        this.meet_link = meet_link;
    }
}
