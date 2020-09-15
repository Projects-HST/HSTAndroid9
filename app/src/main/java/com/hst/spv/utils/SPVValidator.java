package com.hst.spv.utils;

/**
 * Created by Admin on 15-09-2020.
 */

public class SPVValidator {

    public static boolean checkNullString(String value) {
        if (value == null)
            return false;
        else
            return value.trim().length() > 0;
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkStringMinLength(int minValue, String value) {
        if (value == null)
            return false;
        return value.trim().length() >= minValue;
    }

    public static boolean checkMobileNumLength(String mobile) {
        return mobile.length() >= 10 && mobile.length() <= 13;
    }
}
