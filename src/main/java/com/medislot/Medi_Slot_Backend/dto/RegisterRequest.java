package com.medislot.Medi_Slot_Backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role;       // "PATIENT" or "DOCTOR"
    private String name;       // for doctor
    private String specialization;
    private String experience;
    private String phone;
}