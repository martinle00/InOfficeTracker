package com.project.InOfficeTracker.Services;

import com.project.InOfficeTracker.Models.CalendarData;
import com.project.InOfficeTracker.Models.Month;
import com.project.InOfficeTracker.Models.UpdateInOfficeDaysRequest;
import com.project.InOfficeTracker.Models.Weekday;
import com.project.InOfficeTracker.Repositories.CalendarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.YearMonth;
import java.util.*;

@Service
public class CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    Logger logger = LoggerFactory.getLogger(CalendarService.class);

    public ArrayList<Weekday> GetCalendarMonthWeekdays(Month month) {
        ArrayList<Weekday> days = new ArrayList<>();
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
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("month").is(Integer.toString(updateInOfficeDaysRequest.getMonth().getValue())),
                Criteria.where("year").is(Integer.toString(Year.now().getValue())));
        Query query = new Query(criteria);
        Update update = new Update()
                .set("inOfficeDays", updateInOfficeDaysRequest.getUpdatedOfficeDays())
                .set("atHomeDays", updateInOfficeDaysRequest.getUpdatedAtHomeDays())
                .set("absentDays", updateInOfficeDaysRequest.getUpdatedAbsentDays())
                .set("last_updated", new Date());
        mongoTemplate.findAndModify(query, update, CalendarData.class);
    }
}
