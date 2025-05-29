package com.example.logging;

import com.example.system_events.grpc.EventLoggerGrpc;
import com.example.system_events.grpc.EventRequest;
import com.example.system_events.grpc.EventResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcSystemEventsClient {

    @GrpcClient("system-events")
    private EventLoggerGrpc.EventLoggerBlockingStub stub;

    public void log(String actionType, String serviceName, String resource, String status, String username) {
        System.out.println("➡️ Pozvana gRPC log() metoda");

        if (stub == null) {
            System.err.println("❌ gRPC stub nije inicijalizovan (null). Provjeri konfiguraciju!");
            return;
        }

        EventRequest request = EventRequest.newBuilder()
                .setActionType(actionType)
                .setServiceName(serviceName)
                .setResource(resource)
                .setStatus(status)
                .setUsername(username)
                .build();

        EventResponse response = stub.logEvent(request);
        System.out.println("✅ Log Response: " + response.getMessage());
    }
}
