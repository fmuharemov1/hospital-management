package ba.unsa.etf.hospital.controller;

import com.example.logging.GrpcSystemEventsClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final GrpcSystemEventsClient grpcLogger;

    public TestController(GrpcSystemEventsClient grpcLogger) {
        this.grpcLogger = grpcLogger;
    }

    @GetMapping("/api/test-log")
    public String testGrpcLog() {
        grpcLogger.log(
                "GET",
                "hospital-appointments",
                "/test-log",
                "Uspješno",
                "test-user"
        );
        return "Sent gRPC log event from Hospital-appointments!";
    }
}
