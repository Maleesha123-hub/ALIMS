package com.alims.londontech.dto;

import com.alims.londontech.entities.Crew;
import com.alims.londontech.entities.Pilot;

import java.util.List;
import java.util.Set;

public class ScheduleDTO {
    private String id;
    private String scheduleName;
    private String countryName;
    private String countryId;
    private String airplaneId;
    private String departureTime;
    private String departureDate;
    private String arrivalDate;
    private String startPoint;
    private String landingPoint;
    private String arrivalTime;
    private String status;
    private Set<PilotDTO> pilots;
    private Set<CrewDTO> crews;

    public ScheduleDTO() {
    }

    public ScheduleDTO(String id, String scheduleName, String countryName, String countryId, String airplaneId, String departureTime, String departureDate, String arrivalDate, String startPoint, String landingPoint, String arrivalTime, String status, Set<PilotDTO> pilots, Set<CrewDTO> crews) {
        this.id = id;
        this.scheduleName = scheduleName;
        this.countryName = countryName;
        this.countryId = countryId;
        this.airplaneId = airplaneId;
        this.departureTime = departureTime;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.startPoint = startPoint;
        this.landingPoint = landingPoint;
        this.arrivalTime = arrivalTime;
        this.status = status;
        this.pilots = pilots;
        this.crews = crews;
    }

    public Set<PilotDTO> getPilots() {
        return pilots;
    }

    public void setPilots(Set<PilotDTO> pilots) {
        this.pilots = pilots;
    }

    public Set<CrewDTO> getCrews() {
        return crews;
    }

    public void setCrews(Set<CrewDTO> crews) {
        this.crews = crews;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getAirplaneId() {
        return airplaneId;
    }

    public void setAirplaneId(String airplaneId) {
        this.airplaneId = airplaneId;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getLandingPoint() {
        return landingPoint;
    }

    public void setLandingPoint(String landingPoint) {
        this.landingPoint = landingPoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
