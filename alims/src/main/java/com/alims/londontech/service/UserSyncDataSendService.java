package com.alims.londontech.service;

import com.alims.londontech.dto.UserAccountDTO;
import com.alims.londontech.dto.UserSyncSendProxyDTO;
import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.repositories.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserSyncDataSendService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserSyncDataSendService.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    /**
     * =======================================================
     * This method is responsible to feed System User and Role
     * to database against airline
     * =======================================================
     *
     * @param dto
     * @return
     */
    public String postAdminUserAndRole(UserSyncSendProxyDTO dto) {
        userRoleService.saveUpdateUserRoleByAdmin(dto.getUserRoleDTO());
        userAccountService.saveUpdateUserSyncByAdmin(dto.getUserRoleDTO(), dto.getUserAccountDTO());
        return "Admin User and Role Saved Successfully.";
    }

    /**
     * ============================================================
     * This method is responsible to get user accounts by profile
     * ============================================================
     *
     * @param token
     * @param airCode
     * @return
     */
    public List<UserAccountDTO> getAllUsersByProfileFromAirlineSystem(String token, String airCode) {
        List<UserAccountDTO> userAccountDTOS = new ArrayList<>();
        try {
            Predicate<UserAccount> filterOnAirCode = airCode.equalsIgnoreCase("NOT_SELECTED") ? u -> true :
                    u -> u.getAirCode().equalsIgnoreCase(airCode);
            userAccountDTOS = userAccountRepository.findAll().stream()
                    .filter(filterOnAirCode)
                    .map(u -> userAccountService.getUserAccountDTO(u))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in UserSyncDataSendService -> getAllUsersByProfileFromAirlineSystem() " + e);
        }
        return userAccountDTOS;
    }
}
