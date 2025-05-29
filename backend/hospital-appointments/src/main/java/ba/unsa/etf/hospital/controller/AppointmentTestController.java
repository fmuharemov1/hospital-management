package ba.unsa.etf.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.logging.GrpcSystemEventsClient;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentTestController {

    @Autowired
    private GrpcSystemEventsClient grpcClient;

    @GetMapping("/test-log")
    public ResponseEntity<String> testLog() {
        grpcClient.log(
                "TEST",                   // actionType
                "hospital-appointments", // serviceName
                "TEST_RESOURCE",         // resource
                "USPJEŠNO",              // status
                "admin"                  // username
        );
        return ResponseEntity.ok("✅ Log sent to system-events");
    }
}
