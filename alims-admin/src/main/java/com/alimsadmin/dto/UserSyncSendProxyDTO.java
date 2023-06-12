package com.alimsadmin.dto;

public class UserSyncSendProxyDTO {

    private String airCode;
    private String airName;
    private UserRoleDTO userRoleDTO;
    private UserAccountDTO userAccountDTO;

    public UserSyncSendProxyDTO() {
    }

    public UserSyncSendProxyDTO(String airCode, String airName, UserRoleDTO userRoleDTO, UserAccountDTO userAccountDTO) {
        this.airCode = airCode;
        this.airName = airName;
        this.userRoleDTO = userRoleDTO;
        this.userAccountDTO = userAccountDTO;
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

    public UserRoleDTO getUserRoleDTO() {
        return userRoleDTO;
    }

    public void setUserRoleDTO(UserRoleDTO userRoleDTO) {
        this.userRoleDTO = userRoleDTO;
    }

    public UserAccountDTO getUserAccountDTO() {
        return userAccountDTO;
    }

    public void setUserAccountDTO(UserAccountDTO userAccountDTO) {
        this.userAccountDTO = userAccountDTO;
    }
}
