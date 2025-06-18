package com.example.logging;

import com.example.system_events.grpc.EventLoggerGrpc;
import com.example.system_events.grpc.EventRequest;
import com.example.system_events.grpc.EventResponse;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GrpcSystemEventsClient {

    private static final Logger logger = LoggerFactory.getLogger(GrpcSystemEventsClient.class);

    @GrpcClient("system-events")
    private EventLoggerGrpc.EventLoggerBlockingStub stub;

    public void log(String actionType, String service, String resource, String status, String username) {
        // 🚫 Spriječi logovanje iz system-events servisa samog sebe
        if ("system-events".equalsIgnoreCase(service)) {
            logger.debug("⏭️ Preskočeno logovanje unutar system-events servisa kako bi se izbjegla rekurzija.");
            return;
        }

        logger.info("➡️ Pozvana gRPC log() metoda za akciju: {}", actionType);

        if (stub == null) {
            logger.warn("❗ gRPC stub nije inicijalizovan (null). Provjeri konfiguraciju ili konekciju sa system-events servisom.");
            return;
        }

        try {
            EventRequest request = EventRequest.newBuilder()
                    .setActionType(actionType)
                    .setService(service) // ✅ ispravljeno
                    .setResource(resource)
                    .setStatus(status)
                    .setUsername(username)
                    .build();

            EventResponse response = stub.logEvent(request);
            logger.info("✅ gRPC log uspješno poslan: {}", response.getMessage());

        } catch (StatusRuntimeException e) {
            logger.error("❌ gRPC greška prilikom slanja loga: {}", e.getStatus().getDescription());
        } catch (Exception e) {
            logger.error("❌ Neočekivana greška pri gRPC logovanju: {}", e.getMessage(), e);
        }
    }
}
