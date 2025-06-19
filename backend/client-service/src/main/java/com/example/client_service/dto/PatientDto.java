package com.example.client_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class PatientDto {
    // konstruktori, getteri i setteri
    private Long id;
    private String fullName;
    private String email;

    public void setId(Long id) { this.id = id; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setEmail(String email) { this.email = email; }
}