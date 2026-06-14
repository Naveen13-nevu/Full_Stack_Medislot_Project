package com.medislot.Medi_Slot_Backend.controller;

import com.medislot.Medi_Slot_Backend.dto.DoctorRegisterRequest;
import com.medislot.Medi_Slot_Backend.entity.Doctor;
import com.medislot.Medi_Slot_Backend.entity.Slot;
import com.medislot.Medi_Slot_Backend.repository.DoctorRepository;
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

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/doctors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDoctor(@RequestBody DoctorRegisterRequest request) {
        authService.registerDoctor(request);
        return ResponseEntity.ok("Doctor created successfully");
    }

    @DeleteMapping("/doctors/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        authService.deleteDoctor(doctor.getUser().getId());
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