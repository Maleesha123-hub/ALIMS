package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.CountryDTO;
import com.alims.londontech.dto.PilotDTO;
import com.alims.londontech.dto.ScheduleDTO;
import com.alims.londontech.service.ScheduleService;
import com.alims.londontech.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/arrangeSchedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/saveUpdate")
    public CommonResponse saveUpdateSchedule(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                             @RequestBody ScheduleDTO dto) {
        token = getTrimedToken(token);
        return scheduleService.saveUpdateSchedule(token, dto);
    }

    @GetMapping("/getAllActiveInactive")
    public List<ScheduleDTO> getAllActiveInactiveSchedulesDTOs(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return scheduleService.getAllActiveInactiveSchedulesDTOs(token);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
