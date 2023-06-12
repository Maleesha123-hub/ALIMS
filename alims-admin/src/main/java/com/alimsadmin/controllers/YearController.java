package com.alimsadmin.controllers;

import com.alimsadmin.constants.CommonConstants;
import com.alimsadmin.dto.YearDTO;
import com.alimsadmin.service.YearService;
import com.alimsadmin.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/financialYear")
public class YearController {

    @Autowired
    private YearService yearService;

    @PostMapping("/saveUpdateYear")
    public CommonResponse saveUpdateYear(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                         @RequestBody YearDTO dto) {
        token = getTrimedToken(token);
        return yearService.saveUpdateYear(token, dto);
    }

    @GetMapping("/getAllActiveInactive")
    public List<YearDTO> getAllActiveInactiveYears(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return yearService.getAllActiveInactiveYears();
    }

    @GetMapping("/getAllActive")
    public List<YearDTO> getAllActiveYears(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return yearService.getAllActiveYears();
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
