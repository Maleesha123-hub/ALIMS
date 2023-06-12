package com.alimsadmin.controllers;

import com.alimsadmin.constants.CommonConstants;
import com.alimsadmin.dto.AirlineDTO;
import com.alimsadmin.service.AirlineService;
import com.alimsadmin.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airlineMaster")
public class AirlineController {

    @Autowired
    private AirlineService airlineService;

    @RequestMapping("/saveUpdateAirline")
    public CommonResponse saveUpdateAirline(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                            @RequestBody AirlineDTO airlineDTO) {
        token = getTrimedToken(token);
        return airlineService.saveUpdateAirline(token, airlineDTO);
    }

    @GetMapping("/getAllActiveInactive")
    public List<AirlineDTO> getAllActiveInactiveAirlineDTOs(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return airlineService.getAllActiveInactiveAirlineDTOs();
    }

    @GetMapping("/getAllActive")
    public List<AirlineDTO> getAllActiveAirlines(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return airlineService.getAllActiveAirlines();
    }

    @GetMapping("/getById/{airlineId}")
    public AirlineDTO getAirlineById(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                     @PathVariable String airlineId) {
        token = getTrimedToken(token);
        return airlineService.getAirlineById(token, airlineId);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }

}
