package com.example.client_service.dto;

// Dodaj u client-service/dto/
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    // getters/setters
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    // ... ostali getters/setters
}