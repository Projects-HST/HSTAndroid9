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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hst.spv.R;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
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

public class ProfileActivity extends AppCompatActivity implements DialogClickListener, IServiceListener, View.OnClickListener {

    private static final String TAG = ProfileActivity.class.getName();
    ImageView prof_pic;
    EditText prof_name, prof_password, prof_mail, dob;
    RadioGroup prof_gen;
    RadioButton male, female, others;
    Button save;

    private String gender;
    private SimpleDateFormat mDateFormatter;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prof_pic = (ImageView) findViewById(R.id.profile_img);
        prof_name = (EditText) findViewById(R.id.prof_name);
        prof_password = (EditText) findViewById(R.id.prof_password);
        prof_mail = (EditText) findViewById(R.id.prof_mail);
        dob = (EditText) findViewById(R.id.prof_dob);
        prof_gen = (RadioGroup)findViewById(R.id.prof_gen);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        others = (RadioButton) findViewById(R.id.other);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prof_gen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.male){
                    male.isChecked();
                }
                else if (checkedId == R.id.female){
                    female.isChecked();
                }
                else if (checkedId == R.id.other){
                    others.isChecked();
                }
            }
        });

        mDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        dialogHelper = new ProgressDialogHelper(this);
    }

    private boolean validateFields() {

        if (!SPVValidator.checkNullString(this.prof_name.getText().toString().trim())) {

            prof_name.setError(getString(R.string.error_entry));
            reqFocus(prof_name);
            return false;
        }
        else if (!SPVValidator.checkNullString(prof_password.getText().toString().trim())) {

            prof_password.setError(getString(R.string.error_entry));
            reqFocus(prof_password);
            return false;
        }
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

    private void saveUserDetails(){

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            if (validateFields()) {

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
        }
        else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }

    }

    @Override
    public void onClick(View v) {

        if (v == save) {

            saveUserDetails();
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

        if (validateResponse(response)){
            try{
                JSONArray userDetails = response.getJSONArray("user_details");
                JSONObject object = userDetails.getJSONObject(0);

                Log.d(TAG, object.toString());

                String name = prof_name.getText().toString();
                String password = prof_password.getText().toString().trim();
                String mail = prof_mail.getText().toString().trim();
                String gender = "";

                for (int i=0; i<userDetails.length(); i++){

                    name = object.getString("full_name");
                    mail = object.getString("email_id");
                    gender = object.getString("gender");
                }

                Intent saveIntent = new Intent(this, SettingsActivity.class);
                saveIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(saveIntent);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

        dialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }
}