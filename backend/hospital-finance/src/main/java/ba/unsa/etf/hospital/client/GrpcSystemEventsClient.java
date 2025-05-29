package ba.unsa.etf.hospital.client;

import com.example.system_events.grpc.EventLoggerGrpc;
import com.example.system_events.grpc.EventRequest;
import com.example.system_events.grpc.EventResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcSystemEventsClient {

    @GrpcClient("system-events")
    private EventLoggerGrpc.EventLoggerBlockingStub stub;

    public void log(String actionType, String service, String resource, String status, String username) {
        System.out.println("➡️ Pozvana gRPC log() metoda");

        EventRequest request = EventRequest.newBuilder()
                .setActionType(actionType)
                .setService("hospital-finance")
                .setResource(resource)
                .setStatus(status)
                .setUsername(username)
                .build();

        EventResponse response = stub.logEvent(request);
        System.out.println("Log Response: " + response.getMessage());
    }

    // Ova metoda koristi log() i samo je alias sa drugim nazivom
    public void logEvent(String username, String actionType, String resource, String status) {
        log(actionType, "hospital-finance", resource, status, username);
    }
}
