package com.alims.londontech.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * ==============================================================
 * Utility methods for date/time calculations
 * ==============================================================
 **/

public class DateTimeUtil {

    public static final String TIME_ZONE = "GMT+05:30";
    public static DateTimeFormatter FORMATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter SINCE_FORMATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter TIME_FORMATE = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static DateTimeFormatter TIME_FORMAT_FOE_ADD_DROP = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public static LocalDateTime getFormattedDateTime(String dateTime) {
        LocalDateTime localDateTime = null;
        try {
            localDateTime = LocalDateTime.parse(dateTime, FORMATE);
        } catch (Exception e) {
        }
        return (localDateTime);
    }

    public static LocalTime getFormattedTime(String time) {
        return LocalTime.parse(time, TIME_FORMATE);
    }

    public static String getFormattedDateTime(LocalDateTime dateTime) {
        return (dateTime == null ? null : dateTime.format(FORMATE));
    }

    public static String getFormattedDate(LocalDateTime dateTime) {
        return (dateTime == null ? null : dateTime.format(FORMAT_DATE));
    }

    public static String getTimeDiffrenceInSec(String startTime, String endTime) {
        LocalDateTime localStartTime = getFormattedDateTime(startTime);
        LocalDateTime localEndTime = getFormattedDateTime(endTime);
        if (localStartTime == null || localEndTime == null) return null;
        Duration diff = Duration.between(localStartTime, localEndTime);
        long durationInSec = diff.getSeconds();
        return String.valueOf(Math.abs(durationInSec));
    }

    public static String getDateTimeFromISODateTime(String iosDateTime) {
        String inputDate = iosDateTime.substring(0, 16);
        String inputDateModified = inputDate.replace("T", " ");
        return getFormattedDateTime(LocalDateTime.parse(inputDateModified, FORMATE));
    }

    public static String getZoneFromISODateTime(String iosDateTime) {
        return iosDateTime.substring(19);
    }

    public static String getFormattedSinceDate(String since) {
        return since.concat(" ").concat("00:00:00");
    }

    public static String getLocalTimeByUTC(String utcTime, String timeZone) {
        String localDatetime = null;
        if (utcTime == null) {
            return localDatetime;
        }
        if (utcTime.contains(".")) {
            utcTime = utcTime.substring(0, utcTime.lastIndexOf("."));
        }
        LocalDateTime formattedTime = LocalDateTime.parse(utcTime, SINCE_FORMATE);
        if (!timeZone.equalsIgnoreCase("UTC")) {
            LocalDateTime now = LocalDateTime.now();
            ZoneId zone = ZoneId.of(timeZone);
            ZoneOffset zoneOffSet = zone.getRules().getOffset(now);
            String zoneOffSetString = zoneOffSet.toString();
            String sign = zoneOffSetString.substring(0, 1);
            long hours = Long.parseLong(zoneOffSetString.substring(1, 3));
            long minutes = Long.parseLong(zoneOffSetString.substring(4));
            if (sign.equalsIgnoreCase("+")) {
                formattedTime = formattedTime.plusHours(hours);
                formattedTime = formattedTime.plusMinutes(minutes);
            } else {
                formattedTime = formattedTime.minusHours(hours);
                formattedTime = formattedTime.minusMinutes(minutes);
            }
        }
        localDatetime = formattedTime.format(SINCE_FORMATE);
        return localDatetime;
    }

    public static LocalDateTime getSriLankaTime() {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of(TIME_ZONE));
        String dateTime = FORMATE.format(localDateTime);
        return getFormattedDateTime(dateTime);
    }
}
