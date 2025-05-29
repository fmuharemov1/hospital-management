package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.client.GrpcSystemEventsClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final GrpcSystemEventsClient grpcLogger;

    public TestController(GrpcSystemEventsClient grpcLogger) {
        this.grpcLogger = grpcLogger;
    }

    @GetMapping("/test-log")
    public String testGrpcLog() {
        grpcLogger.logEvent(
                "test-user",
                "GET",
                "/test-log",
                "SUCCESS"
        );
        return "Sent gRPC log event from hospital-finance!";
    }
}
