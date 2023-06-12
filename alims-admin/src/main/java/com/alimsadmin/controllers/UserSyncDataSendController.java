package com.alimsadmin.controllers;

import com.alimsadmin.constants.CommonConstants;
import com.alimsadmin.dto.UserAccountDTO;
import com.alimsadmin.service.UserSyncDataSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dataSync")
public class UserSyncDataSendController {

    @Autowired
    private UserSyncDataSendService userSyncDataSendService;

    @PostMapping("/syncUsersAndRoles/{profileId}")
    public String syncUsersAndRoles(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                    @PathVariable String profileId) {
        token = getTrimedToken(token);
        return userSyncDataSendService.postAdminUserAndRole(token, profileId);
    }

    @GetMapping("/getAllUsersByProfileFromAirlineSystem/{profileId}")
    public List<UserAccountDTO> getAllUsersByProfileFromAirlineSystem(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                                                      @PathVariable String profileId) {
        token = getTrimedToken(token);
        return userSyncDataSendService.getAllUsersByProfileFromAirlineSystem(token, profileId);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }

}
