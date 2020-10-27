package com.hst.spv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

/**
 * Created by Admin on 15-09-2020.
 */

public class PreferenceStorage {

    /*To check welcome screen to launch*/
    public static void setFirstTimeLaunch(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SPVConstants.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public static boolean isFirstTimeLaunch(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(SPVConstants.IS_FIRST_TIME_LAUNCH, true);
    }
    /*End*/

    /*To save mobile IMEI number */
    public static void saveIMEI(Context context, String imei) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_IMEI, imei);
        editor.apply();
    }

    public static String getIMEI(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SPVConstants.KEY_IMEI, "");
    }
    /*End*/

    // UserId
    public static void saveUserId(Context context, String userId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_USER_ID, userId);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String userId;
        userId = sharedPreferences.getString(SPVConstants.KEY_USER_ID, "");
        return userId;
    }
    /*End*/

    /*To store full name*/
    public static void saveFullName(Context context, String type) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.PARAMS_FULL_NAME, type);
        editor.apply();
    }

    public static String getFullName(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        String fullName;
        fullName = sharedPreferences.getString(SPVConstants.PARAMS_FULL_NAME, "");
        return fullName;
    }
    /*End*/

    /* User Image*/
    public static void saveUserPicture(Context context, String userPicture) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_USER_IMAGE, userPicture);
        editor.apply();
    }

    public static String getUserPicture(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        String userPicture;
        userPicture = sharedPreferences.getString(SPVConstants.KEY_USER_IMAGE, "");
        return userPicture;
    }
    /*End*/


    /*To save FCM key locally*/
    public static void saveFCM(Context context, String gcmId) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_FCM_ID, gcmId);
        editor.apply();
    }

    public static String getFCM(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SPVConstants.KEY_FCM_ID, "");
    }
    /*End*/


    /*To store language*/
    public static void saveLang(Context context, String lang) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_LANGUAGE, lang);
        editor.apply();
    }

    public static String getLang(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        String lang;
        lang = sharedPreferences.getString(SPVConstants.KEY_LANGUAGE, "");
        return lang;
    }
    /*End*/

    /*User Name*/
    public static void saveUserName(Context context, String type) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_USERNAME, type);
        editor.apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        String fullName;
        fullName = sharedPreferences.getString(SPVConstants.KEY_USERNAME, "");
        return fullName;
    }
    /*End*/

    /*Mail Id*/
    public static void saveEmailId(Context context, String type) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_USER_EMAIL_ID, type);
        editor.apply();
    }

    public static String getEmailId(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        String emailId;
        emailId = sharedPreferences.getString(SPVConstants.KEY_USER_EMAIL_ID, "");
        return emailId;
    }

    /*To store user birthday*/
    public static void saveUserBirthday(Context context, String data) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_USER_BIRTHDAY, data);
        editor.apply();
    }

    public static String getUserBirthday(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        String birthDay;
        birthDay = sharedPreferences.getString(SPVConstants.KEY_USER_BIRTHDAY, "");
        return birthDay;
    }
    /*End*/

    /*To store user gender*/
    public static void saveUserGender(Context context, String data) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPVConstants.KEY_USER_GENDER, data);
        editor.apply();
    }

    public static String getUserGender(Context context) {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        String gender;
        gender = sharedPreferences.getString(SPVConstants.KEY_USER_GENDER, "");

        return gender;
    }
    /*End*/

}
