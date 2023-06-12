package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.CountryAirportDTO;
import com.alims.londontech.dto.CountryDTO;
import com.alims.londontech.service.CountryAirportService;
import com.alims.londontech.service.CountryService;
import com.alims.londontech.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/countryAirport")
public class CountryAirportController {

    @Autowired
    private CountryAirportService countryAirportService;

    @Autowired
    private CountryService countryService;

    @PostMapping("/saveUpdate")
    public CommonResponse saveUpdate(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                     @RequestBody CountryAirportDTO dto) {
        token = getTrimedToken(token);
        return countryAirportService.saveUpdateCountryAirport(token, dto);
    }

    @GetMapping("/getAllActiveInactive")
    public List<CountryAirportDTO> getAllActiveInactiveAirportDTOs(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return countryAirportService.getAllActiveInactiveAirportDTOs(token);
    }

    @GetMapping("/getAllActiveAirportsByCountry/{countryId}")
    public List<CountryAirportDTO> getAllActiveAirportsByCountry(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                                                 @PathVariable String countryId) {
        token = getTrimedToken(token);
        return countryAirportService.getAllActiveAirportsByCountry(token, countryId);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
