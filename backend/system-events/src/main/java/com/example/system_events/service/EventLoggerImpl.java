package com.example.system_events.service;

import com.example.system_events.grpc.EventLoggerGrpc;
import com.example.system_events.grpc.EventRequest;
import com.example.system_events.grpc.EventResponse;
import com.example.system_events.model.SystemEvent;
import com.example.system_events.repository.SystemEventRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@GrpcService
public class EventLoggerImpl extends EventLoggerGrpc.EventLoggerImplBase {

    @Autowired
    private SystemEventRepository systemEventRepository;

    @Override
    public void logEvent(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        System.out.println("[SYSTEM-EVENTS] Akcija: " + request.getActionType() +
                ", Servis: " + request.getService() +
                ", Resurs: " + request.getResource());

        // Snimi u bazu
        SystemEvent event = SystemEvent.builder()
                .actionType(request.getActionType())
                .service(request.getService())
                .resource(request.getResource())
                .timestamp(LocalDateTime.now())
                .status("Uspješno") // ili request.getStatus() ako ga dodaš u .proto
                .build();

        systemEventRepository.save(event);

        EventResponse response = EventResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Evidentirano u bazi")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
