package com.medislot.Medi_Slot_Backend.service;

import com.medislot.Medi_Slot_Backend.dto.SlotDTO;
import com.medislot.Medi_Slot_Backend.entity.*;
import com.medislot.Medi_Slot_Backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    // inside DoctorService, modify the method:
    @Cacheable(value = "doctors", unless = "#result.isEmpty()")
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public List<SlotDTO> getAvailableSlots(Long doctorId) {
        List<Slot> slots = slotRepository.findByDoctorIdAndIsBookedFalse(doctorId);
        return slots.stream().map(this::toSlotDTO).collect(Collectors.toList());
    }

    public List<SlotDTO> getDoctorSlots(String email) {   // email parameter
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        List<Slot> slots = slotRepository.findByDoctorId(doctor.getId());
        return slots.stream().map(this::toSlotDTO).collect(Collectors.toList());
    }

    public Slot addSlot(String email, Slot slot) {    // email parameter
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        slot.setDoctor(doctor);
        slot.setBooked(false);
        return slotRepository.save(slot);
    }

    public Slot addSlotForDoctor(Long doctorId, Slot slot) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        slot.setDoctor(doctor);
        slot.setBooked(false);
        return slotRepository.save(slot);
    }

    private SlotDTO toSlotDTO(Slot slot) {
        return new SlotDTO(
                slot.getId(),
                slot.getDoctor().getId(),
                slot.getDoctor().getName(),
                slot.getStartTime(),
                slot.getEndTime(),
                slot.isBooked()
        );
    }
}