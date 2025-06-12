package com.project.InOfficeTracker.Services;

import com.project.InOfficeTracker.Models.Date;
import com.project.InOfficeTracker.Models.Month;
import com.project.InOfficeTracker.Models.PublicHoliday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PublicHolidayService {

    @Autowired
    ParameterStoreService parameterStoreService;

    private final WebClient webClient = WebClient.create();

    // Mapping function
    public Mono<List<PublicHoliday>> GetPublicHolidayByMonth(Month month) {
        return GetPublicHoliday(month)
                .map(holidays -> holidays.stream()
                        .map(this::mapToPublicHoliday)
                        .collect(Collectors.toList())
                );
    }

    public Mono<List<Map<String, Object>>> GetPublicHoliday(Month month) {
        Mono<Map<String, Object>> res = GetPublicHolidayResults();

Mono<List<Map<String, Object>>> nationalAndNswHolidays = res
    .map(response -> {
        Map<String, Object> responseMap = (Map<String, Object>) response.get("response");
        List<Map<String, Object>> holidays = (List<Map<String, Object>>) responseMap.get("holidays");
        return holidays.stream()
            .filter(holiday -> {
                List<?> typeList = (List<?>) holiday.get("type");
                boolean isNational = typeList.contains("National holiday");
                boolean isCommonState = typeList.contains("Common local holiday");
                Object statesObj = holiday.get("states");
                boolean hasNsw = statesObj instanceof List && ((List<?>) statesObj).stream()
                        .anyMatch(stateObj -> stateObj instanceof Map && "NSW".equals(((Map<?, ?>) stateObj).get("abbrev")));
                return isNational || (isCommonState && hasNsw);
            })
            .collect(Collectors.toList());
    });

        Mono<List<Map<String, Object>>> filterbyMonth = nationalAndNswHolidays
                .map(response -> response.stream()
                        .filter(h -> h.containsKey("date") &&
                                ((Map<String, Object>) h.get("date")).containsKey("datetime") &&
                                ((Map<String, Object>) ((Map<String, Object>) h.get("date")).get("datetime")).get("month") != null &&
                                ((Integer) ((Map<String, Object>) ((Map<String, Object>) h.get("date")).get("datetime")).get("month")) == month.getValue())
                        .collect(Collectors.toList())
                );

        return filterbyMonth;
    }

    // Helper to convert Map to PublicHoliday
    private PublicHoliday mapToPublicHoliday(Map<String, Object> holidayMap) {
        String name = (String) holidayMap.get("name");
        String description = (String) holidayMap.get("description");
        // Extract other fields as needed, including nested maps
        // Example for date:
        Map<String, Object> dateMap = (Map<String, Object>) holidayMap.get("date");
        Map<String, Object> datetimeMap = (Map<String, Object>) dateMap.get("datetime");
        int year = (Integer) datetimeMap.get("year");
        int month = (Integer) datetimeMap.get("month");
        int day = (Integer) datetimeMap.get("day");
        // Construct your PublicHoliday object
        PublicHoliday newPublicHoliday = new PublicHoliday();
        Date newDate = new Date();
        newDate.setYear(year);
        newDate.setMonth(month);
        newDate.setDay(day);
        newPublicHoliday.setDate(newDate);
        newPublicHoliday.setName(name);
        return newPublicHoliday;
    }

    private Mono<Map<String, Object>> GetPublicHolidayResults() {
        String baseURL = "https://calendarific.com/api/v2/holidays?&api_key=";
        String apiKey = parameterStoreService.getSecret("PublicHolidayKey");
        String year = Integer.toString(LocalDate.now().getYear());
        String finalUrl = baseURL + apiKey + "&country=AU&year=" + year;

        return webClient
                .get()
                .uri(finalUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
