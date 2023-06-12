package com.alimsadmin.dto.JWT;

public class AuthResponse {

    private String jwtToken;
    private String errorMessege;

    public AuthResponse() {
    }

    public AuthResponse(String jwtToken, String errorMessege) {
        this.jwtToken = jwtToken;
        this.errorMessege = errorMessege;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getErrorMessege() {
        return errorMessege;
    }

    public void setErrorMessege(String errorMessege) {
        this.errorMessege = errorMessege;
    }
}
