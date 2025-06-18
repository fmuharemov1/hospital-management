package com.example.api_gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain; // Vaš import
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger; // Dodajte import
import org.slf4j.LoggerFactory; // Dodajte import

@Component
@Order(1)
public class GatewayRequestLogger implements GlobalFilter {

    // Zamijenite gRPC logger standardnim Spring Boot loggerom
    private static final Logger logger = LoggerFactory.getLogger(GatewayRequestLogger.class);

    // Uklonite injektovanje GrpcSystemEventsClient-a jer ga vise ne koristimo
    // public GatewayRequestLogger(GrpcSystemEventsClient grpcLogger) {
    //     this.grpcLogger = grpcLogger;
    // }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getPath().toString();

        // Sada logirajte pomoću SLF4J
        logger.info("API-GATEWAY Request: Method={}, Path={}, Status={}, User={}",
                method,
                path,
                "OK",          // Dinamički status je teži za dohvatiti u GlobalFilteru prije responsea
                "anonymous"    // Username iz JWT tokena ce biti dohvacen preko SecurityContextRepository
        );

        // Ako zelite dohvatiti status code nakon obrade zahtjeva:
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            int statusCode = exchange.getResponse().getStatusCode() != null ?
                    exchange.getResponse().getStatusCode().value() : 0;
            // Logirajte ponovo sa status kodom
            logger.info("API-GATEWAY Response: Method={}, Path={}, Status={}, User={}",
                    method,
                    path,
                    statusCode,
                    "anonymous" // Ili dohvatiti iz SecurityContextHolder.getReactiveSecurityContext().map(SecurityContext::getAuthentication)
            );
        }));
    }
}