package com.project.InOfficeTracker.Models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "Calendar")
public class CalendarData {
    public String year;
    public String month;
    public Date last_updated;
    public List<Integer> inOfficeDays;
    public List<Integer> atHomeDays;
    public List<Integer> absentDays;
}
