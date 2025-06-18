package ba.unsa.etf.hospital.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NAME = "hospital.exchange";

    // 🔁 Exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 📨 Queue definicije
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
    public Queue paymentCreatedQ() {
        return new Queue("payment.created");
    }

    @Bean
    public Queue financeSuccessQ() {
        return new Queue("finance.success");
    }

    @Bean
    public Queue financeFailedQ() {
        return new Queue("finance.failed");
    }

    // ✅ NOVO za rollback
    @Bean
    public Queue paymentRollbackQ() {
        return new Queue("payment.rollback.queue");
    }

    // 🔗 Bindingi (veze između queue i exchange preko routing key-a)
    @Bean
    public Binding appointmentCreatedBinding() {
        return BindingBuilder.bind(appointmentCreatedQ())
                .to(exchange())
                .with("appointment.created");
    }

    @Bean
    public Binding emrSuccessBinding() {
        return BindingBuilder.bind(emrSuccessQ())
                .to(exchange())
                .with("emr.success");
    }

    @Bean
    public Binding emrFailedBinding() {
        return BindingBuilder.bind(emrFailedQ())
                .to(exchange())
                .with("emr.failed");
    }

    @Bean
    public Binding paymentCreatedBinding() {
        return BindingBuilder.bind(paymentCreatedQ())
                .to(exchange())
                .with("payment.created");
    }

    @Bean
    public Binding financeSuccessBinding() {
        return BindingBuilder.bind(financeSuccessQ())
                .to(exchange())
                .with("finance.success");
    }

    @Bean
    public Binding financeFailedBinding() {
        return BindingBuilder.bind(financeFailedQ())
                .to(exchange())
                .with("finance.failed");
    }

    // ✅ Binding za rollback queue (npr. veže se na failed event)
    @Bean
    public Binding paymentRollbackBinding() {
        return BindingBuilder.bind(paymentRollbackQ())
                .to(exchange())
                .with("finance.failed"); // Ovo je routing key koji šalješ kod rollbacka
    }

    // 🛠️ JSON konverter za slanje objekata
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("ba.unsa.etf.hospital.model", "ba.unsa.etf.hospital.events"); // 🛡️ Dodaj i druge pakete ako treba
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    // 📬 RabbitTemplate koristi naš JSON konverter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
