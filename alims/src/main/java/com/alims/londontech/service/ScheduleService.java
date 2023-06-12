package com.alims.londontech.service;

import com.alims.londontech.constants.CommonValidationMessage;
import com.alims.londontech.constants.enums.CommonStatus;
import com.alims.londontech.constants.validation.ScheduleValidationMessages;
import com.alims.londontech.dto.ScheduleDTO;
import com.alims.londontech.entities.*;
import com.alims.londontech.repositories.*;
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
public class ScheduleService {

    private final Logger LOGGER = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryAirportRepository countryAirportRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PilotRepository pilotRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserAccountService userAccountService;

    /**
     * ========================================================================
     * This method is responsible save and update airline schedule details.
     * ========================================================================
     *
     * @param token
     * @param dto
     * @return
     */
    @Transactional
    public CommonResponse saveUpdateSchedule(String token, ScheduleDTO dto) {
        CommonResponse commonResponse = new CommonResponse();
        Schedule schedule;
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            List<String> validations = validateSchedule(dto);
            if (!CollectionUtils.isEmpty(validations)) {
                commonResponse.setErrorMessages(validations);
                return commonResponse;
            }
            if (CommonValidation.stringNullValidation(dto.getId())) {
                schedule = new Schedule();
                AuditData auditData = new AuditData();
                auditData.setCreatedBy(userAccount.getId());
                auditData.setCreatedOn(DateTimeUtil.getSriLankaTime());
                schedule.setAuditData(auditData);
            } else {
                schedule = scheduleRepository.getOne(Long.parseLong(dto.getId()));
                schedule.getAuditData().setUpdatedBy(userAccount.getId());
                schedule.getAuditData().setUpdatedOn(DateTimeUtil.getSriLankaTime());
            }
            schedule = getScheduleByDTO(schedule, dto);
            schedule.setAirCode(userAccount.getAirCode());
            scheduleRepository.save(schedule);
            commonResponse.setStatus(true);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in ScheduleService -> saveUpdateSchedule()" + e);
        }
        return commonResponse;
    }

    /**
     * ==================================================
     * This method is responsible to validate schedule
     * ==================================================
     *
     * @param dto
     * @return
     */
    public List<String> validateSchedule(ScheduleDTO dto) {
        List<String> validations = new ArrayList<>();
        if (dto.getAirplaneId().equalsIgnoreCase(CommonStatus.NOT_SELECTED.toString())) {
            validations.add(ScheduleValidationMessages.EMPTY_AIRLINE);
        } else if (dto.getPilots().isEmpty()) {
            validations.add(ScheduleValidationMessages.EMPTY_PILOTS);
        } else if (dto.getCrews().isEmpty()) {
            validations.add(ScheduleValidationMessages.EMPTY_CREWS);
        } else if (CommonValidation.stringNullValidation(dto.getScheduleName())) {
            validations.add(ScheduleValidationMessages.EMPTY_SCH_NAME);
        } else if (dto.getDepartureDate().equalsIgnoreCase(" 00:00")) {
            validations.add(ScheduleValidationMessages.EMPTY_DATE_OF_FLIGHT);
        } else if (dto.getArrivalDate().equalsIgnoreCase(" 00:00")) {
            validations.add(ScheduleValidationMessages.EMPTY_ARRIVAL_DATE);
        } else if (dto.getDepartureTime().equalsIgnoreCase(":00")) {
            validations.add(ScheduleValidationMessages.EMPTY_DEPARTURE_TIME);
        } else if (dto.getArrivalTime().equalsIgnoreCase(":00")) {
            validations.add(ScheduleValidationMessages.EMPTY_ARRIVAL_TIME);
        } else if (dto.getCountryId().equalsIgnoreCase(CommonStatus.NOT_SELECTED.toString())) {
            validations.add(ScheduleValidationMessages.EMPTY_COUNTRY);
        } else if (dto.getStartPoint().equalsIgnoreCase(CommonStatus.NOT_SELECTED.toString())) {
            validations.add(ScheduleValidationMessages.EMPTY_STARTING_POINT);
        } else if (dto.getLandingPoint().equalsIgnoreCase(CommonStatus.NOT_SELECTED.toString())) {
            validations.add(ScheduleValidationMessages.EMPTY_LANDING_POINT);
        } else if (dto.getStatus().equalsIgnoreCase(CommonStatus.NOT_SELECTED.toString())) {
            validations.add(CommonValidationMessage.EMPTY_STATUS);
        }
        return validations;
    }

    /**
     * ========================================================================
     * This method is responsible to get all active inactive schedules
     * ========================================================================
     *
     * @param token
     * @return
     */
    @Transactional
    public List<ScheduleDTO> getAllActiveInactiveSchedulesDTOs(String token) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        try {
            UserAccount userAccount = userAccountService.getUserByToken(token);
            Predicate<Schedule> filterOnStatus = c -> c.getStatus() != CommonStatus.DELETED;
            Predicate<Schedule> filterOnAirCode = c -> c.getAirCode().equalsIgnoreCase(userAccount.getAirCode());

            scheduleDTOS = scheduleRepository.findAll().stream()
                    .filter(filterOnStatus.and(filterOnAirCode))
                    .map(s -> getScheduleDTO(s))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.warn("******** Exception in ScheduleService -> getAllActiveInactiveSchedulesDTOs() " + e);
        }
        return scheduleDTOS;
    }

    /**
     * ========================================================================
     * This method is responsible to get schedule dto by schedule
     * ========================================================================
     *
     * @param schedule
     * @return
     */
    public ScheduleDTO getScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId().toString());
        scheduleDTO.setCountryId(schedule.getCountry().getId().toString());
        scheduleDTO.setCountryName(schedule.getCountry().getCountry_name());
        scheduleDTO.setDepartureDate(DateTimeUtil.getFormattedDateTime(schedule.getDepartureDate()));
        scheduleDTO.setDepartureTime(schedule.getDepartureTime().toString());
        scheduleDTO.setArrivalTime(schedule.getArrivalTime().toString());
        scheduleDTO.setStatus(schedule.getStatus().toString());
        return scheduleDTO;
    }

    /**
     * ========================================================================
     * This method is responsible to get schedule by schedule dto
     * ========================================================================
     *
     * @param dto
     * @param schedule
     * @return
     */
    public Schedule getScheduleByDTO(Schedule schedule, ScheduleDTO dto) {
        if (!CommonValidation.stringNullValidation(dto.getId())) {
            schedule.setId(Long.parseLong(dto.getId()));
        }
        schedule.setScheduleName(dto.getScheduleName());
        schedule.setStatus(CommonStatus.valueOf(dto.getStatus()));
        schedule.setAirplane(airplaneRepository.getOne(Long.parseLong(dto.getAirplaneId())));
        schedule.setCountry(countryRepository.getOne(Long.parseLong(dto.getCountryId())));
        schedule.setCountryAirport(countryAirportRepository.getOne(Long.parseLong(dto.getStartPoint())));
        schedule.setLandingPoint(countryAirportRepository.getOne(Long.parseLong(dto.getLandingPoint())));
        schedule.setArrivalTime(DateTimeUtil.getFormattedTime(dto.getArrivalTime()));
        schedule.setDepartureTime(DateTimeUtil.getFormattedTime(dto.getDepartureTime()));
        schedule.setDepartureDate(DateTimeUtil.getFormattedDateTime(dto.getDepartureDate()));
        schedule.setArrivalDate(DateTimeUtil.getFormattedDateTime(dto.getArrivalDate()));
        schedule.setPilots(dto.getPilots().stream().map(pilotDTO -> pilotRepository.getOne(Long.parseLong(pilotDTO.getId()))).collect(Collectors.toSet()));
        schedule.setCrews(dto.getCrews().stream().map(crewDTO -> crewRepository.getOne(Long.parseLong(crewDTO.getId()))).collect(Collectors.toSet()));
        return schedule;
    }
}
