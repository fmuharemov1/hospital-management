package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.PaymentEvent;
import com.example.logging.GrpcSystemEventsClient; // Provjeri da je ispravan import
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentTestController {

    private final RabbitTemplate rabbitTemplate;
    private final GrpcSystemEventsClient grpcClient;

    public PaymentTestController(RabbitTemplate rabbitTemplate, GrpcSystemEventsClient grpcClient) {
        this.rabbitTemplate = rabbitTemplate;
        this.grpcClient = grpcClient;
    }

    @PostMapping("/test")
    public ResponseEntity<String> sendTestPayment() {
        PaymentEvent event = new PaymentEvent(1L, 100L, 199.99);
        rabbitTemplate.convertAndSend("hospital.exchange", "payment.created", event);
        return ResponseEntity.ok("✅ Event 'payment.created' sent!");
    }

    @GetMapping("/test-log")
    public ResponseEntity<String> testLog() {
        grpcClient.log(
                "TEST",               // actionType
                "hospital-finance",  // serviceName
                "PAYMENT_TEST",      // resource
                "USPJEŠNO",          // status
                "admin"              // username (ili iz tokena)
        );
        return ResponseEntity.ok("✅ Log sent to system-events from hospital-finance");
    }
}
