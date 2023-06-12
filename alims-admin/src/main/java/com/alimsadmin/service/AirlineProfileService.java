package com.alimsadmin.service;

import com.alimsadmin.constants.CommonValidationMessage;
import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.constants.validation.AirlineProfileValidationMessage;
import com.alimsadmin.constants.validation.UserRoleValidationMessages;
import com.alimsadmin.dto.AirlineProfileDTO;
import com.alimsadmin.dto.UserAccountDTO;
import com.alimsadmin.dto.UserRoleDTO;
import com.alimsadmin.dto.YearDTO;
import com.alimsadmin.entities.*;
import com.alimsadmin.repositories.AirlineBranchRepository;
import com.alimsadmin.repositories.AirlineProfileRepository;
import com.alimsadmin.repositories.YearRepository;
import com.alimsadmin.utils.CommonResponse;
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
public class AirlineProfileService {

    private final Logger LOGGER = LoggerFactory.getLogger(AirlineProfileService.class);

    @Autowired
    private AirlineProfileRepository airlineProfileRepository;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private YearRepository yearRepository;

    @Autowired
    private AirlineBranchRepository airlineBranchRepository;

    /**
     * ========================================================================
     * This method is responsible save and update airline profile details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateProfile(String token, AirlineProfileDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        AirlineProfile airlineProfile;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateProfile(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                airlineProfile = new AirlineProfile();
                AuditData auditData = new AuditData();
                auditData.setCreatedBy(userAccount.getId());
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                airlineProfile.setAuditData(auditData);
            } else {
                airlineProfile = airlineProfileRepository.getOne(Long.parseLong(dto.getId()));
                airlineProfile.getAuditData().setUpdatedBy(userAccount.getId());
                airlineProfile.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            airlineProfile = getAirlineProfileEntityByDTO(airlineProfile, dto);
            airlineProfileRepository.save(airlineProfile);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in AirlineProfileService -> saveUpdateProfile()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible to convert airline dto to entity.
     * ========================================================================
     *
     * @param airlineProfile
     * @param dto
     * @return
     */
    public AirlineProfile getAirlineProfileEntityByDTO(AirlineProfile airlineProfile, AirlineProfileDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            airlineProfile.setId(Long.parseLong(dto.getId()));
        }
        airlineProfile.setProfileName(dto.getProfileName());
        airlineProfile.setFinancialYear(yearRepository.getOne(Long.parseLong(dto.getFinancialYearId())));
        airlineProfile.setAirlineBranch(airlineBranchRepository.getOne(Long.parseLong(dto.getAirlineBranchId())));
        airlineProfile.setUserAccount(userAccountService.findAccountById(Long.parseLong(dto.getAirlineSystemAdminId())));
        airlineProfile.setStatus(dto.getStatus());
        return airlineProfile;
    }

    /**
     * ==================================================
     * This method is responsible to validate profile
     * ==================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateProfile(AirlineProfileDTO dto) {
        List<String> validations = new ArrayList<>();
        if (CommonValidation.stringNullValidation(dto.getId())) {
            if (airlineProfileRepository.findProfileByName(dto.getProfileName()) != null) {
                validations.add(AirlineProfileValidationMessage.PROFILE_NAME_EXISTS);
                return validations;
            }
        }
        if (CommonValidation.stringNullValidation(dto.getProfileName())) {
            validations.add(AirlineProfileValidationMessage.PROFILE_NAME_EMPTY);
        } else if (dto.getAirlineBranchId().equalsIgnoreCase(String.valueOf(CommonStatus.NOT_SELECTED))) {
            validations.add(AirlineProfileValidationMessage.BRANCH_EMPTY);
        } else if (dto.getFinancialYearId().equalsIgnoreCase(String.valueOf(CommonStatus.NOT_SELECTED))) {
            validations.add(AirlineProfileValidationMessage.YEAR_EMPTY);
        } else if (dto.getAirlineSystemAdminId().equalsIgnoreCase(String.valueOf(CommonStatus.NOT_SELECTED))) {
            validations.add(AirlineProfileValidationMessage.SYS_ADMIN_EMPTY);
        } else if (dto.getStatus().equals(CommonStatus.NOT_SELECTED)) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive profile
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<AirlineProfileDTO> getAllActiveInactiveAirlineProfiles() {
        List<AirlineProfileDTO> airlineProfileDTOS = new ArrayList<>();
        try {
            airlineProfileDTOS = airlineProfileRepository.findAll().stream()
                    .map(arp -> getAirlineProfileDTO(arp))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in AirlineProfileService -> getAllActiveInactiveAirlineProfiles() " + e);
        }
        return airlineProfileDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to convert airline entity to dto.
     * ========================================================================
     *
     * @param airlineProfile
     * @return
     */
    public AirlineProfileDTO getAirlineProfileDTO(AirlineProfile airlineProfile) {
        AirlineProfileDTO airlineProfileDTO = new AirlineProfileDTO();
        airlineProfileDTO.setId(airlineProfile.getId().toString());
        airlineProfileDTO.setProfileName(airlineProfile.getProfileName());
        airlineProfileDTO.setFinancialYear(airlineProfile.getFinancialYear().getYear());
        airlineProfileDTO.setAirlineBranch(airlineProfile.getAirlineBranch().getName());
        airlineProfileDTO.setAirlineSystemAdmin(airlineProfile.getUserAccount().getUsername());
        airlineProfileDTO.setStatus(airlineProfile.getStatus());
        return airlineProfileDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active profiles
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<AirlineProfileDTO> getAllActiveProfiles() {
        List<AirlineProfileDTO> airlineProfileDTOS = new ArrayList<>();
        try {
            Predicate<AirlineProfile> filterByStatus = y -> y.getStatus() == CommonStatus.ACTIVE;
            airlineProfileDTOS = airlineProfileRepository.findAll().stream()
                    .filter(filterByStatus)
                    .map(arp -> getAirlineProfileDTO(arp))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in AirlineProfileService -> getAllActiveProfiles()" + e);
        }
        return airlineProfileDTOS;
    }
}
