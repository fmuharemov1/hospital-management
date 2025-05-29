package com.example.api_gateway;

import com.example.logging.GrpcSystemEventsClient;


import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;

@Component
@Order(1)
public class GatewayRequestLogger implements GlobalFilter {

    private final GrpcSystemEventsClient grpcLogger;

    public GatewayRequestLogger(GrpcSystemEventsClient grpcLogger) {
        this.grpcLogger = grpcLogger;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String method = exchange.getRequest().getMethod().name();

        String path = exchange.getRequest().getPath().toString();

        grpcLogger.log(
                method,
                "API-GATEWAY",
                path,
                "OK",          // možeš kasnije dodati dinamički response status
                "anonymous"    // ako nemaš korisnika – kasnije dodaješ JWT parsiranje
        );

        return chain.filter(exchange);
    }
}
