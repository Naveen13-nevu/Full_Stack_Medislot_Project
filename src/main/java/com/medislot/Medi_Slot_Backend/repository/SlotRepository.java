package com.medislot.Medi_Slot_Backend.repository;

import com.medislot.Medi_Slot_Backend.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findByDoctorId(Long doctorId);

    List<Slot> findByDoctorIdAndIsBookedFalse(Long doctorId);

    // For cascade delete – removes all slots of a given doctor
    @Transactional
    void deleteByDoctorId(Long doctorId);

    // For scheduler – remove expired free slots
    void deleteByStartTimeBeforeAndIsBookedFalse(LocalDateTime dateTime);
}