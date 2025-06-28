package com.project.InOfficeTracker.Controllers;

import com.project.InOfficeTracker.Models.Month;
import com.project.InOfficeTracker.Models.UpdateInOfficeDaysRequest;
import com.project.InOfficeTracker.Models.Weekday;
import com.project.InOfficeTracker.Services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/calendar")
    public ArrayList<Weekday> GetCalendarMonth(@RequestParam Month month) {
        return calendarService.GetCalendarMonthWeekdays(month);
    }

    @PostMapping("/month/save")
    public void SaveMonthData(@RequestBody UpdateInOfficeDaysRequest updateInOfficeDaysRequest) {
        calendarService.SaveMonthData(updateInOfficeDaysRequest);
    }
}
