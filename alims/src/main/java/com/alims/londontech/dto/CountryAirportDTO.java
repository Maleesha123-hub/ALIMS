package com.alims.londontech.dto;

public class CountryAirportDTO {

    private String id;
    private String airportName;
    private String contactNo;
    private String country;
    private String countryId;
    private String status;
    private String airCode;

    public CountryAirportDTO() {
    }

    public CountryAirportDTO(String id, String airportName, String contactNo, String country, String countryId, String status, String airCode) {
        this.id = id;
        this.airportName = airportName;
        this.contactNo = contactNo;
        this.country = country;
        this.countryId = countryId;
        this.status = status;
        this.airCode = airCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAirCode() {
        return airCode;
    }

    public void setAirCode(String airCode) {
        this.airCode = airCode;
    }
}
