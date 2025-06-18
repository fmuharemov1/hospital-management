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

    @GetMapping("/test-log")
    public String testGrpcLog() {
        grpcLogger.log(
                "GET",               // actionType
                "hospital-finance", // serviceName
                "/test-log",        // resource
                "SUCCESS",          // status
                "test-user"         // username
        );
        return "Sent gRPC log event from hospital-finance!";
    }
}
