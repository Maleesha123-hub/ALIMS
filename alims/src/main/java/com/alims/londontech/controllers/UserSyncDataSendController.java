package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.UserAccountDTO;
import com.alims.londontech.dto.UserSyncSendProxyDTO;
import com.alims.londontech.service.UserSyncDataSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/dataSync")
public class UserSyncDataSendController {

    @Autowired
    private UserSyncDataSendService userSyncDataSendService;

    @PostMapping("/syncUsersAndRoles")
    public String syncUsersAndRoles(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                    @RequestBody UserSyncSendProxyDTO dto) {
        token = getTrimedToken(token);
        return userSyncDataSendService.postAdminUserAndRole(dto);
    }

    @GetMapping("/getAllUsersByProfileFromAirlineSystem/{airCode}")
    public List<UserAccountDTO> getAllUsersByProfileFromAirlineSystem(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                                                      @PathVariable String airCode) {
        token = getTrimedToken(token);
        return userSyncDataSendService.getAllUsersByProfileFromAirlineSystem(token, airCode);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }

}
