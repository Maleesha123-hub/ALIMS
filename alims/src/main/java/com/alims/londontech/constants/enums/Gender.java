package com.alims.londontech.constants.enums;

import com.alims.londontech.utils.CommonValidation;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender findByFirstLetter(String abbr) {
        if (!CommonValidation.stringNullValidation(abbr)) {
            for (Gender gender : values()) {
                if (gender.toString().charAt(0) == abbr.charAt(0)) {
                    return gender;
                }
            }
        }
        return null;
    }
}
