package com.alims.londontech.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * ==============================================================
 * A Common response with payload/status used as return results
 * of Controller/Service methods
 * ==============================================================
 **/

public class CommonResponse {

    private List<Object> payload = null;
    private List<String> errorMessages = new ArrayList<>();
    private boolean status = false;

    public List<Object> getPayload() {
        return payload;
    }

    public void setPayload(List<Object> payload) {
        this.payload = payload;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
