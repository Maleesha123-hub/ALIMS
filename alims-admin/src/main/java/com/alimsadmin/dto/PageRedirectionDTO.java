package com.alimsadmin.dto;

public class PageRedirectionDTO {
    private String homePage;
    private String userName;
    private String token;
    private String userRole;

    public PageRedirectionDTO() {
    }

    public PageRedirectionDTO(String homePage, String userName, String token, String userRole) {
        this.homePage = homePage;
        this.userName = userName;
        this.token = token;
        this.userRole = userRole;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
