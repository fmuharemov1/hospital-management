package com.example.elektronski_karton_servis.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppointmentEvent {
    private Long appointmentId;
    private Long patientId;
    private String status;

    public AppointmentEvent() {
    }

    public AppointmentEvent(Long appointmentId, Long patientId, String status) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.status = status;
    }

}
