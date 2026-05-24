package com.medislot.Medi_Slot_Backend.service;


import com.medislot.Medi_Slot_Backend.dto.SlotDTO;
import com.medislot.Medi_Slot_Backend.entity.Doctor;
import com.medislot.Medi_Slot_Backend.entity.Slot;
import com.medislot.Medi_Slot_Backend.entity.User;
import com.medislot.Medi_Slot_Backend.repository.DoctorRepository;
import com.medislot.Medi_Slot_Backend.repository.SlotRepository;
import com.medislot.Medi_Slot_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<SlotDTO> getAvailableSlots(Long doctorId) {
        List<Slot> slots = slotRepository.findByDoctorIdAndIsBookedFalse(doctorId);
        return slots.stream().map(slot -> new SlotDTO(
                slot.getId(),
                slot.getDoctor().getId(),
                slot.getDoctor().getName(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.isBooked()
        )).collect(Collectors.toList());
    }

    // For DOCTOR role: add a slot
    public Slot addSlot(String username, Slot slot) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        slot.setDoctor(doctor);
        slot.setBooked(false);
        return slotRepository.save(slot);
    }
}