package com.alims.londontech.service;

import com.alims.londontech.constants.CommonValidationMessage;
import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.constants.validation.CountryAirportValidationMessages;
import com.alims.londontech.dto.CountryAirportDTO;
import com.alims.londontech.entities.AuditData;
import com.alims.londontech.entities.CountryAirport;
import com.alims.londontech.entities.Pilot;
import com.alims.londontech.entities.UserAccount;
import com.alims.londontech.repositories.CountryAirportRepository;
import com.alims.londontech.repositories.CountryRepository;
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
public class CountryAirportService {

    private final Logger LOGGER = LoggerFactory.getLogger(CountryAirportService.class);

    @Autowired
    private CountryAirportRepository countryAirportRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * ========================================================================
     * This method is responsible save and update airport details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateCountryAirport(String token, CountryAirportDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        CountryAirport countryAirport;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateCountryAirport(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                countryAirport = new CountryAirport();
                AuditData auditData = new AuditData();
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                auditData.setCreatedBy(userAccount.getId());
                countryAirport.setAuditData(auditData);
            } else {
                countryAirport = findCountryAirportById(Long.parseLong(dto.getId()));
                countryAirport.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
                countryAirport.getAuditData().setUpdatedBy(userAccount.getId());
            }
            countryAirport = getCountryAirportEntity(countryAirport, dto);
            countryAirport.setAirCode(userAccount.getAirCode());
            countryAirportRepository.save(countryAirport);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in CountryAirportService -> saveUpdateCountryAirport()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible to validate input country airport details.
     * ========================================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateCountryAirport(CountryAirportDTO dto) {
        List<String> validations = new ArrayList<>();
        if (CommonValidation.stringNullValidation(dto.getAirportName())) {
            validations.add(CountryAirportValidationMessages.EMPTY_AIRPORT_NAME);
        } else if (CommonValidation.stringNullValidation(dto.getContactNo())) {
            validations.add(CountryAirportValidationMessages.EMPTY_EMERGENCY_NO);
        } else if (dto.getCountryId() == CommonStatus.NOT_SELECTED.toString()) {
            validations.add(CountryAirportValidationMessages.EMPTY_COUNTRY);
        } else if (dto.getStatus() == CommonStatus.NOT_SELECTED.toString()) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to get country airport by id.
     * ========================================================================
     *
     * @param airportId
     * @return
     */
    public CountryAirport findCountryAirportById(Long airportId) {
        return countryAirportRepository.findCountryAirportById(airportId);
    }

    /**
     * ================================================================================
     * This method is responsible convert CountryAirportDTO to Airport Entity.
     * =================================================================================
     *
     * @param countryAirport
     * @param dto
     * @return
     */
    public CountryAirport getCountryAirportEntity(CountryAirport countryAirport, CountryAirportDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            countryAirport.setId(Long.parseLong(dto.getId()));
        }
        countryAirport.setAirport_name(dto.getAirportName());
        countryAirport.setEmergencyContactNumber(dto.getContactNo());
        countryAirport.setCountry(countryRepository.findCountryById(Long.parseLong(dto.getCountryId())));
        countryAirport.setStatus(CommonStatus.valueOf(dto.getStatus()));
        return countryAirport;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive airports
     * ========================================================================
     *
     * @param token
     * @return
     */
    @Transactional
    public List<CountryAirportDTO> getAllActiveInactiveAirportDTOs(String token) {
        List<CountryAirportDTO> countryAirportDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<CountryAirport> filterOnStatus = p -> p.getStatus() != CommonStatus.DELETED;
            Predicate<CountryAirport> filterOnAirCode = p -> p.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            countryAirportDTOS = countryAirportRepository.findAll().stream()
                    .filter(filterOnAirCode.and(filterOnStatus))
                    .map(arpt -> getCountryAirportDTOByEntity(arpt))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in CountryAirportService -> getAllActiveInactiveAirportDTOs() " + e);
        }
        return countryAirportDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active airports
     * ========================================================================
     *
     * @param token
     * @return
     */
    @Transactional
    public List<CountryAirportDTO> getAllActiveAirports(String token) {
        List<CountryAirportDTO> countryAirportDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<CountryAirport> filterOnStatus = p -> p.getStatus() == CommonStatus.ACTIVE;
            Predicate<CountryAirport> filterOnAirCode = p -> p.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            countryAirportDTOS = countryAirportRepository.findAll().stream()
                    .filter(filterOnAirCode.and(filterOnStatus))
                    .map(arpt -> getCountryAirportDTOByEntity(arpt))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in CountryAirportService -> getAllActiveAirports() " + e);
        }
        return countryAirportDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active airports by country
     * ========================================================================
     *
     * @param countryId
     * @param token
     * @return
     */
    @Transactional
    public List<CountryAirportDTO> getAllActiveAirportsByCountry(String token, String countryId) {
        List<CountryAirportDTO> countryAirportDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<CountryAirport> filterOnStatus = p -> p.getStatus() == CommonStatus.ACTIVE;
            Predicate<CountryAirport> filterOnAirCode = p -> p.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            Predicate<CountryAirport> filterOnCountryId = p -> p.getCountry().getId().equals(Long.parseLong(countryId));
            countryAirportDTOS = countryAirportRepository.findAll().stream()
                    .filter(filterOnAirCode.and(filterOnStatus).and(filterOnCountryId))
                    .map(arpt -> getCountryAirportDTOByEntity(arpt))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in CountryAirportService -> getAllActiveAirportsByCountry() " + e);
        }
        return countryAirportDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to convert airport entity to dto
     * ========================================================================
     *
     * @param countryAirport
     * @return
     */
    public CountryAirportDTO getCountryAirportDTOByEntity(CountryAirport countryAirport) {
        CountryAirportDTO countryAirportDTO = new CountryAirportDTO();
        countryAirportDTO.setId(countryAirport.getId().toString());
        countryAirportDTO.setContactNo(countryAirport.getEmergencyContactNumber());
        countryAirportDTO.setAirportName(countryAirport.getAirport_name());
        countryAirportDTO.setCountry(countryAirport.getCountry().getCountry_name());
        countryAirportDTO.setCountryId(countryAirport.getCountry().getId().toString());
        countryAirportDTO.setAirCode(countryAirport.getAirCode());
        countryAirportDTO.setStatus(countryAirport.getStatus().toString());
        return countryAirportDTO;
    }
}
