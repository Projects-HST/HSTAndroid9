package com.hst.spv.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hst.spv.R;
import com.hst.spv.bean.database.SQLiteHelper;
import com.hst.spv.customview.CustomOtpEditText;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
import com.hst.spv.utils.PreferenceStorage;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.util.Log.d;

public class NumberVerificationActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = NumberVerificationActivity.class.getName();

    private CustomOtpEditText otpEditText;
    private TextView tvResendOTP, tvCountDown;
    private Button btnConfirm;
    private String mobileNo;
    private String checkVerify;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;
    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    SQLiteHelper database;
    private SmsBrReceiver smsReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);
        database = new SQLiteHelper(getApplicationContext());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mobileNo = PreferenceStorage.getMobileNo(getApplicationContext());
        otpEditText = (CustomOtpEditText) findViewById(R.id.otp_view);
        tvResendOTP = (TextView) findViewById(R.id.resend);
        tvResendOTP.setOnClickListener(this);
        tvCountDown = (TextView) findViewById(R.id.countdown);
        btnConfirm = (Button) findViewById(R.id.verify);
        btnConfirm.setOnClickListener(this);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                Toast.makeText(NumberVerificationActivity.this, "Listening for otp...", Toast.LENGTH_SHORT).show();
                IntentFilter filter = new IntentFilter();
                filter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
                if (smsReceiver == null) {
                    smsReceiver = new NumberVerificationActivity.SmsBrReceiver();
                }
                getApplicationContext().registerReceiver(smsReceiver, filter);
//                            startActivity(homeIntent);
//                            finish();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Toast.makeText(NumberVerificationActivity.this, "Failed listening for otp...", Toast.LENGTH_SHORT).show();
            }
        });

        countDownTimers();

    }

    void countDownTimers() {
        new CountDownTimer(30 * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvResendOTP.setVisibility(View.GONE);
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tvCountDown.setText("Resend in " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds) + " seconds");
            }

            public void onFinish() {
                tvCountDown.setText("Try again...");
                tvCountDown.setVisibility(View.GONE);
                tvResendOTP.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    @Override
    public void onClick(View v) {
        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            if (v == tvResendOTP) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.resend_otp);
                alertDialogBuilder.setMessage(getString(R.string.mobile_number) + getString(R.string.mobile_number_tag) + PreferenceStorage.getMobileNo(getApplicationContext()));
                alertDialogBuilder.setPositiveButton(R.string.alert_button_ok,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                checkVerify = "Resend";
                                countDownTimers();
                                tvCountDown.setVisibility(View.VISIBLE);
                                JSONObject jsonObject = new JSONObject();
                                try {

                                    jsonObject.put(SPVConstants.KEY_MOBILE, PreferenceStorage.getMobileNo(getApplicationContext()));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                                String url = SPVConstants.BUILD_URL + SPVConstants.RESEND_OTP;
                                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

                            }
                        });
                alertDialogBuilder.setNegativeButton(R.string.alert_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
//                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialogBuilder.show();

            } else if (v == btnConfirm) {
                if (otpEditText.hasValidOTP()) {
                    checkVerify = "Confirm";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(SPVConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
                        jsonObject.put(SPVConstants.KEY_MOBILE, PreferenceStorage.getMobileNo(getApplicationContext()));
                        jsonObject.put(SPVConstants.OTP, otpEditText.getOTP());
                        jsonObject.put(SPVConstants.DEVICE_TOKEN, PreferenceStorage.getFCM(getApplicationContext()));
                        jsonObject.put(SPVConstants.MOBILE_TYPE, "1");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = SPVConstants.BUILD_URL + SPVConstants.LOGIN;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
                } else {
                    if (PreferenceStorage.getLang(this).equalsIgnoreCase("tamil")) {
                        AlertDialogHelper.showSimpleAlertDialog(this, "தவறான கடவுச்சொல்!");
                    } else {
                        AlertDialogHelper.showSimpleAlertDialog(this, "Invalid OTP");
                    }
                }

            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(SPVConstants.PARAM_MESSAGE);
//                String msg_en = response.getString(SkilExConstants.PARAM_MESSAGE_ENG);
//                String msg_ta = response.getString(SkilExConstants.PARAM_MESSAGE_TAMIL);
                d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
//                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        try {
            if (validateSignInResponse(response)) {
                if (checkVerify.equalsIgnoreCase("Resend")) {

                    Toast.makeText(getApplicationContext(), "OTP resent successfully", Toast.LENGTH_SHORT).show();

                } else if (checkVerify.equalsIgnoreCase("Confirm") || checkVerify.equalsIgnoreCase("verified")) {
                    PreferenceStorage.setFirstTimeLaunch(getApplicationContext(), false);
                    database.app_info_check_insert("Y");
                    Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                    JSONObject data = response.getJSONObject("userData");

                    String userId = data.getString("user_id");
                    String fullName = data.getString("full_name");
                    String phoneNo = data.getString("phone_number");
                    String profilePic = data.getString("profile_pic");
                    String lang_id = data.getString("language_id");

                    PreferenceStorage.saveUserId(getApplicationContext(), userId);
                    PreferenceStorage.saveUserName(getApplicationContext(), fullName);
                    PreferenceStorage.savePhoneNumber(getApplicationContext(), phoneNo);
                    PreferenceStorage.saveUserPicture(getApplicationContext(), profilePic);
                    PreferenceStorage.saveLanguageId(getApplicationContext(), lang_id);

                    Intent homeInt = new Intent(this, MainActivity.class);
                    startActivity(homeInt);
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    public class SmsBrReceiver extends BroadcastReceiver {

        private String parseCode(String message) {
            Pattern p = Pattern.compile("\\b\\d{4}\\b");
            Matcher m = p.matcher(message);
            String code = "";
            while (m.find()) {
                code = m.group(0);
            }
            return code;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get SMS message contents
                        String smsMessage = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        // Extract one-time code from the message and complete verification
                        // by sending the code back to your server.
                        Log.d(TAG, "Retrieved sms code: " + smsMessage);
                        if (smsMessage != null) {
                            String sms = parseCode(smsMessage);
                            verifyMessage(sms);
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Waiting for SMS timed out (5 minutes)
                        // Handle the error ...
                        break;
                }
            }
        }

        public void verifyMessage(String otp) {
            checkVerify = "verified";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(SPVConstants.KEY_USER_ID, PreferenceStorage.getUserId(getApplicationContext()));
                jsonObject.put(SPVConstants.KEY_MOBILE, PreferenceStorage.getMobileNo(getApplicationContext()));
                jsonObject.put(SPVConstants.OTP, otp);
                jsonObject.put(SPVConstants.DEVICE_TOKEN, PreferenceStorage.getFCM(getApplicationContext()));
                jsonObject.put(SPVConstants.MOBILE_TYPE, "1");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
            String url = SPVConstants.BUILD_URL + SPVConstants.LOGIN;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        }
    }
}