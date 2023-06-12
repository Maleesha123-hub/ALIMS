package com.alimsadmin.dto;

import com.alimsadmin.constants.enums.CommonStatus;

public class UserAccountDTO {

    private String id;
    private String adminUserId;
    private String rolName;
    private String userName;
    private String password;
    private CommonStatus status;
    private String airCode;
    private String userRoleId;
    private String updatedOn;
    private String CreatedOn;
    private String lastAccess;

    public UserAccountDTO() {
    }

    public UserAccountDTO(String id, String adminUserId, String rolName, String userName, String password, CommonStatus status, String airCode, String userRoleId, String updatedOn, String createdOn, String lastAccess) {
        this.id = id;
        this.adminUserId = adminUserId;
        this.rolName = rolName;
        this.userName = userName;
        this.password = password;
        this.status = status;
        this.airCode = airCode;
        this.userRoleId = userRoleId;
        this.updatedOn = updatedOn;
        this.CreatedOn = createdOn;
        this.lastAccess = lastAccess;
    }

    public UserAccountDTO(String id, String adminUserId, String userName, String password, CommonStatus status, String airCode, String userRoleId) {
        this.id = id;
        this.adminUserId = adminUserId;
        this.userName = userName;
        this.password = password;
        this.status = status;
        this.airCode = airCode;
        this.userRoleId = userRoleId;
    }

    public UserAccountDTO(String id, String userName, CommonStatus status, String userRoleId) {
        this.id = id;
        this.userName = userName;
        this.status = status;
        this.userRoleId = userRoleId;
    }

    public UserAccountDTO(String id, String userName, String password, CommonStatus status, String userRoleId) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.status = status;
        this.userRoleId = userRoleId;
    }

    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getAirCode() {
        return airCode;
    }

    public void setAirCode(String airCode) {
        this.airCode = airCode;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }
}
