package com.medislot.Medi_Slot_Backend.dto;

import lombok.Data;

@Data
public class PatientRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String location;
    private String pincode;
}