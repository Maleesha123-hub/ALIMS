package com.alimsadmin.service;

import com.alimsadmin.constants.enums.CommonStatus;
import com.alimsadmin.constants.validation.AirlineValidationMessage;
import com.alimsadmin.dto.AirlineDTO;
import com.alimsadmin.entities.Airline;
import com.alimsadmin.entities.AuditData;
import com.alimsadmin.entities.UserAccount;
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
import java.util.stream.Collectors;

@Service
public class AirlineService {

    private final Logger LOGGER = LoggerFactory.getLogger(AirlineService.class);

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * ========================================================================
     * This method is responsible to save update airline
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateAirline(String token, AirlineDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        UserAccount userAccount;
        Airline airline;
        try {
            userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateAirline(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                airline = new Airline();
                AuditData auditData = new AuditData();
                auditData.setCreatedBy(userAccount.getId());
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                airline.setAuditData(auditData);
            } else {
                airline = airlineRepository.getOne(Long.parseLong(dto.getId()));
                airline.getAuditData().setUpdatedBy(userAccount.getId());
                airline.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            airline = getAirlineByDto(airline, dto);
            airlineRepository.save(airline);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in AirlineService -> saveUpdateAirline()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================
     * This method is responsible to validate airline
     * ==================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateAirline(AirlineDTO dto) {
        List<String> validations = new ArrayList<>();
        if (dto.getStatus().equals(CommonStatus.NOT_SELECTED)) {
            validations.add(AirlineValidationMessage.EMPTY_STATUS);
        } else if (CommonValidation.stringNullValidation(dto.getName())) {
            validations.add(AirlineValidationMessage.EMPTY_BRANCH_NAME);
        } else if (CommonValidation.stringNullValidation(dto.getCode())) {
            validations.add(AirlineValidationMessage.EMPTY_CODE);
        }
        if (CommonValidation.stringNullValidation(dto.getId())) {
            if (airlineRepository.findAirlineByName(dto.getName()) != null) {
                validations.add(AirlineValidationMessage.EXIT_AIRLINE_NAME);
                return validations;
            }
            if (airlineRepository.findAirlineByCode(dto.getCode()) != null) {
                validations.add(AirlineValidationMessage.EXIT_AIRLINE_CODE);
                return validations;
            }
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to convert airline dto to airline entity
     * ========================================================================
     *
     * @param dto
     * @param airline
     * @return
     */
    public Airline getAirlineByDto(Airline airline, AirlineDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            airline.setId(Long.parseLong(dto.getId()));
        }
        airline.setName(dto.getName());
        airline.setCode(dto.getCode());
        airline.setContactNo1(dto.getContactNo1());
        airline.setContactNo2(dto.getContactNo2());
        airline.setEmail(dto.getEmail());
        airline.setWeb(dto.getWeb());
        airline.setContactPerson(dto.getContactPerson());
        airline.setCountry(dto.getCountry());
        airline.setStatus(dto.getStatus());
        airline.setComment(dto.getComment());
        airline.setAddress(dto.getAddress());
        return airline;
    }

    /**
     * ========================================================================
     * This method is responsible to convert airline entity to dto
     * ========================================================================
     *
     * @param airline
     * @return
     */
    public AirlineDTO getAirlineDTOByEntity(Airline airline) {
        AirlineDTO airlineDTO = new AirlineDTO();
        airlineDTO.setId(airline.getId().toString());
        airlineDTO.setName(airline.getName());
        airlineDTO.setCode(airline.getCode());
        airlineDTO.setContactNo1(airline.getContactNo1());
        airlineDTO.setContactNo2(airline.getContactNo2());
        airlineDTO.setEmail(airline.getEmail());
        airlineDTO.setWeb(airline.getWeb());
        airlineDTO.setContactPerson(airline.getContactPerson());
        airlineDTO.setCountry(airline.getCountry());
        airlineDTO.setStatus(airline.getStatus());
        airlineDTO.setComment(airline.getComment());
        airlineDTO.setAddress(airline.getAddress());
        return airlineDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive airlines
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<AirlineDTO> getAllActiveInactiveAirlineDTOs() {
        List<AirlineDTO> airlineDTOS = new ArrayList<>();
        try {
            airlineDTOS = airlineRepository.findAll().stream()
                    .map(arl -> getAirlineDTOByEntity(arl))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in AirlineService -> getAllActiveInactiveAirlineDTOs() " + e);
        }
        return airlineDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active airlines
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<AirlineDTO> getAllActiveAirlines() {
        List<AirlineDTO> airlineDTOS = new ArrayList<>();
        try {
            airlineDTOS = airlineRepository.findAll().stream()
                    .filter(arl -> arl.getStatus().equals(CommonStatus.ACTIVE))
                    .map(arl -> getAirlineDTOByEntity(arl))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in AirlineService -> getAllActiveAirlines() " + e);
        }
        return airlineDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get airline by id
     * ========================================================================
     *
     * @param token
     * @param airlineId
     * @return
     */
    @Transactional
    public AirlineDTO getAirlineById(String token, String airlineId) {
        AirlineDTO airlineDTO = new AirlineDTO();
        try {
            airlineDTO = getAirlineDTOByEntity(airlineRepository.getOne(Long.parseLong(airlineId)));
        } catch (Exception e) {
            LOGGER.warn("******** Exception in AirlineService -> getAirlineById() " + e);
        }
        return airlineDTO;
    }
}
