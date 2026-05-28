package com.medislot.Medi_Slot_Backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;               // required for all users
    private String password;
    private String name;                // patient name (or doctor display name)
    private String location;
    private String pincode;
    private String role;                // PATIENT, DOCTOR, ADMIN (admin forces DOCTOR)

    // Doctor-specific fields (when role=DOCTOR)
    private String specialization;
    private String experience;
    private String phone;
}