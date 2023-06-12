package com.alimsadmin.service;

import com.alimsadmin.dto.UserAccountDTO;
import com.alimsadmin.dto.UserRoleDTO;
import com.alimsadmin.dto.UserSyncSendProxyDTO;
import com.alimsadmin.entities.AirlineProfile;
import com.alimsadmin.entities.UserAccount;
import com.alimsadmin.proxyClients.SyncUserDataServiceClient;
import com.alimsadmin.repositories.AirlineProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSyncDataSendService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserSyncDataSendService.class);

    @Autowired
    private AirlineProfileRepository airlineProfileRepository;

    @Autowired
    private AirlineProfileService airlineProfileService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private SyncUserDataServiceClient syncUserDataServiceClient;

    /**
     * ====================================================================================
     * This Method is responsible to send SYSTEM ADMIN DATA to respective airline system.
     * ====================================================================================
     *
     * @param token
     * @param profileId
     * @return
     */
    public String postAdminUserAndRole(String token, String profileId) {
        String returnMsg = null;
        try {
            UserSyncSendProxyDTO userProxyDTO = getAirlineProfileByProfileId(token, profileId);
            token = "Bearer " + token;
            returnMsg = syncUserDataServiceClient.postAdminUsersAndRole(token, userProxyDTO);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in UserSyncDataSendService -> postAdminUserAndRole()" + e);
        }
        return returnMsg;
    }

    /**
     * ====================================================================================
     * This Method is responsible to get profile send proxy dto by profile.
     * ====================================================================================
     *
     * @param token
     * @param profileId
     * @return
     */
    public UserSyncSendProxyDTO getAirlineProfileByProfileId(String token, String profileId) {
        UserSyncSendProxyDTO proxyDTO = syncSendProxyDTO(token, airlineProfileRepository.getProfileById(Long.parseLong(profileId)));
        return proxyDTO;
    }

    /**
     * ====================================================================================
     * This Method is responsible to get user proxy dto by profile.
     * ====================================================================================
     *
     * @param airlineProfile
     * @return
     */
    public UserSyncSendProxyDTO syncSendProxyDTO(String token, AirlineProfile airlineProfile) {
        UserAccount userAccount = userAccountService.getUserByToken(token);
        UserSyncSendProxyDTO proxyDTO = new UserSyncSendProxyDTO();
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        UserAccountDTO userAccountDTO = new UserAccountDTO();

        // User role DTO
        userRoleDTO.setId(airlineProfile.getUserAccount().getUserRole().getId().toString());
        userRoleDTO.setDescription(airlineProfile.getUserAccount().getUserRole().getDescription());
        userRoleDTO.setRoleName(airlineProfile.getUserAccount().getUserRole().getRoleName());
        userRoleDTO.setStatus(airlineProfile.getUserAccount().getUserRole().getStatus().toString());
        userRoleDTO.setAirCode(airlineProfile.getAirlineBranch().getCode());
        userRoleDTO.setAirName(airlineProfile.getAirlineBranch().getName());
        userRoleDTO.setAdminUserId(userAccount.getId().toString());

        //User account DTO
        userAccountDTO.setId(airlineProfile.getUserAccount().getId().toString());
        userAccountDTO.setPassword(airlineProfile.getUserAccount().getPassword());
        userAccountDTO.setStatus(airlineProfile.getUserAccount().getStatus());
        userAccountDTO.setUserName(airlineProfile.getUserAccount().getUsername());
        userAccountDTO.setUserRoleId(airlineProfile.getUserAccount().getUserRole().getId().toString());
        userAccountDTO.setAdminUserId(userAccount.getId().toString());
        userAccountDTO.setAirCode(airlineProfile.getAirlineBranch().getCode());

        //User ins code and ins name
        proxyDTO.setUserRoleDTO(userRoleDTO);
        proxyDTO.setUserAccountDTO(userAccountDTO);
        proxyDTO.setAirCode(airlineProfile.getAirlineBranch().getCode());
        proxyDTO.setAirName(airlineProfile.getAirlineBranch().getName());

        return proxyDTO;
    }

    /**
     * ====================================================================================
     * This Method is responsible to get SYSTEM ADMIN USERS to respective airline system.
     * ====================================================================================
     *
     * @param token
     * @param profileId
     * @return
     */
    public List<UserAccountDTO> getAllUsersByProfileFromAirlineSystem(String token, String profileId) {
        List<UserAccountDTO> userAccountDTOS = new ArrayList<>();
        try {
            String airCode = profileId.equalsIgnoreCase("NOT_SELECTED") ? "NOT_SELECTED" : airlineProfileRepository.getProfileById(Long.parseLong(profileId)).getAirlineBranch().getCode();
            token = "Bearer " + token;
            userAccountDTOS = syncUserDataServiceClient.getAllUsersByProfileFromAirlineSystem(token, airCode);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in UserSyncDataSendService -> getAllUsersByProfileFromAirlineSystem()" + e);
        }
        return userAccountDTOS;
    }

}
