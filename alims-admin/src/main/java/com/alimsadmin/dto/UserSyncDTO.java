package com.alimsadmin.dto;

public class UserSyncDTO {

    private String userName;
    private String userRole;
    private String userRoleStatus;
    private String userStatus;
    private String createdOn;
    private String updatedOn;
    private String lastAccess;

    public UserSyncDTO() {
    }

    public UserSyncDTO(String userName, String userRole, String userRoleStatus, String userStatus) {
        this.userName = userName;
        this.userRole = userRole;
        this.userRoleStatus = userRoleStatus;
        this.userStatus = userStatus;
    }

    public UserSyncDTO(String userName, String userRole, String userRoleStatus, String userStatus, String createdOn, String updatedOn, String lastAccess) {
        this.userName = userName;
        this.userRole = userRole;
        this.userRoleStatus = userRoleStatus;
        this.userStatus = userStatus;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.lastAccess = lastAccess;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleStatus() {
        return userRoleStatus;
    }

    public void setUserRoleStatus(String userRoleStatus) {
        this.userRoleStatus = userRoleStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(String lastAccess) {
        this.lastAccess = lastAccess;
    }
}
