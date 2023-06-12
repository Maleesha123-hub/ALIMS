package com.alimsadmin.service;

import com.alimsadmin.constants.CommonValidationMessage;
import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.constants.validation.UserRoleValidationMessages;
import com.alimsadmin.dto.UserRoleDTO;
import com.alimsadmin.entities.AuditData;
import com.alimsadmin.entities.UserAccount;
import com.alimsadmin.entities.UserRole;
import com.alimsadmin.repositories.UserRoleRepository;
import com.alimsadmin.utils.CommonResponse;
import com.alimsadmin.utils.CommonUtils;
import com.alimsadmin.utils.CommonValidation;
import com.alimsadmin.utils.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserRoleService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserRoleService.class);

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * ========================================================================
     * This method is responsible save and update user role details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateUserRole(String token, UserRoleDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        UserRole userRole;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateUserRole(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                userRole = new UserRole();
                AuditData auditData = new AuditData();
                auditData.setCreatedBy(userAccount.getId());
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                userRole.setAuditData(auditData);
            } else {
                userRole = userRoleRepository.getOne(Long.parseLong(dto.getId()));
                userRole.getAuditData().setUpdatedBy(userAccount.getId());
                userRole.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            userRole = getUserRoleEntityByUserRoleDto(userRole, dto);
            userRoleRepository.save(userRole);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in UserRoleService -> saveUpdateUserRole()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================
     * This method is responsible to validate user role
     * ==================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateUserRole(UserRoleDTO dto) {
        List<String> validations = new ArrayList<>();
        if (CommonValidation.stringNullValidation(dto.getId())) {
            if (userRoleRepository.findUserRoleByUserName(dto.getRoleName()) != null) {
                validations.add(UserRoleValidationMessages.EXIT_USER_ROLE);
                return validations;
            }
        }
        if (CommonValidation.stringNullValidation(dto.getRoleName())) {
            validations.add(UserRoleValidationMessages.EMPTY_USER_ROLE_NAME);
        } else if (CommonValidation.stringNullValidation(dto.getDescription())) {
            validations.add(UserRoleValidationMessages.EMPTY_USER_ROLE_DESCRIPTION);
        } else if (dto.getStatus().equalsIgnoreCase(String.valueOf(CommonStatus.NOT_SELECTED))) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to convert user role dto to user role entity
     * ========================================================================
     *
     * @param dto
     * @return
     */
    public UserRole getUserRoleEntityByUserRoleDto(UserRole userRole, UserRoleDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            userRole.setId(Long.valueOf(dto.getId()));
        }
        userRole.setRoleName(dto.getRoleName());
        userRole.setStatus(CommonStatus.valueOf(dto.getStatus()));
        userRole.setDescription(dto.getDescription());
        return userRole;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active user roles
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<UserRoleDTO> getAllActiveUserRoles() {
        List<UserRoleDTO> userRoleDTOS = new ArrayList<>();
        try {
            Predicate<UserRole> filterByStatus = ur -> ur.getStatus() == CommonStatus.ACTIVE;
            userRoleDTOS = userRoleRepository.findAll().stream()
                    .filter(filterByStatus)
                    .map(ur -> getUserRoleDTO(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in UserRoleService -> getAllActiveUserRoles()" + e);
        }
        return userRoleDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive user roles
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<UserRoleDTO> getAllActiveInactiveUserRoles() {
        List<UserRoleDTO> userRoleDTOS = new ArrayList<>();
        try {
            userRoleDTOS = userRoleRepository.findAll().stream()
                    .map(ur -> getUserRoleDTO(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in UserRoleService -> getAllActiveInactiveUserRoles() " + e);
        }
        return userRoleDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to convert role entity to role dto
     * ========================================================================
     *
     * @param userRole
     * @return
     */
    public UserRoleDTO getUserRoleDTO(UserRole userRole) {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setId(userRole.getId().toString());
        userRoleDTO.setRoleName(userRole.getRoleName());
        userRoleDTO.setDescription(userRole.getDescription());
        userRoleDTO.setStatus(String.valueOf(userRole.getStatus()));
        return userRoleDTO;
    }

}
