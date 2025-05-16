package com.example.elektronski_karton_servis;

import com.example.elektronski_karton_servis.client.GrpcSystemEventsClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcLogTestConfig {

    @Bean
    public CommandLineRunner testGrpcClient(GrpcSystemEventsClient grpcClient) {
        return args -> {
            System.out.println("🔁 Testiram gRPC klijenta ručno...");
            grpcClient.log("test", "elektronski-karton-servis", "manual-test");
        };
    }
}
