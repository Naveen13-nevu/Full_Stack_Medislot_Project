package com.medislot.Medi_Slot_Backend.controller;

import com.medislot.Medi_Slot_Backend.dto.RegisterRequest;
import com.medislot.Medi_Slot_Backend.entity.Slot;
import com.medislot.Medi_Slot_Backend.service.AuthService;
import com.medislot.Medi_Slot_Backend.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/doctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDoctor(@RequestBody RegisterRequest request) {
        request.setRole("DOCTOR"); // force doctor role
        authService.register(request);
        return ResponseEntity.ok("Doctor created successfully");
    }

    @DeleteMapping("/doctors/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long userId) {
        authService.deleteDoctor(userId);
        return ResponseEntity.ok("Doctor deleted");
    }

    @PostMapping("/doctors/{doctorId}/slots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Slot> addSlotForDoctor(@PathVariable Long doctorId,
                                                 @RequestBody Slot slot) {
        Slot created = doctorService.addSlotForDoctor(doctorId, slot);
        return ResponseEntity.ok(created);
    }
}