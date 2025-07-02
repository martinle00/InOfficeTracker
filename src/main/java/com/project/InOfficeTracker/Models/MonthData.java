package com.project.InOfficeTracker.Models;

import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class MonthData {
    public ArrayList<Weekday> OfficeDays;
    public int RequiredInOfficeDays;
    @Nullable
    public List<PublicHoliday> PublicHolidays;
    public List<Integer> userOfficeDays;
    public List<Integer> userAtHomeDays;
    public List<Integer> userAbsentDays;
}
