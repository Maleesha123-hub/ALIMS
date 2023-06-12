package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.PilotDTO;
import com.alims.londontech.service.PilotService;
import com.alims.londontech.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/pilot")
public class PilotController {

    @Autowired
    private PilotService pilotService;

    @PostMapping("/saveUpdate")
    public CommonResponse saveUpdatePilot(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                          @RequestBody PilotDTO dto) {
        token = getTrimedToken(token);
        return pilotService.saveUpdatePilot(token, dto);
    }

    @GetMapping("/getAllActiveInactive")
    public List<PilotDTO> getAllActiveInactivePilotDTOs(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return pilotService.getAllActiveInactivePilotDTOs(token);
    }

    @GetMapping("/getAllActive")
    public List<PilotDTO> getAllActivePilotDTOs(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return pilotService.getAllActivePilotDTOs(token);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
