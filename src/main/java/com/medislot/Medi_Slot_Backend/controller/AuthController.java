package com.medislot.Medi_Slot_Backend.controller;

import com.medislot.Medi_Slot_Backend.dto.AuthRequest;
import com.medislot.Medi_Slot_Backend.dto.AuthResponse;
import com.medislot.Medi_Slot_Backend.dto.PatientRegisterRequest;
import com.medislot.Medi_Slot_Backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@Valid @RequestBody PatientRegisterRequest request) {
        authService.registerPatient(request);
        return ResponseEntity.ok("Patient registered successfully");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        // If we reach here, the token is valid (Spring Security filter passed)
        return ResponseEntity.ok(Map.of("valid", true));
    }
}