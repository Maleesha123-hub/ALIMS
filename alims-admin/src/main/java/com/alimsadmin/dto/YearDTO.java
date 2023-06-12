package com.alimsadmin.dto;

import com.alimsadmin.constants.enums.CommonStatus;

public class YearDTO {

    private String id;
    private String year;
    private String startDate;
    private String endDate;
    private CommonStatus status;

    public YearDTO() {
    }

    public YearDTO(String id, String year, String startDate, String endDate, CommonStatus status) {
        this.id = id;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "YearDTO{" +
                "id='" + id + '\'' +
                ", year='" + year + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status=" + status +
                '}';
    }
}
