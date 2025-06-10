package com.project.InOfficeTracker.Repositories;

import com.project.InOfficeTracker.Models.CalendarData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface CalendarRepository extends JpaRepository<CalendarData, Integer> {

    public List<CalendarData> getCalendarDataByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("UPDATE CalendarData c SET c.inOfficeDays = :updatedOfficeDays WHERE c.userId = :userId")
    public void saveInOfficeDaysByUserId(UUID userId, List<String> updatedOfficeDays);
}
