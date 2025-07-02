package com.project.InOfficeTracker.Services;

import com.project.InOfficeTracker.Models.*;
import com.project.InOfficeTracker.Models.Month;
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
import java.util.Date;

@Service
public class CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private PublicHolidayService publicHolidayService;

    @Autowired
    private MongoTemplate mongoTemplate;

    Logger logger = LoggerFactory.getLogger(CalendarService.class);

    public MonthData GetCalendarMonthWeekdays(Month month) {
        MonthData monthData = new MonthData();
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
        monthData.OfficeDays = days;
        List<PublicHoliday> publicHolidaysList = publicHolidayService.GetPublicHolidayByMonth(month).block();
        monthData.PublicHolidays = publicHolidaysList.isEmpty() ? null : publicHolidayService.GetPublicHolidayByMonth(month).block();
        int workingDays = days.size();
        monthData.RequiredInOfficeDays = (int) Math.ceil(workingDays/2.0) - publicHolidaysList.size();

        List<CalendarData> calendarData = calendarRepository.getCalendarDataByMonth(Integer.toString(month.getValue()));
        monthData.userOfficeDays = calendarData.isEmpty() ? new ArrayList<>() : calendarData.get(0).getInOfficeDays();
        monthData.userAtHomeDays = calendarData.isEmpty() ? new ArrayList<>() : calendarData.get(0).getAtHomeDays();
        monthData.userAbsentDays = calendarData.isEmpty() ? new ArrayList<>() : calendarData.get(0).getAbsentDays();

        return monthData;
    }

public void UpsertMonthData(UpdateInOfficeDaysRequest req) {
    int month = req.getMonth().getValue();
    int year = Year.now().getValue();

    Query query = new Query(Criteria.where("month").is(Integer.toString(month))
            .and("year").is(Integer.toString(year)));

    Update update = new Update()
            .set("inOfficeDays", req.getUpdatedOfficeDays())
            .set("atHomeDays", req.getUpdatedAtHomeDays())
            .set("last_updated", new Date());

    boolean exists = mongoTemplate.exists(query, CalendarData.class);
    List<Integer> publicHolidayDays = getPublicHolidayDays(req.getMonth());

    if (!exists && !publicHolidayDays.isEmpty()) {
        update.setOnInsert("absentDays", publicHolidayDays);
    } else {
        update.push("absentDays").each(req.getUpdatedAbsentDays().toArray());
    }

    mongoTemplate.findAndModify(query, update,
        org.springframework.data.mongodb.core.FindAndModifyOptions.options().upsert(true),
        CalendarData.class);
}

private List<Integer> getPublicHolidayDays(Month month) {
    List<PublicHoliday> holidays = publicHolidayService.GetPublicHolidayByMonth(month).block();
    if (holidays == null) return Collections.emptyList();
    return holidays.stream()
            .map(h -> h.getDate().getDay())
            .toList();
}
}
