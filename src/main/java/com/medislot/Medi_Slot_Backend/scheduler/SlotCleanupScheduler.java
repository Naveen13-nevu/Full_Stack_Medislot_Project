package com.medislot.Medi_Slot_Backend.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.medislot.Medi_Slot_Backend.repository.SlotRepository;
import java.time.LocalDateTime;

@Component
public class SlotCleanupScheduler {

    @Autowired
    private SlotRepository slotRepository;

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredSlots() {
        LocalDateTime now = LocalDateTime.now();
        slotRepository.deleteByStartTimeBeforeAndIsBookedFalse(now);
        System.out.println("Expired free slots deleted.");
    }
}