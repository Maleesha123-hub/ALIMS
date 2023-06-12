package com.alims.londontech.dto;

public class CountryDTO {

    private String id;
    private String countryName;
    private String status;
    private String airCode;

    public CountryDTO() {
    }

    public CountryDTO(String id, String countryName, String status, String airCode) {
        this.id = id;
        this.countryName = countryName;
        this.status = status;
        this.airCode = airCode;
    }

    public String getAirCode() {
        return airCode;
    }

    public void setAirCode(String airCode) {
        this.airCode = airCode;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
