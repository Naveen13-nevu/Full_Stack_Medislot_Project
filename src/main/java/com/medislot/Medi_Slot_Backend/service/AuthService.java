package com.medislot.Medi_Slot_Backend.service;

import com.medislot.Medi_Slot_Backend.dto.*;
import com.medislot.Medi_Slot_Backend.entity.*;
import com.medislot.Medi_Slot_Backend.repository.*;
import com.medislot.Medi_Slot_Backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PasswordEncoder encoder;

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String role = authentication.getAuthorities().iterator().next().getAuthority().substring(5);
        String token = jwtUtils.generateToken(request.getEmail(), role);
        return new AuthResponse(token, role);
    }

    // ---------- Patient registration ----------
    public void registerPatient(PatientRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(User.Role.PATIENT);
        user.setName(request.getName());
        user.setLocation(request.getLocation());
        user.setPincode(request.getPincode());
        userRepository.save(user);
    }

    // ---------- Doctor registration  ----------
    @CacheEvict(value = "doctors", allEntries = true)
    public void registerDoctor(DoctorRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(User.Role.DOCTOR);
        user.setName(request.getName());
        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setExperience(request.getExperience());
        doctor.setPhone(request.getPhone());
        doctorRepository.save(doctor);
    }

    // ---------- Delete doctor  ----------
    @CacheEvict(value = "doctors", allEntries = true)
    @Transactional
    public void deleteDoctor(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != User.Role.DOCTOR) {
            throw new RuntimeException("User is not a doctor");
        }
        // Find doctor profile
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        Long doctorId = doctor.getId();

        // 1. Delete all appointments for this doctor
        appointmentRepository.deleteBySlotDoctorId(doctorId);
        // 2. Delete all slots belonging to this doctor
        slotRepository.deleteByDoctorId(doctorId);
        // 3. Delete the doctor profile
        doctorRepository.delete(doctor);
        // 4. Delete the user account
        userRepository.delete(user);
    }
}