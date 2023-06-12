package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.CrewDTO;
import com.alims.londontech.dto.PilotDTO;
import com.alims.londontech.service.CrewService;
import com.alims.londontech.service.PilotService;
import com.alims.londontech.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/crew")
public class CrewController {

    @Autowired
    private CrewService crewService;

    @PostMapping("/saveUpdate")
    public CommonResponse saveUpdateCrew(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                         @RequestBody CrewDTO dto) {
        token = getTrimedToken(token);
        return crewService.saveUpdateCrew(token, dto);
    }

    @GetMapping("/getAllActiveInactive")
    public List<CrewDTO> getAllActiveInactiveCrewDTOs(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return crewService.getAllActiveInactiveCrewDTOs(token);
    }

    @GetMapping("/getAllActive")
    public List<CrewDTO> getAllActiveCrewDTOs(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return crewService.getAllActiveCrewDTOs(token);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
