package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.PageRedirectionDTO;
import com.alims.londontech.dto.UserAccountDTO;
import com.alims.londontech.service.UserAccountService;
import com.alims.londontech.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/userAccount")
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
        return userAccountService.getAllActiveInactiveUsers(token);
    }

    @GetMapping("/getAllActive")
    public List<UserAccountDTO> getAllActiveUsers(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return userAccountService.getAllActiveUsers(token);
    }

    @GetMapping("/getAllActiveUsersByPilot")
    public List<UserAccountDTO> getAllActiveUsersByPilot(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return userAccountService.getAllActiveUsersByPilot(token);
    }

    @GetMapping("/getAllActiveUsersByCrew")
    public List<UserAccountDTO> getAllActiveUsersByCrew(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return userAccountService.getAllActiveUsersByCrew(token);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }
}
