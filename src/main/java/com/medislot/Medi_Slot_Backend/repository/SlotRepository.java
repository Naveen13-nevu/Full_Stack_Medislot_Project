package com.medislot.Medi_Slot_Backend.repository;

import com.medislot.Medi_Slot_Backend.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
    List<Slot> findByDoctorIdAndIsBookedFalse(Long doctorId);
    List<Slot> findByDoctorId(Long doctorId);
    void deleteByStartTimeBeforeAndIsBookedFalse(LocalDateTime dateTime);
}