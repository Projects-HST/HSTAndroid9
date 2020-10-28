package com.hst.spv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hst.spv.R;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
import com.hst.spv.utils.PreferenceStorage;
import com.hst.spv.utils.SPVConstants;
import com.hst.spv.utils.SPVValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements DialogClickListener, IServiceListener, View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = ProfileActivity.class.getName();
    ImageView prof_pic;
    EditText prof_name, prof_password, prof_mail, prof_dob;
    RadioGroup prof_gen;
    RadioButton male, female, others;
    Button save;

    private SimpleDateFormat mDateFormatter;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;
    private DatePickerDialog mDatePicker;

    private RadioButton radioButton;
    private int radioId;
    private String resString;
    String fullName ="";
    String mailId = "";
    String birthDay = "";
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prof_pic = (ImageView) findViewById(R.id.profile_img);
        prof_name = (EditText) findViewById(R.id.prof_name);
        prof_password = (EditText) findViewById(R.id.prof_password);
        prof_mail = (EditText) findViewById(R.id.prof_mail);
        prof_dob = (EditText) findViewById(R.id.prof_dob);
        prof_gen = (RadioGroup)findViewById(R.id.prof_gen);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        others = (RadioButton) findViewById(R.id.other);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });

//        String birthdayval = PreferenceStorage.getUserBirthday(this);
//
//        if (birthdayval != null) {
//
//            prof_dob.setText(birthdayval);
//        }

        prof_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "birthday widget selected");
                showBirthdayDate();
            }
        });

        mDateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        dialogHelper = new ProgressDialogHelper(this);

        showUserDetails();
    }

    private boolean validateFields() {

        if (!SPVValidator.checkNullString(this.prof_name.getText().toString().trim())) {

            prof_name.setError(getString(R.string.error_entry));
            reqFocus(prof_name);
            return false;
        }
//        else if (!SPVValidator.checkNullString(prof_password.getText().toString().trim())) {
//
//            prof_password.setError(getString(R.string.error_entry));
//            reqFocus(prof_password);
//            return false;
//        }
        else if (prof_mail.getText().length() > 0) {

            if (!SPVValidator.isEmailValid(this.prof_mail.getText().toString().trim())) {
                prof_mail.setError(getString(R.string.error_entry));
                reqFocus(prof_mail);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void reqFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void saveProfileData(){

        resString = "saveProfile";

        fullName = prof_name.getText().toString().trim();
        PreferenceStorage.saveUserName(this, fullName);
        mailId = prof_mail.getText().toString().trim();
        PreferenceStorage.saveEmailId(this, mailId);
        birthDay = prof_dob.getText().toString().trim();
        PreferenceStorage.saveUserBirthday(this, birthDay);
        radioId = prof_gen.getCheckedRadioButtonId();
        radioButton = prof_gen.findViewById(radioId);
        gender = radioButton.getText().toString();
        PreferenceStorage.saveUserGender(this, gender);

            if (male != null) {
                male.isChecked();
            }
            else if (female != null){
                female.isChecked();
            }
            else if (others != null) {
                others.isChecked();
            }

        String newFormat = "";
        if ((birthDay != null) && (birthDay.equals(""))) {

            String date = prof_dob.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            Date testDate = null;
            try {
                testDate = sdf.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
            newFormat = formatter.format(testDate);
            System.out.println(".....Date..." + newFormat);
        }

        if (validateFields()) {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(SPVConstants.KEY_USER_ID, "1");
                jsonObject.put(SPVConstants.KEY_USERNAME, fullName);
                jsonObject.put(SPVConstants.KEY_PHONE_NO, "9994449999");
                jsonObject.put(SPVConstants.KEY_USER_EMAIL_ID, mailId);
                jsonObject.put(SPVConstants.KEY_USER_GENDER, gender);
                jsonObject.put(SPVConstants.KEY_USER_BIRTHDAY, birthDay);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serviceUrl = SPVConstants.BUILD_URL + SPVConstants.PROFILE_UPDATE;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), serviceUrl);
        }
    }

    private void showUserDetails(){

        resString = "userDetails";

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(SPVConstants.KEY_USER_ID, "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialogHelper.showProgressDialog(getString(R.string.progress_bar));
            String url = SPVConstants.BUILD_URL + SPVConstants.EDIT_URL;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        }
        else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    @Override
    public void onClick(View v) {

        if (v == save) {

            saveProfileData();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        return true;
    }

    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(SPVConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);
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

        dialogHelper.hideProgressDialog();
        try{
            if (validateResponse(response)) {

                if (resString.equalsIgnoreCase("userDetails")) {

                    JSONArray userDetails = response.getJSONArray("user_details");
                    JSONObject object = userDetails.getJSONObject(0);

                    Log.d(TAG, object.toString());

                    fullName = PreferenceStorage.getUserName(this);
                    mailId = PreferenceStorage.getEmailId(this);
                    birthDay = PreferenceStorage.getUserBirthday(this);
                    gender = PreferenceStorage.getUserGender(this);

                    for (int i = 0; i < userDetails.length(); i++) {

                        fullName = object.getString("full_name");
                        if (fullName != null) {
                            prof_name.setText(fullName);
                        }
                        mailId = object.getString("email_id");
                        if (mailId != null) {
                            prof_mail.setText(mailId);
                        }
                        birthDay = object.getString("dob");
                        if (birthDay != null) {
                            prof_dob.setText(birthDay);
                        }
                        gender = object.getString("gender");

                        if (gender != null) {

                            for (int i1 = 0; i1 < prof_gen.getChildCount(); i1++) {

                                radioButton = (RadioButton) prof_gen.getChildAt(i1);
                                radioButton.isChecked();

                                if (male.getText().toString().equals(gender)) {

                                    male.setChecked(true);

                                } else if (female.getText().toString().equals(gender)) {

                                    female.setChecked(true);

                                }
                                else if (others.getText().toString().equals(gender)) {

                                    others.setChecked(true);
                                }
                            }
                        }
                    }
                }

                if (resString.equalsIgnoreCase("saveProfile")) {

                    Log.d(TAG, response.toString());
                    Toast.makeText(getApplicationContext(), response.getString("msg"), Toast.LENGTH_SHORT).show();

                    Intent saveIntent = new Intent(this, SettingsActivity.class);
                    saveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(saveIntent);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onError(String error) {

        dialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    private void showBirthdayDate() {

        Log.d(TAG, "Show the birthday date");
        Calendar newCalendar = Calendar.getInstance();
        String currentDate = prof_dob.getText().toString();
        Log.d(TAG, "current date is" + currentDate);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        int year = newCalendar.get(Calendar.YEAR);
        if ((currentDate != null) && !(currentDate.isEmpty())) {
            //extract the date/month and year
            try {
                Date startDate = mDateFormatter.parse(currentDate);
                Calendar newDate = Calendar.getInstance();

                newDate.setTime(startDate);
                month = newDate.get(Calendar.MONTH);
                day = newDate.get(Calendar.DAY_OF_MONTH);
                year = newDate.get(Calendar.YEAR);
                Log.d(TAG, "month" + month + "day" + day + "year" + year);

            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                mDatePicker = new DatePickerDialog(this, R.style.datePickerTheme, this, year, month, day);
                mDatePicker.show();
            }
        } else {
            Log.d(TAG, "show default date");

            mDatePicker = new DatePickerDialog(this, R.style.datePickerTheme, this, year, month, day);
            mDatePicker.show();
        }
    }


    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, dayOfMonth);
        prof_dob.setText(mDateFormatter.format(newDate.getTime()));
    }
}