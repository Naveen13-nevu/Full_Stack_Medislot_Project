package com.medislot.Medi_Slot_Backend.service;

import com.medislot.Medi_Slot_Backend.dto.AuthRequest;
import com.medislot.Medi_Slot_Backend.dto.AuthResponse;
import com.medislot.Medi_Slot_Backend.dto.RegisterRequest;
import com.medislot.Medi_Slot_Backend.entity.*;
import com.medislot.Medi_Slot_Backend.repository.*;
import com.medislot.Medi_Slot_Backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;   // ← THIS WAS MISSING
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
    private PasswordEncoder encoder;

    /**
     * Authenticate user and return JWT token with role.
     */
    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String role = authentication.getAuthorities()
                .iterator().next().getAuthority()
                .substring(5); // remove "ROLE_" prefix
        String token = jwtUtils.generateToken(request.getUsername(), role);
        return new AuthResponse(token, role);
    }

    /**
     * Register a new user.
     * If the role is DOCTOR, also create the Doctor profile.
     */
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        userRepository.save(user);

        if ("DOCTOR".equalsIgnoreCase(request.getRole())) {
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setName(request.getName());
            doctor.setSpecialization(request.getSpecialization());
            doctor.setExperience(request.getExperience());
            doctor.setPhone(request.getPhone());
            doctorRepository.save(doctor);
        }
    }

    /**
     * Delete a doctor by user ID (admin only).
     */
    @Transactional
    public void deleteDoctor(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.DOCTOR) {
            throw new RuntimeException("User is not a doctor");
        }

        // Remove associated doctor profile first
        doctorRepository.findByUserId(userId).ifPresent(doctorRepository::delete);

        // Then delete the user account
        userRepository.delete(user);
    }
}