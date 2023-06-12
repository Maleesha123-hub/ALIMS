package com.alimsadmin.controllers;

import com.alimsadmin.constants.enums.UserRoles;
import com.alimsadmin.entities.UserAccount;
import com.alimsadmin.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class UIControllerInternal {

    private final Logger LOGGER = LoggerFactory.getLogger(UIControllerInternal.class);

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/login")
    public String adminLoginPage() {
        return "login";
    }

    @GetMapping("/airline")
    public String airlinePage() {
        return "superAdminDashboard/airline/airline";
    }

    @GetMapping("/userPage")
    public String userPage() {
        return "superAdminDashboard/settings/user";
    }

    @GetMapping("/userRolePage")
    public String userRolePage() {
        return "superAdminDashboard/settings/userRole";
    }

    @GetMapping("/superAdminDashboard")
    public String dashboardPage() {
        return "superAdminDashboard";
    }

    @GetMapping("/yearPage")
    public String financialYearPage() {
        return "superAdminDashboard/financialYear";
    }

    /**
     * =========================================================
     * Admin dashboard UI routing
     * =========================================================
     *
     * @param token
     * @return
     */
    @GetMapping("/superAdminDashboard/{token}")
    public String dashboardPage(@PathVariable final String token) {
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            switch (UserRoles.valueOf(userAccount.getUserRole().getRoleName())) {
                case SUPER_ADMIN:
                    return "/superAdminDashboard";
            }
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in UIControllerInternal -> dashboardPage()" + e);
        }
        return "accessDenied";
    }

}
