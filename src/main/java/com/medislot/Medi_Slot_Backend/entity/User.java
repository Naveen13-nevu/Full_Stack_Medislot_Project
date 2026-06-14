package com.medislot.Medi_Slot_Backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;               // used as login identifier

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Patient-specific fields (nullable for doctors/admins)
    private String name;
    private String location;
    private String pincode;

    // Optional – keep for backward compatibility, can be null
    @Column(unique = true)
    private String username;

    public enum Role {
        PATIENT, DOCTOR, ADMIN
    }
}