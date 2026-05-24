package com.medislot.Medi_Slot_Backend.repository;

import com.medislot.Medi_Slot_Backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findBySlotDoctorId(Long doctorId);
}