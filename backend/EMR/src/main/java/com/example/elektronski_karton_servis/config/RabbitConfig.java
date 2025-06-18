package com.example.elektronski_karton_servis.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory; // ISPRAVAN IMPORT ZA SPRING AMQP
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("hospital.exchange");
    }

    @Bean
    public Queue appointmentCreatedQ() {
        return new Queue("appointment.created");
    }

    @Bean
    public Queue emrSuccessQ() {
        return new Queue("emr.success");
    }

    @Bean
    public Queue emrFailedQ() {
        return new Queue("emr.failed");
    }

    @Bean
    public Binding bindingAppointmentCreated() {
        return BindingBuilder.bind(appointmentCreatedQ()).to(exchange()).with("appointment.created");
    }

    @Bean
    public Binding bindingEmrSuccess() {
        return BindingBuilder.bind(emrSuccessQ()).to(exchange()).with("emr.success");
    }

    @Bean
    public Binding bindingEmrFailed() {
        return BindingBuilder.bind(emrFailedQ()).to(exchange()).with("emr.failed");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) { // Ovdje je ConnectionFactory sada iz org.springframework.amqp.rabbit.connection
        // Nije potrebno castovanje jer je tip parametra ispravan
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}