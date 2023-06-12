package com.alims.londontech.service;

import com.alims.londontech.constants.CommonValidationMessage;
import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.constants.validation.AirplaneValidationMessages;
import com.alims.londontech.dto.AirplaneDTO;
import com.alims.londontech.entities.Airplane;
import com.alims.londontech.entities.AuditData;
import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.repositories.AirplaneRepository;
import com.alims.londontech.utils.CommonResponse;
import com.alims.londontech.utils.CommonValidation;
import com.alims.londontech.utils.DateTimeUtil;
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
public class AirplaneService {

    private final Logger LOGGER = LoggerFactory.getLogger(AirplaneService.class);

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * ========================================================================
     * This method is responsible save and update airplane details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateAirplane(String token, AirplaneDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        Airplane airplane;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateAirplane(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                airplane = new Airplane();
                AuditData auditData = new AuditData();
                auditData.setCreatedBy(userAccount.getId());
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                airplane.setAuditData(auditData);
            } else {
                airplane = airplaneRepository.getOne(Long.parseLong(dto.getId()));
                airplane.getAuditData().setUpdatedBy(userAccount.getId());
                airplane.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            airplane = getAirplaneEntityByDTO(airplane, dto);
            airplane.setAirCode(userAccount.getAirCode());
            airplaneRepository.save(airplane);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in AirplaneService -> saveUpdateAirplane()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================
     * This method is responsible to validate airplane
     * ==================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateAirplane(AirplaneDTO dto) {
        List<String> validations = new ArrayList<>();
        if (CommonValidation.stringNullValidation(dto.getAirplaneName())) {
            validations.add(AirplaneValidationMessages.EMPTY_AIRPLANE_NAME);
        } else if (dto.getStatus() == CommonStatus.NOT_SELECTED) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        }
        return validations;
    }

    /**
     * ==================================================
     * This method is responsible to get entity by dto
     * ==================================================
     *
     * @param airplane
     * @param dto
     * @return
     */
    public Airplane getAirplaneEntityByDTO(Airplane airplane, AirplaneDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            airplane.setId(Long.parseLong(dto.getId()));
        }
        airplane.setAirplane_name(dto.getAirplaneName());
        airplane.setStatus(dto.getStatus());
        return airplane;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive airplanes
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<AirplaneDTO> getAllActiveInactiveAirplanes(String token) {
        List<AirplaneDTO> airplaneDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Airplane> filterOnStatus = arp -> arp.getStatus() != CommonStatus.DELETED;
            Predicate<Airplane> filterOnAirCode = arp -> arp.getAirCode().equalsIgnoreCase(userAccount.getAirCode());

            airplaneDTOS = airplaneRepository.findAll().stream()
                    .filter(filterOnStatus.and(filterOnAirCode))
                    .map(ur -> getUserAccountDTO(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in AirplaneService -> getAllActiveInactiveAirplanes() " + e);
        }
        return airplaneDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active airplanes
     * ========================================================================
     *
     * @param token
     * @return
     */
    @Transactional
    public List<AirplaneDTO> getAllActiveAirPlanes(String token) {
        List<AirplaneDTO> airplaneDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Airplane> filterOnStatus = arp -> arp.getStatus() == CommonStatus.ACTIVE;
            Predicate<Airplane> filterOnAirCode = arp -> arp.getAirCode().equalsIgnoreCase(userAccount.getAirCode());

            airplaneDTOS = airplaneRepository.findAll().stream()
                    .filter(filterOnStatus.and(filterOnAirCode))
                    .map(ur -> getUserAccountDTO(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in AirplaneService -> getAllActiveAirPlanes() " + e);
        }
        return airplaneDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to convert airplane entity to dto.
     * ========================================================================
     *
     * @param airplane
     * @return
     */
    public AirplaneDTO getUserAccountDTO(Airplane airplane) {
        AirplaneDTO airplaneDTO = new AirplaneDTO();
        airplaneDTO.setId(airplane.getId().toString());
        airplaneDTO.setAirplaneName(airplane.getAirplane_name());
        airplaneDTO.setStatus(airplane.getStatus());
        return airplaneDTO;
    }
}
