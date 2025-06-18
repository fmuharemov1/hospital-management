package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.config.RabbitMQConfig;
import ba.unsa.etf.hospital.dto.AppointmentEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQPublisher {

    private final AmqpTemplate amqpTemplate;

    public RabbitMQPublisher(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendAppointmentCreated(AppointmentEvent event) {
        amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, event);
    }
}
