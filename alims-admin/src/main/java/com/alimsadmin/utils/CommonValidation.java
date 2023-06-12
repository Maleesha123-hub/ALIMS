package com.alimsadmin.utils;

import com.alimsadmin.constants.CommonConstants;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonValidation {

    public static boolean stringNullValidation(String inputString) {
        return inputString == null || inputString.isEmpty();
    }

    public static boolean stringNullAndDefaultListSelectionValidation(String inputString) {
        return inputString == null || inputString.isEmpty() || inputString.equals("-1");
    }

    public static String sinceDateValidation(String since) {
        if (since.trim().equals("")) {
        } else {
            String sinceDate = since.trim();
            if (sinceDate.length() != 10) {
            } else {
                int delimeterCount = 0;
                char ch = '-';
                for (int x = 0; x < sinceDate.length(); x++) {
                    if (sinceDate.charAt(x) == ch) {
                        delimeterCount++;
                    }
                }
                if (delimeterCount != 2) {
                }
            }
        }
        return "";
    }

    public static boolean integerIsNull(Integer intVal) {
        return intVal == null;
    }

    public static boolean isNegative(Integer value) {
        return value < 1;
    }

    /**
     * this method use to validate url
     *
     * @param urlStr
     * @return
     */
    public static boolean urlValidation(String urlStr) {
        Pattern p = Pattern.compile(CommonConstants.REGEX_URL_VALIDATION);
        Matcher matcher = p.matcher(urlStr);
        return matcher.find();
    }

    /**
     * this method use to email url
     *
     * @param emailStr
     * @return
     */
    public static boolean emailValidation(String emailStr) {
        Pattern p = Pattern.compile(CommonConstants.REGEX_EMAIL_VALIDATION);
        Matcher matcher = p.matcher(emailStr);
        return matcher.find();
    }

    /**
     * this method use to email url
     *
     * @param nicStr
     * @return
     */
    public static boolean nicValidation(String nicStr) {
        Pattern p = Pattern.compile(CommonConstants.REGEX_NIC_VALIDATION);
        Matcher matcher = p.matcher(nicStr);
        return matcher.find();
    }

    /**
     * this method use to validate input numbers
     *
     * @param strNum
     * @return
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isValidDate(String fromDate, String toDate) {
        LocalDateTime firstDate = null;
        LocalDateTime secondDate = null;
        if (!CommonValidation.stringNullValidation(fromDate)) {
            firstDate = DateTimeUtil.getFormattedDateTime(fromDate);
        } else {
            firstDate = DateTimeUtil.getSriLankaTime();
        }
        if (!CommonValidation.stringNullValidation(toDate)) {
            secondDate = DateTimeUtil.getFormattedDateTime(toDate);
        }
        return secondDate.isAfter(firstDate);
    }

}
