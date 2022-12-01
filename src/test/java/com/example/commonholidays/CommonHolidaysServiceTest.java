package com.example.commonholidays;

import com.example.commonholidays.domain.Holiday;
import com.example.commonholidays.dto.RawHolidayData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
public class CommonHolidaysServiceTest {

    @Spy
    CommonHolidaysService commonHolidaysService;

    @Test
    public void testGetCommonHolidays() {
        RawHolidayData[] firstCountryHolidays = {new RawHolidayData("2022-01-01", "Jaunais Gads",
                "New Year's Day", "LV", true, true, null, 0,
                List.of("Public")),
                new RawHolidayData("2022-04-15", "Lielā Piektdiena",
                        "Good Friday", "LV", false, true, null, 0,
                        List.of("Public")),
                new RawHolidayData("2022-05-01", "Darba svētki",
                        "Labour Day", "LV", true, true, null, 0,
                        List.of("Public"))};
        RawHolidayData[] secondCountryHolidays = {new RawHolidayData("2022-01-01", "New Year's Day",
                "New Year's Day", "GB", false, false, null, 0,
                List.of("Public")),
                new RawHolidayData("2022-04-15", "Good Friday",
                        "Good Friday", "GB", false, true, null, 0,
                        List.of("Public"))};

        Mockito.doAnswer(invocation -> firstCountryHolidays).when(commonHolidaysService).callApi("LV", 2022);
        Mockito.doAnswer(invocation -> secondCountryHolidays).when(commonHolidaysService).callApi("GB", 2022);

        List<Holiday> firstList = commonHolidaysService.getCommonHolidays("LV", "GB", 2022);

        Assertions.assertEquals(2, firstList.size());
        Assertions.assertEquals("2022-01-01", firstList.get(0).getDate());
        Assertions.assertEquals("New Year's Day", firstList.get(0).getName());
        Assertions.assertEquals("2022-04-15", firstList.get(1).getDate());
        Assertions.assertEquals("Good Friday", firstList.get(1).getName());
    }

    @Test
    public void testApiCallWithWrongCountryCodeInPath() {
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> commonHolidaysService.getCommonHolidays("LV", "YY", 2022));
        Assertions.assertEquals("Please enter correct data in Path!", exception.getReason());
    }

    @Test
    public void testApiCallWithWrongYearInPath() {
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> commonHolidaysService.getCommonHolidays("LV", "GB", 20222));
        Assertions.assertEquals("Please enter correct data in Path!", exception.getReason());
    }


}
