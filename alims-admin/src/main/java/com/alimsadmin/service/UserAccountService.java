package com.alimsadmin.service;

import com.alimsadmin.constants.CommonValidationMessage;
import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.constants.enums.UserRoles;
import com.alimsadmin.constants.validation.UserAccountValidationMessages;
import com.alimsadmin.constants.validation.UserRoleValidationMessages;
import com.alimsadmin.dto.*;
import com.alimsadmin.entities.*;
import com.alimsadmin.repositories.AirlineProfileRepository;
import com.alimsadmin.repositories.UserAccountRepository;
import com.alimsadmin.repositories.UserRoleRepository;
import com.alimsadmin.utils.CommonResponse;
import com.alimsadmin.utils.CommonUtils;
import com.alimsadmin.utils.CommonValidation;
import com.alimsadmin.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserAccountService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserAccountService.class);

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AirlineProfileRepository airlineProfileRepository;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * ========================================================================
     * This method is responsible save and update user details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateUser(String token, @RequestBody UserAccountDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        UserAccount userAccount;
        try {
            userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateUser(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                userAccount = new UserAccount();
                AuditData auditData = new AuditData();
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                auditData.setCreatedBy(userAccount.getId());
                userAccount.setAuditData(auditData);
                userAccount.setPassword(passwordEncoder.encode(dto.getPassword()));
            } else {
                userAccount = userAccountService.findAccountById(Long.parseLong(dto.getId()));
                userAccount.getAuditData().setUpdatedBy(userAccount.getId());
                userAccount.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            userAccount = getUserAccountEntity(userAccount, dto);
            userAccountRepository.save(userAccount);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in UserAccountService -> saveUpdateUser()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================
     * This method is responsible to validate user
     * ==================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateUser(UserAccountDTO dto) {
        List<String> validations = new ArrayList<>();
        if (CommonValidation.stringNullValidation(dto.getId())) {
            if (CommonValidation.stringNullValidation(dto.getPassword())) {
                validations.add(UserAccountValidationMessages.EMPTY_PASSWORD);
                return validations;
            }
            if (userAccountRepository.findByName(dto.getUserName()) != null) {
                validations.add(UserAccountValidationMessages.EXIT_USER);
                return validations;
            }
        }
        if (CommonValidation.stringNullValidation(dto.getUserName())) {
            validations.add(UserAccountValidationMessages.EMPTY_USER_NAME);
        } else if (dto.getStatus().equals(CommonStatus.NOT_SELECTED)) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        } else if (dto.getUserRoleId().equalsIgnoreCase(String.valueOf(CommonStatus.NOT_SELECTED))) {
            validations.add(UserAccountValidationMessages.EMPTY_USER_ROLE);
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to convert user dto to user entity
     * ========================================================================
     *
     * @param dto
     * @param userAccount
     * @return
     */
    private UserAccount getUserAccountEntity(UserAccount userAccount, UserAccountDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            userAccount.setId(Long.parseLong(dto.getId()));
            userAccount.setPassword(dto.getPassword());
        }
        userAccount.setUsername(dto.getUserName());
        userAccount.setStatus(dto.getStatus());
        userAccount.setUserRole(userRoleRepository.getOne(Long.parseLong(dto.getUserRoleId())));
        return userAccount;
    }

    /**
     * ========================================================================
     * This method is responsible save and update user token.
     * ========================================================================
     *
     * @param token
     * @param userName
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateToken(String token, String userName) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            UserAccount userAccount = new UserAccount();
            userAccount = userAccountRepository.findByName(userName);
            if (CommonValidation.stringNullValidation(userAccount.getId().toString())) {
                commonResponse.setStatus(false);
                return commonResponse;
            }
            userAccount.setToken(token);
            userAccount.setLastAccessTime(DateTimeUtil.getSriLankaTime());
            userAccountRepository.save(userAccount);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in UserAccountService -> saveUpdateToken()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible to convert userAccount entity to dto.
     * ========================================================================
     *
     * @param userAccount
     * @return
     */
    public UserAccountDTO getUserAccountDTO(UserAccount userAccount) {
        UserAccountDTO userAccountDTO = new UserAccountDTO();
        userAccountDTO.setPassword(userAccount.getPassword());
        userAccountDTO.setId(userAccount.getId().toString());
        userAccountDTO.setUserName(userAccount.getUsername());
        userAccountDTO.setStatus(userAccount.getStatus());
        userAccountDTO.setUserRoleId(userAccount.getUserRole().getId().toString());
        return userAccountDTO;
    }

    /**
     * ===================================================================
     * This method is responsible to identify the relevant home page
     * according to the user role.
     * ===================================================================
     *
     * @return
     */
    public PageRedirectionDTO getHomePage(String token) {
        UserAccount userAccount = getUserByToken(token);
        String homePage = null;

        if (userAccount.getUserRole().getRoleName().equalsIgnoreCase(String.valueOf(UserRoles.SUPER_ADMIN))) {
            homePage = "superAdminDashboard";
        } else {
            homePage = "No Permission to redirect!";
        }
        PageRedirectionDTO pageRedirectionDTO = new PageRedirectionDTO(
                homePage, userAccount.getUsername(), userAccount.getToken(), userAccount.getUserRole().getRoleName());
        return pageRedirectionDTO;
    }

    /**
     * ========================================================================
     * This method is responsible get user account by token
     * ========================================================================
     *
     * @param token
     * @return
     */
    public UserAccount getUserByToken(String token) {
        return userAccountRepository.findAccountByToken(token);
    }

    /**
     * ========================================================================
     * This method is responsible get user account by id
     * ========================================================================
     *
     * @param id
     * @return
     */
    public UserAccount findAccountById(Long id) {
        return userAccountRepository.findAccountById(id);
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive users
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<UserAccountDTO> getAllActiveInactiveUsers() {
        List<UserAccountDTO> userAccountDTOS = new ArrayList<>();
        try {
            userAccountDTOS = userAccountRepository.findAll().stream()
                    .map(ur -> getUserAccountDTO(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in UserAccountService -> getAllActiveInactiveUsers() " + e);
        }
        return userAccountDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active users
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<UserAccountDTO> getAllActiveUsers() {
        List<UserAccountDTO> userAccountDTOS = new ArrayList<>();
        try {
            Predicate<UserAccount> filterByStatus = u -> u.getStatus() == CommonStatus.ACTIVE;
            Predicate<UserAccount> filterOnRole = u -> u.getUserRole().getRoleName().equalsIgnoreCase("SYSTEM_ADMIN");
            userAccountDTOS = userAccountRepository.findAll().stream()
                    .filter(filterByStatus.and(filterOnRole))
                    .map(u -> getUserAccountDTO(u))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in UserAccountService -> getAllActiveUsers()" + e);
        }
        return userAccountDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get sync dto by profile
     * ========================================================================
     *
     * @return
     */
    public UserSyncDTO getUserSyncDTO(UserAccount userAccount) {
        UserSyncDTO userSyncDTO = new UserSyncDTO();
        userSyncDTO.setUserName(userAccount.getUsername());
        userSyncDTO.setUserRole(userAccount.getUserRole().getRoleName());
        userSyncDTO.setUserStatus(userAccount.getStatus().toString());
        userSyncDTO.setUserRoleStatus(userAccount.getUserRole().getStatus().toString());
        return userSyncDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get account by profile
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public UserSyncDTO getSystemAdminByProfile(String profileId) {
        UserSyncDTO userSyncDTO = new UserSyncDTO();
        try {
            if (!profileId.equalsIgnoreCase(String.valueOf(CommonStatus.NOT_SELECTED))) {
                AirlineProfile airlineProfile = airlineProfileRepository.getOne(Long.parseLong(profileId));
                userSyncDTO = getUserSyncDTO(airlineProfile.getUserAccount());
            }
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in UserAccountService -> getSystemAdminByProfile()" + e);
        }
        return userSyncDTO;
    }
}
