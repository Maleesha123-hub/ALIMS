package com.alims.londontech.controllers;

import com.alims.londontech.constants.enums.UserRoles;
import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/airline")
public class UIControllerInternal {

    private final Logger LOGGER = LoggerFactory.getLogger(UIControllerInternal.class);

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/login")
    public String userLoginPage() {
        return "systemAdminLogin";
    }

    @GetMapping("/arrangeSchedule")
    public String arrangeSchedulePage() {
        return "systemAdminDashboard/settings/arrangeSchedule";
    }

    @GetMapping("/user")
    public String userPage() {
        return "systemAdminDashboard/settings/user";
    }

    @GetMapping("/userRole")
    public String userRolePage() {
        return "systemAdminDashboard/settings/userRole";
    }

    @GetMapping("/pilot")
    public String pilotPage() {
        return "systemAdminDashboard/pilot/pilot";
    }

    @GetMapping("/flightCrew")
    public String flightCrewPage() {
        return "systemAdminDashboard/flightCrew/flightCrew";
    }

    @GetMapping("/destination")
    public String destinationPage() {
        return "systemAdminDashboard/destination/destination";
    }

    @GetMapping("/airplane")
    public String airplanePage() {
        return "systemAdminDashboard/airplane/airplane";
    }

    @GetMapping("/country")
    public String countryPage() {
        return "systemAdminDashboard/country/country";
    }

    /**
     * =========================================================
     * System Admin dashboard UI routing
     * =========================================================
     *
     * @param token
     * @return
     */
    @GetMapping("/systemAdminDashboard/{token}")
    public String dashboardPage(@PathVariable final String token) {
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            switch (UserRoles.valueOf(userAccount.getUserRole().getRoleName())) {
                case SYSTEM_ADMIN:
                    return "/systemAdminDashboard";
            }
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in UIControllerInternal -> dashboardPage()" + e);
        }
        return "accessDenied";
    }
}
