package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.dto.AppointmentEvent;
import com.example.elektronski_karton_servis.model.Karton;
import com.example.elektronski_karton_servis.Repository.KartonRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AppointmentEventListener {

    private final KartonRepository kartonRepository;
    private final RabbitTemplate rabbitTemplate;

    public AppointmentEventListener(KartonRepository kartonRepository, RabbitTemplate rabbitTemplate) {
        this.kartonRepository = kartonRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "appointment.created")
    public void handleAppointmentCreated(AppointmentEvent event) {
        try {
            // Kreiraj novi karton za pacijenta
            Karton karton = new Karton();
            karton.setPacijentId(event.getPatientId());
            kartonRepository.save(karton);

            // Pošalji uspješan odgovor
            event.setStatus("EMR_SUCCESS");
            rabbitTemplate.convertAndSend("hospital.exchange", "emr.success", event);
        } catch (Exception e) {
            // U slučaju greške, pošalji event za rollback
            event.setStatus("EMR_FAILED");
            rabbitTemplate.convertAndSend("hospital.exchange", "emr.failed", event);
        }
        System.out.println("📥 Primljen event za appointmentId: " + event.getAppointmentId() + ", pacijentId: " + event.getPatientId());
        System.out.println("💾 Spašavam karton za pacijenta: " + event.getPatientId());

    }
}
