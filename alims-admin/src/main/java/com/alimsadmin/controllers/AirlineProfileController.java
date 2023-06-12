package com.alimsadmin.controllers;

import com.alimsadmin.constants.CommonConstants;
import com.alimsadmin.dto.AirlineProfileDTO;
import com.alimsadmin.dto.UserAccountDTO;
import com.alimsadmin.dto.YearDTO;
import com.alimsadmin.service.AirlineProfileService;
import com.alimsadmin.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airlineProfileController")
public class AirlineProfileController {

    @Autowired
    private AirlineProfileService airlineProfileService;

    @PostMapping("/saveUpdateAirlineProfile")
    public CommonResponse saveUpdateAirlineProfile(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                                   @RequestBody AirlineProfileDTO dto) {
        token = getTrimedToken(token);
        return airlineProfileService.saveUpdateProfile(token, dto);
    }

    @GetMapping("/getAllActiveInactiveAirlineProfiles")
    public List<AirlineProfileDTO> getAllActiveInactiveAirlineProfiles(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return airlineProfileService.getAllActiveInactiveAirlineProfiles();
    }

    @GetMapping("/getAllActive")
    public List<AirlineProfileDTO> getAllActiveProfiles(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return airlineProfileService.getAllActiveProfiles();
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
