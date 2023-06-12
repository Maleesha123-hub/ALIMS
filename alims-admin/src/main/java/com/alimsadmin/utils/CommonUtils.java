package com.alimsadmin.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * ==============================================================
 * Common utility methods used in Services
 * ==============================================================
 **/

public class CommonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    public static List objectMapper(List<?> containerData, TypeReference reference) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(containerData, reference);
    }

    public static SearchParams objectMapper(String jsonData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonData, SearchParams.class);
        } catch (Exception e) {
            LOGGER.warn("/**************** Exception in CommonUtils -> objectMapper()" + e);
        }
        return null;
    }

    public static String getUniqueCode() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

}
