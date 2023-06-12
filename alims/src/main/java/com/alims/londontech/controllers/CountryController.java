package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.CountryDTO;
import com.alims.londontech.service.CountryService;
import com.alims.londontech.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @PostMapping("/saveUpdate")
    public CommonResponse saveUpdate(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                     @RequestBody CountryDTO dto) {
        token = getTrimedToken(token);
        return countryService.saveUpdateCountry(token, dto);
    }

    @GetMapping("/getAllActiveInactive")
    public List<CountryDTO> getAllActiveInactiveCountryDTOs(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return countryService.getAllActiveInactiveCountryDTOs(token);
    }

    @GetMapping("/getAllActive")
    public List<CountryDTO> getAllActiveCountries(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return countryService.getAllActiveCountries(token);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
