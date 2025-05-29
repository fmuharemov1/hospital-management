package com.example.client_service.config;

import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.function.Function;

@Configuration
public class GrpcSecurityConfig {

    @Bean
    public GrpcAuthenticationReader grpcAuthenticationReader() {
        Function<String, Authentication> dummyAuthConverter = token ->
                new UsernamePasswordAuthenticationToken("grpc-user", null, Collections.emptyList());

        return new BearerAuthenticationReader(dummyAuthConverter);
    }
}
