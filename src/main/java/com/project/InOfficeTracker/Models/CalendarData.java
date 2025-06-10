package com.project.InOfficeTracker.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class CalendarData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private Date created_at;
    private Date last_updated;
    private String year;
    private String month;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "in_office_days", columnDefinition = "text[]")
    private List<String> inOfficeDays;
}
