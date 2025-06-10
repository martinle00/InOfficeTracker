package com.project.InOfficeTracker.Controllers;

import com.project.InOfficeTracker.Models.PublicHoliday;
import com.project.InOfficeTracker.Services.PublicHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class PublicHolidayController {

    @Autowired
    PublicHolidayService publicHolidayService;

    @GetMapping("/publicHolidays")
    public Mono<Map<String, Object>> GetPublicHolidays() {
        return publicHolidayService.GetPublicHoliday();
    }
    @GetMapping("/test")
    public String Test() {
        return "Hello World";
    }
}
