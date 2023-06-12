package com.alims.londontech.service;

import com.alims.londontech.constants.CommonValidationMessage;
import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.constants.enums.Gender;
import com.alims.londontech.constants.enums.Race;
import com.alims.londontech.constants.enums.Religion;
import com.alims.londontech.dto.CountryDTO;
import com.alims.londontech.dto.PilotDTO;
import com.alims.londontech.entities.AuditData;
import com.alims.londontech.entities.Pilot;
import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.repositories.PilotRepository;
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
public class PilotService {

    private final Logger LOGGER = LoggerFactory.getLogger(PilotService.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private PilotRepository pilotRepository;

    /**
     * ========================================================================
     * This method is responsible save and update pilot details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdatePilot(String token, PilotDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        Pilot pilot;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validatePilot(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                pilot = new Pilot();
                AuditData auditData = new AuditData();
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                auditData.setCreatedBy(userAccount.getId());
                pilot.setAuditData(auditData);
            } else {
                pilot = pilotRepository.getOne(Long.parseLong(dto.getId()));
                pilot.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
                pilot.getAuditData().setUpdatedBy(userAccount.getId());
            }
            pilot = getPilotEntity(pilot, dto);
            pilot.setAirCode(userAccount.getAirCode());
            String pilotId = pilotRepository.save(pilot).getId().toString();
            userAccountService.findAccountById(Long.valueOf(dto.getPilotUser())).setPilotId(pilotId);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in PilotService -> saveUpdatePilot()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible to validate pilot
     * ========================================================================
     *
     * @param dto
     * @return
     */
    public List<String> validatePilot(PilotDTO dto) {
        List<String> validations = new ArrayList<>();
        if (CommonValidation.stringNullValidation(dto.getInitials())) {
            validations.add(CommonValidationMessage.EMPTY_INIT);
        } else if (CommonValidation.stringNullValidation(dto.getFirstName())) {
            validations.add(CommonValidationMessage.EMPTY_FIRST_NAME);
        } else if (CommonValidation.stringNullValidation(dto.getLastName())) {
            validations.add(CommonValidationMessage.EMPTY_LAST_NAME);
        } else if (CommonValidation.stringNullValidation(dto.getFullName())) {
            validations.add(CommonValidationMessage.EMPTY_FULL_NAME);
        } else if (CommonValidation.stringNullValidation(dto.getNic())) {
            validations.add(CommonValidationMessage.EMPTY_NIC);
        } else if (CommonValidation.stringNullValidation(dto.getEmergencyContactNo())) {
            validations.add(CommonValidationMessage.EMPTY_EMERG_CONT_NO);
        } else if (CommonValidation.stringNullValidation(dto.getEmail())) {
            validations.add(CommonValidationMessage.EMPTY_EMAIL);
        } else if (CommonValidation.stringNullValidation(dto.getDob())) {
            validations.add(CommonValidationMessage.EMPTY_DOB);
        } else if (CommonValidation.stringNullValidation(dto.getReligion())) {
            validations.add(CommonValidationMessage.EMPTY_RELIGION);
        } else if (CommonValidation.stringNullValidation(dto.getGender())) {
            validations.add(CommonValidationMessage.EMPTY_GENDER);
        } else if (CommonValidation.stringNullValidation(dto.getRace())) {
            validations.add(CommonValidationMessage.EMPTY_RACE);
        } else if (dto.getPilotUser().equalsIgnoreCase("NOT_SELECTED")) {
            validations.add(CommonValidationMessage.EMPTY_USER);
        } else if (CommonValidation.stringNullValidation(dto.getPilotRegDate())) {
            validations.add(CommonValidationMessage.EMPTY_DATE_OF_REG);
        } else if (dto.getPilotStatus().equalsIgnoreCase("NOT_SELECTED")) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        } else if (CommonValidation.stringNullValidation(dto.getAddress())) {
            validations.add(CommonValidationMessage.EMPTY_ADDRESS);
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to get entity by dto
     * ========================================================================
     *
     * @param pilot
     * @param dto
     * @return
     */
    public Pilot getPilotEntity(Pilot pilot, PilotDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            pilot.setId(Long.parseLong(dto.getId()));
        }
        pilot.setInitials(dto.getInitials());
        pilot.setFirstName(dto.getFirstName());
        pilot.setLastName(dto.getLastName());
        pilot.setFullName(dto.getFullName());
        pilot.setEmail(dto.getEmail());
        pilot.setNic(dto.getNic());
        pilot.setDob(DateTimeUtil.getFormattedDateTime(dto.getDob()));
        pilot.setReligion(Religion.valueOf(dto.getReligion()));
        pilot.setGender(Gender.valueOf(dto.getGender()));
        pilot.setRace(Race.valueOf(dto.getRace()));
        pilot.setRegistrationDate(DateTimeUtil.getFormattedDateTime(dto.getPilotRegDate()));
        pilot.setStatus(CommonStatus.valueOf(dto.getPilotStatus()));
        pilot.setAddress(dto.getAddress());
        pilot.setEmergencyContactNumber(dto.getEmergencyContactNo());
        pilot.setUserAccount(userAccountService.findAccountById(Long.valueOf(dto.getPilotUser())));
        return pilot;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive pilots
     * ========================================================================
     *
     * @param token
     * @return
     */
    @Transactional
    public List<PilotDTO> getAllActiveInactivePilotDTOs(String token) {
        List<PilotDTO> pilotDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Pilot> filterOnStatus = p -> p.getStatus() != CommonStatus.DELETED;
            Predicate<Pilot> filterOnAirCode = p -> p.getAirCode().equalsIgnoreCase(userAccount.getAirCode());

            pilotDTOS = pilotRepository.findAll().stream()
                    .filter(filterOnStatus.and(filterOnAirCode))
                    .map(p -> getPilotDTO(p))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in PilotService -> getAllActiveInactivePilotDTOs() " + e);
        }
        return pilotDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get pilot dto by pilot
     * ========================================================================
     *
     * @param pilot
     * @return
     */
    public PilotDTO getPilotDTO(Pilot pilot) {
        PilotDTO pilotDTO = new PilotDTO();
        pilotDTO.setId(pilot.getId().toString());
        pilotDTO.setInitials(pilot.getInitials());
        pilotDTO.setFirstName(pilot.getFirstName());
        pilotDTO.setLastName(pilot.getLastName());
        pilotDTO.setFullName(pilot.getFullName());
        pilotDTO.setNic(pilot.getNic());
        pilotDTO.setEmergencyContactNo(pilot.getEmergencyContactNumber());
        pilotDTO.setEmail(pilot.getEmail());
        pilotDTO.setDob(pilot.getDob().toString());
        pilotDTO.setReligion(pilot.getReligion().toString());
        pilotDTO.setGender(pilot.getGender().toString());
        pilotDTO.setRace(pilot.getRace().toString());
        pilotDTO.setPilotUser(pilot.getUserAccount().getId().toString());
        pilotDTO.setPilotUserName(pilot.getUserAccount().getUsername());
        pilotDTO.setPilotRegDate(pilot.getRegistrationDate().toString());
        pilotDTO.setPilotStatus(pilot.getStatus().toString());
        pilotDTO.setAddress(pilot.getAddress());
        return pilotDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active pilots
     * ========================================================================
     *
     * @param token
     * @return
     */
    @Transactional
    public List<PilotDTO> getAllActivePilotDTOs(String token) {
        List<PilotDTO> pilotDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Pilot> filterOnStatus = p -> p.getStatus() == CommonStatus.ACTIVE;
            Predicate<Pilot> filterOnAirCode = p -> p.getAirCode().equalsIgnoreCase(userAccount.getAirCode());

            pilotDTOS = pilotRepository.findAll().stream()
                    .filter(filterOnStatus.and(filterOnAirCode))
                    .map(p -> getPilotDTO(p))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in PilotService -> getAllActivePilotDTOs() " + e);
        }
        return pilotDTOS;
    }
}
