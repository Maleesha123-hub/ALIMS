package com.alimsadmin.service;

import com.alimsadmin.constants.CommonValidationMessage;
import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.constants.validation.AirlineBranchValidationMessages;
import com.alimsadmin.dto.AirlineBranchDTO;
import com.alimsadmin.dto.AirlineDTO;
import com.alimsadmin.dto.YearDTO;
import com.alimsadmin.entities.AirlineBranch;
import com.alimsadmin.entities.AuditData;
import com.alimsadmin.entities.FinancialYear;
import com.alimsadmin.entities.UserAccount;
import com.alimsadmin.repositories.AirlineBranchRepository;
import com.alimsadmin.repositories.AirlineRepository;
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
public class AirlineBranchService {

    private final Logger LOGGER = LoggerFactory.getLogger(AirlineBranchService.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private AirlineBranchRepository airlineBranchRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    /**
     * ========================================================================
     * This method is responsible to save update airline branch
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateAirlineBranch(String token, AirlineBranchDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        UserAccount userAccount;
        AirlineBranch airlineBranch;
        try {
            userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateAirlineBranch(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                airlineBranch = new AirlineBranch();
                AuditData auditData = new AuditData();
                auditData.setCreatedBy(userAccount.getId());
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                airlineBranch.setAuditData(auditData);
            } else {
                airlineBranch = airlineBranchRepository.getOne(Long.parseLong(dto.getId()));
                airlineBranch.getAuditData().setUpdatedBy(userAccount.getId());
                airlineBranch.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            airlineBranch = getAirlineBranchByDto(airlineBranch, dto);
            airlineBranchRepository.save(airlineBranch);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in AirlineBranchService -> saveUpdateAirlineBranch()" + e);
        }
        return commonResponse;
    }

    /**
     * =====================================================
     * This method is responsible to validate airline branch
     * =====================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateAirlineBranch(AirlineBranchDTO dto) {
        List<String> validations = new ArrayList<>();
        if (dto.getStatus().equals(CommonStatus.NOT_SELECTED)) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        } else if (CommonValidation.stringNullValidation(dto.getName())) {
            validations.add(AirlineBranchValidationMessages.EMPTY_BRANCH_NAME);
        } else if (CommonValidation.stringNullValidation(dto.getCode())) {
            validations.add(AirlineBranchValidationMessages.EMPTY_BRANCH_CODE);
        } else if (CommonValidation.stringNullValidation(dto.getAirlineId())) {
            validations.add(AirlineBranchValidationMessages.EMPTY_AIRLINE);
        }
        if (CommonValidation.stringNullValidation(dto.getId())) {
            if (airlineBranchRepository.findAirlineBranchByName(dto.getName()) != null) {
                validations.add(AirlineBranchValidationMessages.EMPTY_BRANCH_NAME);
                return validations;
            }
            if (airlineBranchRepository.findAirlineBranchByCode(dto.getCode()) != null) {
                validations.add(AirlineBranchValidationMessages.EMPTY_BRANCH_CODE);
                return validations;
            }
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to convert airline branch dto to branch entity
     * ========================================================================
     *
     * @param dto
     * @param airlineBranch
     * @return
     */
    public AirlineBranch getAirlineBranchByDto(AirlineBranch airlineBranch, AirlineBranchDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            airlineBranch.setId(Long.parseLong(dto.getId()));
        }
        airlineBranch.setAirline(airlineRepository.getOne(Long.parseLong(dto.getAirlineId())));
        airlineBranch.setName(dto.getName());
        airlineBranch.setCode(dto.getAirlineCode() + dto.getCode());
        airlineBranch.setContactNo1(dto.getContactNo1());
        airlineBranch.setContactNo2(dto.getContactNo2());
        airlineBranch.setEmail(dto.getEmail());
        airlineBranch.setWeb(dto.getWeb());
        airlineBranch.setContactPerson(dto.getContactPerson());
        airlineBranch.setCountry(dto.getCountry());
        airlineBranch.setStatus(dto.getStatus());
        airlineBranch.setComment(dto.getComment());
        airlineBranch.setBranchType(dto.getBranchType());
        airlineBranch.setAddress(dto.getAddress());
        return airlineBranch;
    }

    /**
     * ========================================================================
     * This method is responsible to convert airline branch entity to dto
     * ========================================================================
     *
     * @param airlineBranch
     * @return
     */
    public AirlineBranchDTO getAirlineBranchDTOByEntity(AirlineBranch airlineBranch) {
        AirlineBranchDTO airlineBranchDTO = new AirlineBranchDTO();
        airlineBranchDTO.setId(airlineBranch.getId().toString());
        airlineBranchDTO.setName(airlineBranch.getName());
        airlineBranchDTO.setCode(airlineBranch.getCode());
        airlineBranchDTO.setContactNo1(airlineBranch.getContactNo1());
        airlineBranchDTO.setContactNo2(airlineBranch.getContactNo2());
        airlineBranchDTO.setEmail(airlineBranch.getEmail());
        airlineBranchDTO.setWeb(airlineBranch.getWeb());
        airlineBranchDTO.setContactPerson(airlineBranch.getContactPerson());
        airlineBranchDTO.setCountry(airlineBranch.getCountry());
        airlineBranchDTO.setStatus(airlineBranch.getStatus());
        airlineBranchDTO.setComment(airlineBranch.getComment());
        airlineBranchDTO.setAddress(airlineBranch.getAddress());
        return airlineBranchDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive airline branches
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<AirlineBranchDTO> getAllActiveInactiveAirlineBranches() {
        List<AirlineBranchDTO> airlineBranchDTOS = new ArrayList<>();
        try {
            airlineBranchDTOS = airlineBranchRepository.findAll().stream()
                    .map(arlb -> getAirlineBranchDTOByEntity(arlb))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in AirlineBranchService -> getAllActiveInactiveAirlineBranches() " + e);
        }
        return airlineBranchDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active branches
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<AirlineBranchDTO> getAllActiveBranches() {
        List<AirlineBranchDTO> airlineBranchDTOS = new ArrayList<>();
        try {
            Predicate<AirlineBranch> filterByStatus = y -> y.getStatus() == CommonStatus.ACTIVE;
            airlineBranchDTOS = airlineBranchRepository.findAll().stream()
                    .filter(filterByStatus)
                    .map(ur -> getAirlineBranchDTOByEntity(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in AirlineBranchService -> getAllActiveBranches()" + e);
        }
        return airlineBranchDTOS;
    }
}
