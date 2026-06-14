package com.medislot.Medi_Slot_Backend.dto;

import lombok.Data;

@Data
public class DoctorRegisterRequest {
    private String email;
    private String password;
    private String name;
    private String specialization;
    private String experience;
    private String phone;
}