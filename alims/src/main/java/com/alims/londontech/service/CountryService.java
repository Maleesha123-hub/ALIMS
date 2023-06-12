package com.alims.londontech.service;

import com.alims.londontech.constants.CommonConstants;
import com.alims.londontech.constants.CommonValidationMessage;
import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.constants.validation.CountryValidationMessages;
import com.alims.londontech.dto.CountryAirportDTO;
import com.alims.londontech.dto.CountryDTO;
import com.alims.londontech.entities.*;
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
public class CountryService {

    private final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * ========================================================================
     * This method is responsible save and update country details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateCountry(String token, CountryDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        Country country;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateCountry(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                country = new Country();
                AuditData auditData = new AuditData();
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                auditData.setCreatedBy(userAccount.getId());
                country.setAuditData(auditData);
            } else {
                country = findCountryById(Long.parseLong(dto.getId()));
                country.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
                country.getAuditData().setUpdatedBy(userAccount.getId());
            }
            country = getCountryEntity(country, dto);
            country.setAirCode(userAccount.getAirCode());
            countryRepository.save(country);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in CountryService -> saveUpdateCountry()" + e);
        }
        return commonResponse;
    }

    /**
     * ========================================================================
     * This method is responsible to validate input country details.
     * ========================================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateCountry(CountryDTO dto) {
        List<String> validations = new ArrayList<>();
        if (CommonValidation.stringNullValidation(dto.getCountryName())) {
            validations.add(CountryValidationMessages.EMPTY_COUNTRY_NAME);
        } else if (dto.getStatus() == CommonStatus.NOT_SELECTED.toString()) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to get country by id.
     * ========================================================================
     *
     * @param countryId
     * @return
     */
    public Country findCountryById(Long countryId) {
        return countryRepository.findCountryById(countryId);
    }

    /**
     * ========================================================================
     * This method is responsible convert CountryDTO to Country Entity.
     * ========================================================================
     *
     * @param country
     * @param dto
     * @return
     */
    public Country getCountryEntity(Country country, CountryDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            country.setId(Long.parseLong(dto.getId()));
        }
        country.setCountry_name(dto.getCountryName());
        country.setStatus(CommonStatus.valueOf(dto.getStatus()));
        return country;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive countries
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<CountryDTO> getAllActiveInactiveCountryDTOs(String token) {
        List<CountryDTO> countryDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Country> filterOnStatus = c -> c.getStatus() != CommonStatus.DELETED;
            Predicate<Country> filterOnAirCode = c -> c.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            countryDTOS = countryRepository.findAll().stream()
                    .filter(filterOnStatus.and(filterOnAirCode))
                    .map(arl -> getCountryDTOByEntity(arl))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in CountryService -> getAllActiveInactiveCountryDTOs() " + e);
        }
        return countryDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to convert country entity to dto
     * ========================================================================
     *
     * @param country
     * @return
     */
    public CountryDTO getCountryDTOByEntity(Country country) {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(country.getId().toString());
        countryDTO.setCountryName(country.getCountry_name());
        countryDTO.setAirCode(country.getAirCode());
        countryDTO.setStatus(country.getStatus().toString());
        return countryDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active countries
     * ========================================================================
     *
     * @return
     */
    @Transactional
    public List<CountryDTO> getAllActiveCountries(String token) {
        List<CountryDTO> countryDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Country> filterOnAirCode = c -> c.getAirCode().equalsIgnoreCase(userAccount.getAirCode());
            Predicate<Country> filterByStatus = c -> c.getStatus() == CommonStatus.ACTIVE;
            countryDTOS = countryRepository.findAll().stream()
                    .filter(filterByStatus.and(filterOnAirCode))
                    .map(ur -> getCountryDTOByEntity(ur))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("**************** Exception in CountryService -> getAllActiveCountries()" + e);
        }
        return countryDTOS;
    }
}
