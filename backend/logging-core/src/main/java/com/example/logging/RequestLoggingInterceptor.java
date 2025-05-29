package com.example.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final GrpcSystemEventsClient grpcLogger;

    public RequestLoggingInterceptor(GrpcSystemEventsClient grpcLogger) {
        this.grpcLogger = grpcLogger;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String user = "anonymous";
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            user = "jwt-user"; // ili parsiraj pravi korisnik iz tokena
        }

        grpcLogger.log(
                request.getMethod(),      // actionType
                "gateway",                // serviceName (ili EMR, ili Appointments, itd.)
                request.getRequestURI(),  // resource
                "OK",                     // status
                user                      // username
        );

        System.out.println(">>> Interceptor: " + request.getMethod() + " " + request.getRequestURI());
        return true;
    }
}
