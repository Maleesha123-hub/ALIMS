package com.alims.londontech.dto;

import com.alims.londontech.constants.enums.CommonStatus;

public class AirplaneDTO {

    private String id;
    private String airplaneName;
    private CommonStatus status;

    public AirplaneDTO() {
    }

    public AirplaneDTO(String id, String airplaneName, CommonStatus status) {
        this.id = id;
        this.airplaneName = airplaneName;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAirplaneName() {
        return airplaneName;
    }

    public void setAirplaneName(String airplaneName) {
        this.airplaneName = airplaneName;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }
}
