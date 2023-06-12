package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.AirplaneDTO;
import com.alims.londontech.service.AirplaneService;
import com.alims.londontech.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/airplane")
public class AirplaneController {

    @Autowired
    private AirplaneService airplaneService;

    @PostMapping("/saveUpdate")
    public CommonResponse saveUpdateAirplane(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                             @RequestBody AirplaneDTO dto) {
        token = getTrimedToken(token);
        return airplaneService.saveUpdateAirplane(token, dto);
    }

    @GetMapping("/getAllActiveInactive")
    public List<AirplaneDTO> getAllActiveInactiveAirplanes(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return airplaneService.getAllActiveInactiveAirplanes(token);
    }

    @GetMapping("/getAllActive")
    public List<AirplaneDTO> getAllActiveAirPlanes(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return airplaneService.getAllActiveAirPlanes(token);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
