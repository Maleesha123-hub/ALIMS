package com.alimsadmin.controllers;

import com.alimsadmin.constants.CommonConstants;
import com.alimsadmin.dto.*;
import com.alimsadmin.service.UserAccountService;
import com.alimsadmin.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userAccount")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @PostMapping("/saveUpdateUser")
    public CommonResponse saveUpdateUserAccount(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                                @RequestBody UserAccountDTO dto) {
        token = getTrimedToken(token);
        return userAccountService.saveUpdateUser(token, dto);
    }

    @PostMapping("/saveUpdateToken/{userName}")
    public CommonResponse saveUpdateToken(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                          @PathVariable String userName) {
        token = getTrimedToken(token);
        return userAccountService.saveUpdateToken(token, userName);
    }

    @GetMapping("/home")
    public PageRedirectionDTO pageRedirection(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        PageRedirectionDTO pageRedirectionDTO = userAccountService.getHomePage(token);
        return pageRedirectionDTO;
    }

    @GetMapping("/getAllActiveInactive")
    public List<UserAccountDTO> getAllActiveInactiveUsers(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return userAccountService.getAllActiveInactiveUsers();
    }

    @GetMapping("/getAllActive")
    public List<UserAccountDTO> getAllActiveUsers(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return userAccountService.getAllActiveUsers();
    }

    @GetMapping("/getSystemAdminByProfile/{profileId}")
    public UserSyncDTO getSystemAdminByProfile(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                               @PathVariable String profileId) {
        token = getTrimedToken(token);
        return userAccountService.getSystemAdminByProfile(profileId);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
