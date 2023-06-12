package com.alims.londontech.service;

import com.alims.londontech.constants.CommonValidationMessage;
import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.constants.enums.UserRoles;
import com.alims.londontech.constants.validation.UserAccountValidationMessages;
import com.alims.londontech.dto.PageRedirectionDTO;
import com.alims.londontech.dto.UserAccountDTO;
import com.alims.londontech.dto.UserRoleDTO;
import com.alims.londontech.entities.AuditData;
import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.repositories.UserAccountRepository;
import com.alims.londontech.repositories.UserRoleRepository;
import com.alims.londontech.utils.CommonResponse;
import com.alims.londontech.utils.CommonValidation;
import com.alims.londontech.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

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
            UserAccount user = userAccountService.getUserByToken(token);
            List<String> validations = validateUser(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                userAccount = new UserAccount();
                AuditData auditData = new AuditData();
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                auditData.setCreatedBy(user.getId());
                userAccount.setAuditData(auditData);
                userAccount.setPassword(passwordEncoder.encode(dto.getPassword()));
            } else {
                userAccount = userAccountService.findAccountById(Long.parseLong(dto.getId()));
                userAccount.getAuditData().setUpdatedBy(userAccount.getId());
                userAccount.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            userAccount = getUserAccountEntity(userAccount, dto);
            userAccount.setAirCode(user.getAirCode());
            userAccount.setAdminUserId(user.getAdminUserId());
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
            UserAccount userAccount;
            userAccount = userAccountRepository.findByName(userName);
            if (CommonValidation.stringNullValidation(userAccount.getId().toString())) {
                commonResponse.setStatus(false);
                return commonResponse;
            }
            userAccount.setToken(token);
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
        userAccountDTO.setAirCode(userAccount.getAirCode());
        userAccountDTO.setAdminUserId(userAccount.getAdminUserId().toString());
        userAccountDTO.setUserRoleId(userAccount.getUserRole().getId().toString());
        userAccountDTO.setUpdatedOn(userAccount.getAuditData().getUpdatedOn() == null ? "-" : userAccount.getAuditData().getUpdatedOn().toString());
        userAccountDTO.setCreatedOn(userAccount.getAuditData().getCreatedOn() == null ? "-" : userAccount.getAuditData().getCreatedOn().toString());
        userAccountDTO.setRolName(userAccount.getUserRole().getRoleName());
        userAccountDTO.setLastAccess(userAccount.getLastAccessTime() == null ? "-" : userAccount.getLastAccessTime().toString());
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

        if (userAccount.getUserRole().getRoleName().equalsIgnoreCase(String.valueOf(UserRoles.SYSTEM_ADMIN))) {
            homePage = "systemAdminDashboard";
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
    public List<UserAccountDTO> getAllActiveInactiveUsers(String token) {
        UserAccount userAccount = userAccountService.getUserByToken(token);
        List<UserAccountDTO> userAccountDTOS = new ArrayList<>();
        try {
            Predicate<UserAccount> filterOnAirCode = u -> u.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            userAccountDTOS = userAccountRepository.findAll().stream()
                    .filter(filterOnAirCode)
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
    public List<UserAccountDTO> getAllActiveUsers(String token) {
        UserAccount userAccount = userAccountService.getUserByToken(token);
        List<UserAccountDTO> userAccountDTOS = new ArrayList<>();
        try {
            Predicate<UserAccount> filterOnAirCode = u -> u.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            Predicate<UserAccount> filterByStatus = u -> u.getStatus() == CommonStatus.ACTIVE;
            Predicate<UserAccount> filterOnRole = u -> u.getUserRole().getRoleName().equalsIgnoreCase("SYSTEM_ADMIN");
            userAccountDTOS = userAccountRepository.findAll().stream()
                    .filter(filterByStatus.and(filterOnRole).and(filterOnAirCode))
                    .map(u -> getUserAccountDTO(u))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in UserAccountService -> getAllActiveUsers()" + e);
        }
        return userAccountDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to save update user sync data
     * ========================================================================
     *
     * @param dto
     * @return
     */
    public void saveUpdateUserSyncByAdmin(UserRoleDTO roleDTO, UserAccountDTO dto) {
        UserAccount userAccount = new UserAccount();
        try {
            if (userAccountRepository.findByName(dto.getUserName()) != null) {
                userAccount.setId(userAccountRepository.findByName(dto.getUserName()).getId());
            }
            userAccountRepository.save(getUserAccountByDTO(userAccount, roleDTO, dto));
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in UserAccountService -> saveUpdateUserSyncByAdmin()" + e);
        }
    }

    /**
     * ========================================================================
     * This method is responsible to convert dto to user entity.
     * ========================================================================
     *
     * @param userAccount
     * @param roleDTO
     * @param dto
     * @return
     */
    public UserAccount getUserAccountByDTO(UserAccount userAccount, UserRoleDTO roleDTO, UserAccountDTO dto) {
        AuditData auditData = new AuditData();
        userAccount.setUsername(dto.getUserName());
        userAccount.setPassword(dto.getPassword());
        userAccount.setStatus(dto.getStatus());
        userAccount.setAdminUserId(Long.parseLong(dto.getAdminUserId()));
        userAccount.setAirCode(dto.getAirCode());
        userAccount.setUserRole(userRoleRepository.findUserRoleByUserName(roleDTO.getRoleName()));
        auditData.setCreatedBy(Long.parseLong(dto.getAdminUserId()));
        auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
        userAccount.setAuditData(auditData);
        return userAccount;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active users by pilot
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<UserAccountDTO> getAllActiveUsersByPilot(String token) {
        UserAccount userAccount = userAccountService.getUserByToken(token);
        List<UserAccountDTO> userAccountDTOS = new ArrayList<>();
        try {
            Predicate<UserAccount> filterByStatus = u -> u.getStatus() == CommonStatus.ACTIVE;
            Predicate<UserAccount> filterOnAirCode = u -> u.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            Predicate<UserAccount> filterOnRole = u -> u.getUserRole().getRoleName().equalsIgnoreCase(UserRoles.PILOT.toString());
            Predicate<UserAccount> filterOnNewUser = u -> u.getPilotId() == null;

            userAccountDTOS = userAccountRepository.findAll().stream()
                    .filter(filterByStatus.and(filterOnRole).and(filterOnNewUser).and(filterOnAirCode))
                    .map(u -> getUserAccountDTO(u))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in UserAccountService -> getAllActiveUsersByPilot()" + e);
        }
        return userAccountDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active users by crews
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<UserAccountDTO> getAllActiveUsersByCrew(String token) {
        UserAccount userAccount = userAccountService.getUserByToken(token);
        List<UserAccountDTO> userAccountDTOS = new ArrayList<>();
        try {
            Predicate<UserAccount> filterByStatus = u -> u.getStatus() == CommonStatus.ACTIVE;
            Predicate<UserAccount> filterOnAirCode = u -> u.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            Predicate<UserAccount> filterOnRole = u -> u.getUserRole().getRoleName().equalsIgnoreCase(UserRoles.CREW.toString());
            Predicate<UserAccount> filterOnNewUser = u -> u.getCrewId() == null;

            userAccountDTOS = userAccountRepository.findAll().stream()
                    .filter(filterByStatus.and(filterOnRole).and(filterOnNewUser).and(filterOnAirCode))
                    .map(u -> getUserAccountDTO(u))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in UserAccountService -> getAllActiveUsersByCrew()" + e);
        }
        return userAccountDTOS;
    }
}
