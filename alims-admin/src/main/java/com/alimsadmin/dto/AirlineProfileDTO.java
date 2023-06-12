package com.alimsadmin.dto;

import com.alimsadmin.constants.enums.CommonStatus;

public class AirlineProfileDTO {

    private String id;
    private String profileName;
    private CommonStatus status;
    private String financialYearId;
    private String financialYear;
    private String airlineBranchId;
    private String airlineBranch;
    private String airlineSystemAdminId;
    private String airlineSystemAdmin;

    public AirlineProfileDTO() {
    }

    public AirlineProfileDTO(String id, String profileName, CommonStatus status, String financialYearId, String financialYear, String airlineBranchId, String airlineBranch, String airlineSystemAdminId, String airlineSystemAdmin) {
        this.id = id;
        this.profileName = profileName;
        this.status = status;
        this.financialYearId = financialYearId;
        this.financialYear = financialYear;
        this.airlineBranchId = airlineBranchId;
        this.airlineBranch = airlineBranch;
        this.airlineSystemAdminId = airlineSystemAdminId;
        this.airlineSystemAdmin = airlineSystemAdmin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    public String getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(String financialYearId) {
        this.financialYearId = financialYearId;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getAirlineBranchId() {
        return airlineBranchId;
    }

    public void setAirlineBranchId(String airlineBranchId) {
        this.airlineBranchId = airlineBranchId;
    }

    public String getAirlineBranch() {
        return airlineBranch;
    }

    public void setAirlineBranch(String airlineBranch) {
        this.airlineBranch = airlineBranch;
    }

    public String getAirlineSystemAdminId() {
        return airlineSystemAdminId;
    }

    public void setAirlineSystemAdminId(String airlineSystemAdminId) {
        this.airlineSystemAdminId = airlineSystemAdminId;
    }

    public String getAirlineSystemAdmin() {
        return airlineSystemAdmin;
    }

    public void setAirlineSystemAdmin(String airlineSystemAdmin) {
        this.airlineSystemAdmin = airlineSystemAdmin;
    }
}
