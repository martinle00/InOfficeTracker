package com.project.InOfficeTracker.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class UpdateInOfficeDaysRequest {
    public List<Integer> updatedOfficeDays;
    public List<Integer> updatedAtHomeDays;
    public List<Integer> updatedAbsentDays;
    public Month month;
}
