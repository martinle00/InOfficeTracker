package com.project.InOfficeTracker.Services;

import com.project.InOfficeTracker.Models.PublicHoliday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

@Component
public class PublicHolidayService {

    @Autowired
    ParameterStoreService parameterStoreService;

    private final WebClient webClient = WebClient.create();

    public Mono<Map<String, Object>> GetPublicHoliday() {
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
