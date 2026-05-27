package com.medislot.Medi_Slot_Backend.service;

import com.medislot.Medi_Slot_Backend.dto.AppointmentDTO;
import com.medislot.Medi_Slot_Backend.entity.*;
import com.medislot.Medi_Slot_Backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void bookSlot(Long slotId, String patientUsername) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
        if (slot.isBooked()) {
            throw new RuntimeException("Slot already booked");
        }
        User patient = userRepository.findByUsername(patientUsername)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        slot.setBooked(true);
        slotRepository.save(slot);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setSlot(slot);
        appointment.setBookingTime(LocalDateTime.now());
        appointmentRepository.save(appointment);
    }

    public List<AppointmentDTO> getPatientAppointments(String username) {
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());
        return appointments.stream().map(app -> new AppointmentDTO(
                app.getId(),
                app.getSlot().getDoctor().getName(),
                app.getSlot().getDoctor().getSpecialization(),
                app.getSlot().getStartTime(),
                app.getSlot().getEndTime(),
                app.getBookingTime()
        )).collect(Collectors.toList());
    }

    public List<AppointmentDTO> getDoctorAppointments(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Appointment> appointments = appointmentRepository.findBySlotDoctorId(user.getId());
        return appointments.stream().map(app -> new AppointmentDTO(
                app.getId(),
                app.getSlot().getDoctor().getName(),
                app.getSlot().getDoctor().getSpecialization(),
                app.getSlot().getStartTime(),
                app.getSlot().getEndTime(),
                app.getBookingTime()
        )).collect(Collectors.toList());
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, String patientUsername) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Verify the appointment belongs to the logged‑in patient
        if (!appointment.getPatient().getUsername().equals(patientUsername)) {
            throw new RuntimeException("You can only cancel your own appointments");
        }

        // Free the slot
        Slot slot = appointment.getSlot();
        slot.setBooked(false);
        slotRepository.save(slot);

        // Delete the appointment
        appointmentRepository.delete(appointment);
    }
}