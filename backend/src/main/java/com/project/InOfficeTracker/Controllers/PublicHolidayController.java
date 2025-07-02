package com.project.InOfficeTracker.Controllers;

import com.project.InOfficeTracker.Models.PublicHoliday;
import com.project.InOfficeTracker.Models.Month;
import com.project.InOfficeTracker.Services.PublicHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class PublicHolidayController {

    @Autowired
    PublicHolidayService publicHolidayService;

    @GetMapping("/publicHolidays")

    public Mono<List<PublicHoliday>> GetPublicHolidays(@RequestParam Month month) {
        return publicHolidayService.GetPublicHolidayByMonth(month);
    }
    @GetMapping("/test")
    public String Test() {
        return "Hello World";
    }
}
