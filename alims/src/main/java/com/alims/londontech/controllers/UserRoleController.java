package com.alims.londontech.controllers;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.dto.UserRoleDTO;
import com.alims.londontech.service.UserRoleService;
import com.alims.londontech.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airline/userRole")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/saveUpdateUserRole")
    public CommonResponse saveUpdateUserAccount(@RequestHeader(CommonConstants.AUTH_TOKEN) String token,
                                                @RequestBody UserRoleDTO dto) {
        token = getTrimedToken(token);
        return userRoleService.saveUpdateUserRole(token, dto);
    }

    @GetMapping("/getAllActive")
    public List<UserRoleDTO> getAllActiveUserRoles(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return userRoleService.getAllActiveUserRoles(token);
    }

    @GetMapping("/getAllActiveInactive")
    public List<UserRoleDTO> getAllActiveInactiveUserRoles(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return userRoleService.getAllActiveInactiveUserRoles(token);
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }

}
