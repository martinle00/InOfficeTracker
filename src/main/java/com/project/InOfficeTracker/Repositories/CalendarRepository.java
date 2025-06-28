package com.project.InOfficeTracker.Repositories;

import com.project.InOfficeTracker.Models.CalendarData;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

@Repository
public interface CalendarRepository extends MongoRepository<CalendarData, String> {

    @Query("{Month: '?0'}")
    public List<CalendarData> getCalendarDataByMonth(String month);
}
