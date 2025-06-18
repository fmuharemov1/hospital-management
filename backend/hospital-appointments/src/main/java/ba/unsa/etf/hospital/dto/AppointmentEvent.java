package ba.unsa.etf.hospital.dto;

public class AppointmentEvent {
    private Long appointmentId;
    private Long patientId;
    private String status;

    public AppointmentEvent() {}

    public AppointmentEvent(Long appointmentId, Long patientId, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.status = status;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
