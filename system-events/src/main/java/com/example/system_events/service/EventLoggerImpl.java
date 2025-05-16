package com.example.system_events.service;

import com.example.system_events.grpc.EventLoggerGrpc;
import com.example.system_events.grpc.EventRequest;
import com.example.system_events.grpc.EventResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class EventLoggerImpl extends EventLoggerGrpc.EventLoggerImplBase {

    @Override
    public void logEvent(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        // Prikaz primljenih podataka u konzoli
        System.out.println("[SYSTEM-EVENTS] Akcija: " + request.getActionType() +
                ", Servis: " + request.getServiceName() +
                ", Resurs: " + request.getResource());

        // Odgovor
        EventResponse response = EventResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Uspješno evidentirana akcija.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
