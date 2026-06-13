package com.medislot.Medi_Slot_Backend.controller;

import com.medislot.Medi_Slot_Backend.dto.SlotDTO;
import com.medislot.Medi_Slot_Backend.entity.*;
import com.medislot.Medi_Slot_Backend.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Paginated list of doctors
    @GetMapping("/doctors")
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorService.getAllDoctors(pageable);
    }

    // Slots (could also paginate, but we keep as list for simplicity)
    @GetMapping("/doctors/{doctorId}/slots")
    public List<SlotDTO> getAvailableSlots(@PathVariable Long doctorId) {
        return doctorService.getAvailableSlots(doctorId);
    }

    @GetMapping("/doctor/slots")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<SlotDTO> getMySlots(@AuthenticationPrincipal UserDetails userDetails) {
        return doctorService.getDoctorSlots(userDetails.getUsername());
    }

    @PostMapping("/doctor/slots")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Slot> addSlot(@RequestBody Slot slot,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(doctorService.addSlot(userDetails.getUsername(), slot));
    }
}