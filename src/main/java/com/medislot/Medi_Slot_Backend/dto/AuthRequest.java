package com.medislot.Medi_Slot_Backend.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}