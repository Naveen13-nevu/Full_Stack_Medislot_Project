package com.medislot.Medi_Slot_Backend.controller;

import com.medislot.Medi_Slot_Backend.dto.AuthRequest;
import com.medislot.Medi_Slot_Backend.dto.AuthResponse;
import com.medislot.Medi_Slot_Backend.dto.RegisterRequest;
import com.medislot.Medi_Slot_Backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Only allow patients to self-register
        if (!"PATIENT".equalsIgnoreCase(request.getRole())) {
            return ResponseEntity.badRequest().body("Only patient registration is allowed here");
        }
        authService.register(request);
        return ResponseEntity.ok("Patient registered successfully");
    }
}