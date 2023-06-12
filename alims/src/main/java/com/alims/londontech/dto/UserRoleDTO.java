package com.alims.londontech.dto;

public class UserRoleDTO {

    private String id;
    private String description;
    private String roleName;
    private String status;
    private String airCode;
    private String airName;
    private String adminUserId;

    public UserRoleDTO() {
    }

    public UserRoleDTO(String id, String description, String roleName, String status, String airCode, String adminUserId) {
        this.id = id;
        this.description = description;
        this.roleName = roleName;
        this.status = status;
        this.airCode = airCode;
        this.adminUserId = adminUserId;
    }

    public UserRoleDTO(String id, String roleName, String description, String status) {
        this.id = id;
        this.roleName = roleName;
        this.description = description;
        this.status = status;
    }

    public String getAirCode() {
        return airCode;
    }

    public void setAirCode(String airCode) {
        this.airCode = airCode;
    }

    public String getAirName() {
        return airName;
    }

    public void setAirName(String airName) {
        this.airName = airName;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
