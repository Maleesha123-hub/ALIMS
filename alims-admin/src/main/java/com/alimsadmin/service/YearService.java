package com.alimsadmin.service;

import com.alimsadmin.constants.CommonValidationMessage;
import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.constants.validation.YearValidationMessage;
import com.alimsadmin.dto.UserRoleDTO;
import com.alimsadmin.dto.YearDTO;
import com.alimsadmin.entities.AuditData;
import com.alimsadmin.entities.FinancialYear;
import com.alimsadmin.entities.UserAccount;
import com.alimsadmin.entities.UserRole;
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
public class YearService {

    private final Logger LOGGER = LoggerFactory.getLogger(YearService.class);

    @Autowired
    private YearRepository yearRepository;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * =====================================================
     * This method is responsible save and update year.
     * =====================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateYear(String token, YearDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        FinancialYear financialYear;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateYear(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                financialYear = new FinancialYear();
                AuditData auditData = new AuditData();
                auditData.setCreatedBy(userAccount.getId());
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                financialYear.setAuditData(auditData);
            } else {
                financialYear = yearRepository.getOne(Long.parseLong(dto.getId()));
                financialYear.getAuditData().setUpdatedBy(userAccount.getId());
                financialYear.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            financialYear = getYearEntityByYearDTO(financialYear, dto);
            yearRepository.save(financialYear);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in YearService -> saveUpdateYear()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================
     * This method is responsible to validate year
     * ==================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateYear(YearDTO dto) {
        List<String> validations = new ArrayList<>();
        if (CommonValidation.stringNullValidation(dto.getId())) {
            if (yearRepository.findByName(dto.getYear()) != null) {
                validations.add(YearValidationMessage.EXIT_YEAR);
                return validations;
            }
        }
        if (CommonValidation.stringNullValidation(dto.getYear())) {
            validations.add(YearValidationMessage.EMPTY_YEAR);
        } else if (CommonValidation.stringNullValidation(dto.getStartDate())) {
            validations.add(YearValidationMessage.EMPTY_START_DATE);
        } else if (CommonValidation.stringNullValidation(dto.getEndDate())) {
            validations.add(YearValidationMessage.EMPTY_END_DATE);
        } else if (dto.getStatus().equals(CommonStatus.NOT_SELECTED)) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        }
        return validations;
    }

    /**
     * ====================================================
     * This method is responsible to convert dto to entity
     * ====================================================
     *
     * @param financialYear
     * @param dto
     * @return
     */
    public FinancialYear getYearEntityByYearDTO(FinancialYear financialYear, YearDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            financialYear.setId(Long.parseLong(dto.getId()));
        }
        financialYear.setYear(dto.getYear());
        financialYear.setStartDate(DateTimeUtil.getFormattedDateTime(dto.getStartDate()));
        financialYear.setEndDate(DateTimeUtil.getFormattedDateTime(dto.getEndDate()));
        financialYear.setStatus(dto.getStatus());
        return financialYear;
    }

    /**
     * ==========================================================
     * This method is responsible to convert year entity to dto
     * ==========================================================
     *
     * @param financialYear
     * @return
     */
    public FinancialYear getYearEntityByYearDTO(FinancialYear financialYear) {
        YearDTO dto = new YearDTO();
        dto.setId(financialYear.getId().toString());
        dto.setYear(financialYear.getYear());
        dto.setStartDate(DateTimeUtil.getFormattedDateTime(financialYear.getStartDate()));
        dto.setEndDate(DateTimeUtil.getFormattedDateTime(financialYear.getEndDate()));
        dto.setStatus(financialYear.getStatus());
        return financialYear;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive years
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<YearDTO> getAllActiveInactiveYears() {
        List<YearDTO> yearDTOS = new ArrayList<>();
        try {
            yearDTOS = yearRepository.findAll().stream()
                    .map(ur -> getYearDTO(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in yearService -> getAllActiveInactiveYears() " + e);
        }
        return yearDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to convert year entity to dto.
     * ========================================================================
     *
     * @param financialYear
     * @return
     */
    public YearDTO getYearDTO(FinancialYear financialYear) {
        YearDTO yearDTO = new YearDTO();
        yearDTO.setId(financialYear.getId().toString());
        yearDTO.setYear(financialYear.getYear());
        yearDTO.setStartDate(DateTimeUtil.getFormattedDate(financialYear.getStartDate()));
        yearDTO.setEndDate(DateTimeUtil.getFormattedDate(financialYear.getEndDate()));
        yearDTO.setStatus(financialYear.getStatus());
        return yearDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active years
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<YearDTO> getAllActiveYears() {
        List<YearDTO> yearDTOS = new ArrayList<>();
        try {
            Predicate<FinancialYear> filterByStatus = y -> y.getStatus() == CommonStatus.ACTIVE;
            yearDTOS = yearRepository.findAll().stream()
                    .filter(filterByStatus)
                    .map(ur -> getYearDTO(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in yearService -> getAllActiveYears()" + e);
        }
        return yearDTOS;
    }
}


