package com.medislot.Medi_Slot_Backend.controller;
import com.medislot.Medi_Slot_Backend.dto.AppointmentDTO;
import com.medislot.Medi_Slot_Backend.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/patient/book/{slotId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<?> bookSlot(@PathVariable Long slotId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        appointmentService.bookSlot(slotId, userDetails.getUsername());
        return ResponseEntity.ok("Slot booked successfully");
    }

    @GetMapping("/patient/appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public List<AppointmentDTO> getPatientAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        return appointmentService.getPatientAppointments(userDetails.getUsername());
    }

    @GetMapping("/doctor/appointments")
    @PreAuthorize("hasRole('DOCTOR')")
    public List<AppointmentDTO> getDoctorAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        return appointmentService.getDoctorAppointments(userDetails.getUsername());
    }
}