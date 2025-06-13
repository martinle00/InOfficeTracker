package com.project.InOfficeTracker.Models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "PublicHolidays")
public class PublicHoliday {
    private Date Date;
    private String Name;
}
