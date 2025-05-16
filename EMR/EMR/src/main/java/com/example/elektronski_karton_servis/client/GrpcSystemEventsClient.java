package com.example.elektronski_karton_servis.client;

import com.example.system_events.grpc.EventLoggerGrpc;
import com.example.system_events.grpc.EventRequest;
import com.example.system_events.grpc.EventResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcSystemEventsClient {

    @GrpcClient("system-events")
    private EventLoggerGrpc.EventLoggerBlockingStub stub;

    public void log(String actionType, String serviceName, String resource) {
        System.out.println("➡️ Pozvana gRPC log() metoda");
        EventRequest request = EventRequest.newBuilder()
                .setActionType(actionType)
                .setServiceName(serviceName)
                .setResource(resource)
                .build();

        EventResponse response = stub.logEvent(request);
        System.out.println("Log Response: " + response.getMessage());
    }
}
