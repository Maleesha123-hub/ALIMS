package com.alimsadmin.constants.enums;

public enum UserTypes {
    USER_NONE("0"),
    USER_VISITOR("1"),
    USER_CLINIC("2"),
    USER_ADMIN("3"),
    USER_VG("4"),
    USER_VENDOR("5"),
    USER_PARTNER("6"),
    USER_PROXY("7"),
    USER_KIOSK("8"),
    USER_ANY("9");

    private String userType;

    UserTypes(String userType) {
        this.userType = userType;
    }

    public static UserTypes getUserTypeString(int val) {
        switch (val) {
            case 0:
                return UserTypes.USER_NONE;
            case 1:
                return UserTypes.USER_VISITOR;
            case 2:
                return UserTypes.USER_CLINIC;
            case 3:
                return UserTypes.USER_ADMIN;
            case 4:
                return UserTypes.USER_VG;
            case 5:
                return UserTypes.USER_VENDOR;
            case 6:
                return UserTypes.USER_PARTNER;
            case 7:
                return UserTypes.USER_PROXY;
            case 8:
                return UserTypes.USER_KIOSK;
            case 9:
                return UserTypes.USER_ANY;
            default:
                return null;
        }
    }

    public int getUserTypeInt() {
        return Integer.parseInt(userType);
    }
}
