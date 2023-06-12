package com.alims.londontech.service;

import com.alims.londontech.constants.CommonValidationMessage;
import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.constants.enums.Gender;
import com.alims.londontech.constants.enums.Race;
import com.alims.londontech.constants.enums.Religion;
import com.alims.londontech.dto.CrewDTO;
import com.alims.londontech.entities.AuditData;
import com.alims.londontech.entities.Crew;
import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.repositories.CrewRepository;
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
public class CrewService {

    private final Logger LOGGER = LoggerFactory.getLogger(CrewService.class);

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CrewRepository crewRepository;

    /**
     * ========================================================================
     * This method is responsible save and update crew details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateCrew(String token, CrewDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        Crew crew;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateCrew(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                crew = new Crew();
                AuditData auditData = new AuditData();
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                auditData.setCreatedBy(userAccount.getId());
                crew.setAuditData(auditData);
            } else {
                crew = crewRepository.getOne(Long.parseLong(dto.getId()));
                crew.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
                crew.getAuditData().setUpdatedBy(userAccount.getId());
            }
            crew = getCrewEntity(crew, dto);
            crew.setAirCode(userAccount.getAirCode());
            String crewId = crewRepository.save(crew).getId().toString();
            userAccountService.findAccountById(Long.valueOf(dto.getCrewUser())).setCrewId(crewId);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in CrewService -> saveUpdateCrew()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible to validate crew
     * ========================================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateCrew(CrewDTO dto) {
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
        } else if (dto.getCrewUser().equalsIgnoreCase("NOT_SELECTED")) {
            validations.add(CommonValidationMessage.EMPTY_USER);
        } else if (CommonValidation.stringNullValidation(dto.getCrewRegDate())) {
            validations.add(CommonValidationMessage.EMPTY_DATE_OF_REG);
        } else if (dto.getCrewStatus().equalsIgnoreCase("NOT_SELECTED")) {
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
     * @param crew
     * @param dto
     * @return
     */
    public Crew getCrewEntity(Crew crew, CrewDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            crew.setId(Long.parseLong(dto.getId()));
        }
        crew.setInitials(dto.getInitials());
        crew.setFirstName(dto.getFirstName());
        crew.setLastName(dto.getLastName());
        crew.setFullName(dto.getFullName());
        crew.setEmail(dto.getEmail());
        crew.setNic(dto.getNic());
        crew.setDob(DateTimeUtil.getFormattedDateTime(dto.getDob()));
        crew.setReligion(Religion.valueOf(dto.getReligion()));
        crew.setGender(Gender.valueOf(dto.getGender()));
        crew.setRace(Race.valueOf(dto.getRace()));
        crew.setRegistrationDate(DateTimeUtil.getFormattedDateTime(dto.getCrewRegDate()));
        crew.setStatus(CommonStatus.valueOf(dto.getCrewStatus()));
        crew.setAddress(dto.getAddress());
        crew.setEmergencyContactNumber(dto.getEmergencyContactNo());
        crew.setUserAccount(userAccountService.findAccountById(Long.valueOf(dto.getCrewUser())));
        return crew;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive crews
     * ========================================================================
     *
     * @param token
     * @return
     */
    @Transactional
    public List<CrewDTO> getAllActiveInactiveCrewDTOs(String token) {
        List<CrewDTO> crewDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Crew> filterOnStatus = c -> c.getStatus() != CommonStatus.DELETED;
            Predicate<Crew> filterOnAirCode = c -> c.getAirCode().equalsIgnoreCase(userAccount.getAirCode());

            crewDTOS = crewRepository.findAll().stream()
                    .filter(filterOnStatus.and(filterOnAirCode))
                    .map(c -> getCrewDTO(c))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in CrewService -> getAllActiveInactiveCrewDTOs() " + e);
        }
        return crewDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get crew dto by crew
     * ========================================================================
     *
     * @param crew
     * @return
     */
    public CrewDTO getCrewDTO(Crew crew) {
        CrewDTO crewDTO = new CrewDTO();
        crewDTO.setId(crew.getId().toString());
        crewDTO.setInitials(crew.getInitials());
        crewDTO.setFirstName(crew.getFirstName());
        crewDTO.setLastName(crew.getLastName());
        crewDTO.setFullName(crew.getFullName());
        crewDTO.setNic(crew.getNic());
        crewDTO.setEmergencyContactNo(crew.getEmergencyContactNumber());
        crewDTO.setEmail(crew.getEmail());
        crewDTO.setDob(crew.getDob().toString());
        crewDTO.setReligion(crew.getReligion().toString());
        crewDTO.setGender(crew.getGender().toString());
        crewDTO.setRace(crew.getRace().toString());
        crewDTO.setCrewUser(crew.getUserAccount().getId().toString());
        crewDTO.setCrewUserName(crew.getUserAccount().getUsername());
        crewDTO.setCrewRegDate(crew.getRegistrationDate().toString());
        crewDTO.setCrewStatus(crew.getStatus().toString());
        crewDTO.setAddress(crew.getAddress());
        return crewDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active crews
     * ========================================================================
     *
     * @param token
     * @return
     */
    @Transactional
    public List<CrewDTO> getAllActiveCrewDTOs(String token) {
        List<CrewDTO> crewDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Crew> filterOnStatus = c -> c.getStatus() == CommonStatus.ACTIVE;
            Predicate<Crew> filterOnAirCode = c -> c.getAirCode().equalsIgnoreCase(userAccount.getAirCode());

            crewDTOS = crewRepository.findAll().stream()
                    .filter(filterOnStatus.and(filterOnAirCode))
                    .map(c -> getCrewDTO(c))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in CrewService -> getAllActiveCrewDTOs() " + e);
        }
        return crewDTOS;
    }
}
