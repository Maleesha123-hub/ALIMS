package com.alimsadmin.controllers;

import com.alimsadmin.constants.CommonConstants;
import com.alimsadmin.dto.UserRoleDTO;
import com.alimsadmin.service.UserRoleService;
import com.alimsadmin.utils.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userRole")
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
        return userRoleService.getAllActiveUserRoles();
    }

    @GetMapping("/getAllActiveInactive")
    public List<UserRoleDTO> getAllActiveInactiveUserRoles(@RequestHeader(CommonConstants.AUTH_TOKEN) String token) {
        token = getTrimedToken(token);
        return userRoleService.getAllActiveInactiveUserRoles();
    }

    public String getTrimedToken(String token) {
        return token.substring(7);
    }

}
