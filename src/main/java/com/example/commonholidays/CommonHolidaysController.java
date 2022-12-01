package com.example.commonholidays;

import com.example.commonholidays.domain.Holiday;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CommonHolidaysController {

    private final CommonHolidaysService holidaysService;

    public CommonHolidaysController(CommonHolidaysService holidaysService) {
        this.holidaysService = holidaysService;
    }

    @ApiOperation("Get common holidays for two countries")
    @GetMapping("/myperfectapp/{firstCountry}/{secondCountry}/{year}")
    public ResponseEntity<List<Holiday>> getCommonHolidays(@PathVariable String firstCountry,
                                                           @PathVariable String secondCountry,
                                                           @PathVariable int year) {
        return new ResponseEntity<>(holidaysService.getCommonHolidays(firstCountry,
                secondCountry, year), HttpStatus.OK);
    }


}
