package com.alimsadmin.controllers;

import com.alimsadmin.constants.CommonConstants;
import com.alimsadmin.dto.AirlineBranchDTO;
import com.alimsadmin.service.AirlineBranchService;
import com.alimsadmin.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airlineBranch")
public class AirlineBranchController {

    @Autowired
    private AirlineBranchService airlineBranchService;

    @RequestMapping("/saveUpdateAirlineBranch")
    public CommonResponse saveUpdateAirlineBranch(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                                  @RequestBody AirlineBranchDTO dto) {
        token = getTrimedToken(token);
        return airlineBranchService.saveUpdateAirlineBranch(token, dto);
    }

    @GetMapping("/getAllActiveInactiveBranches")
    public List<AirlineBranchDTO> getAllActiveInactiveAirlineBranches(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return airlineBranchService.getAllActiveInactiveAirlineBranches();
    }

    @GetMapping("/getAllActive")
    public List<AirlineBranchDTO> getAllActiveBranches(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return airlineBranchService.getAllActiveBranches();
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }

}
