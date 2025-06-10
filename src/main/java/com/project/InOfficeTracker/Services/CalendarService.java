package com.project.InOfficeTracker.Services;

import com.project.InOfficeTracker.Models.CalendarData;
import com.project.InOfficeTracker.Models.Month;
import com.project.InOfficeTracker.Models.UpdateInOfficeDaysRequest;
import com.project.InOfficeTracker.Models.Weekday;
import com.project.InOfficeTracker.Repositories.CalendarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;
    Logger logger = LoggerFactory.getLogger(CalendarService.class);

    public ArrayList<Weekday> GetCalendarMonthWeekdays(Month month) {
        ArrayList<Weekday> days = new ArrayList<>();
        List<CalendarData> dbRes = calendarRepository.getCalendarDataByUserId(UUID.fromString("cffd37f4-39c7-447f-8117-badd41c4d5e6"));
        logger.info(Integer.toString(dbRes.size()));
        int year = Year.now().getValue();
        YearMonth yearMonth = YearMonth.of(year, month.getValue());
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            DayOfWeek dow = date.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                Weekday weekday = new Weekday();
                weekday.Date = day;
                weekday.Day = dow.toString();
                days.add(weekday);
            }
        }
        return days;
    }

    public void SaveMonthData(UpdateInOfficeDaysRequest updateInOfficeDaysRequest) {
        List<CalendarData> dataList = calendarRepository.getCalendarDataByUserId(UUID.fromString(updateInOfficeDaysRequest.userId));
        dataList.get(0).setInOfficeDays(updateInOfficeDaysRequest.updatedOfficeDays);
        calendarRepository.save(dataList.get(0));
    }
}
