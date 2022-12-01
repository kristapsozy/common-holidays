package com.example.commonholidays;

import com.example.commonholidays.dto.RawHolidayData;
import com.example.commonholidays.domain.Holiday;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CommonHolidaysService {

    private final WebClient webClient;

    public CommonHolidaysService() {
        this.webClient = WebClient.builder().baseUrl("https://date.nager.at").build();
    }

    public List<Holiday> getCommonHolidays(String firstCountryCode, String secondCountryCode, int year) {
        RawHolidayData[] firstCountryHolidays = callApi(firstCountryCode, year);
        RawHolidayData[] secondCountryHolidays = callApi(secondCountryCode, year);
        return Arrays.stream(firstCountryHolidays).
                filter(firstCountryHoliday -> Arrays.stream(secondCountryHolidays)
                        .anyMatch(secondCountryHoliday -> secondCountryHoliday.getDate()
                                .equals(firstCountryHoliday.getDate())))
                .map(rawHolidayData -> new Holiday(rawHolidayData.getName(), rawHolidayData.getDate())).toList();
    }

    public RawHolidayData[] callApi(String CountryCode, int year) {
        try {
            return webClient.get()
                    .uri("/api/v3/publicHolidays/{year}/{countryCode}", year, CountryCode)
                    .retrieve().bodyToMono(RawHolidayData[].class).block();
        } catch (WebClientResponseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter correct data in Path!");
        }
    }

}
