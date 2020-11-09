package com.hst.spv.utils;

/**
 * Created by Admin on 15-09-2020.
 */

public class SPVConstants {

    // URLS
    //BASE URL
    public static final String BASE_URL = "https://happysanz.in/";

    //Development Mode
    //development
//    public static final String JOINT_URL = "development/";
    //uat
//    public static final String JOINT_URL = "uat/";
    //live
    public static final String JOINT_URL = "spveluapp/";
    //
//    //BUILD URL
    public static final String BUILD_URL = BASE_URL + JOINT_URL + "apiuser/";


    //SPV PERSONAL
    public static final String PERSONAL_URL =  "spv_personal";

    //SPV POLITICAL
    public static final String POLITICAL_URL = "spv_political";

    //SPV POSITION HELD
    public static final String POSITIONS_URL = "spv_positionheld";

    //SPV AWARDS
    public static final String AWARDS_URL = "spv_awards";

    //SPV NOTABLE WORKS
    public static final String NOTABLE_URL = "spv_notable";

    //NAMAKKAGA ULLATCHI
    public static final String NAMAKAAGA_URL = "spv_namakkaga";
    public static final String BANNER_IMAGES = "assets/namakkaga/";

    //AMMA IAS ACADEMY
    public static final String ACADEMY_URL = "spv_ias_academy";
    public static final String ASSETS_URL_ACADEMY = "assets/ias_academy/";

    //NEWS
    public static final String GET_NEWSFEED = "all_newsfeeds";
    public static final String ASSETS_URL_NEWSFEED = "assets/news_feed/";

    //NEWS
    public static final String GET_ALL_NEWS = "all_news";
    public static final String GET_NEWSFEED_DETAIL = "newsfeed_details/";
    public static final String GET_NEWS_CATEGORY = "newsfeeds_categoryid";

    //NEWS
    public static final String GET_ALL_EVENTS = "all_events";

    //SUBSCRIPTION
    public static final String NEWS_SUBSCRIBE = "subscription_update";
    public static final String PUSH_NOTIFICATION = "notification_update";

    //EditProfile
    public static final String EDIT_BASE_URL = "profile_details/";
    public static final String EDIT_URL = EDIT_BASE_URL + "assets/users/";

    //ProfileUpdate
    public static final String PROFILE_UPDATE = "profile_update";

    //ProfilePicUpload
    public static final String UPLOAD_IMAGE = "profilepic_update/1/";

    //service params
    public static String PARAM_MESSAGE = "msg";
    public static String PARAM_MESSAGE_ENG = "msg_en";
    public static String PARAM_MESSAGE_TAMIL = "msg_ta";

    //user details
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PHONE_NO = "phone_number";

    //update_status
    public static final String KEY_STATUS = "status";

    // Alert Dialog Constants
    public static String ALERT_DIALOG_TITLE = "alertDialogTitle";
    public static String ALERT_DIALOG_MESSAGE = "alertDialogMessage";
    public static String ALERT_DIALOG_TAG = "alertDialogTag";
    public static String ALERT_DIALOG_POS_BUTTON = "alert_dialog_pos_button";
    public static String ALERT_DIALOG_NEG_BUTTON = "alert_dialog_neg_button";

    //SP first time launch
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    //SP IMEI
    public static final String KEY_IMEI = "imei_code";

    //SP FCM ID
    public static final String KEY_FCM_ID = "fcm_id";

    //SAVE PARAMS
    public static final String PARAMS_FULL_NAME = "name";
    public static final String KEY_USER_IMAGE = "profile_pic";

    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USERNAME = "full_name";
    public static final String KEY_USER_BIRTHDAY = "dob";
    public static final String KEY_USER_EMAIL_ID = "email_id";

    public static final String KEY_LANGUAGE = "language";

    //    Newsfeed Params
    public static final String KEY_NEWSFEED_ID = "nf_category_id";
    public static final String NEWSFEED_ID = "newsfeed_id";
    public static final String KEY_OFFSET = "offset";
    public static final String KEY_ROWCOUNT = "rowcount";
    public static final String KEY_SEARCH_KEYWORD = "search_text";

}
