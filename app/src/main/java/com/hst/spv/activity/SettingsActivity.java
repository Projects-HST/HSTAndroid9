package com.hst.spv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.hst.spv.R;
import com.hst.spv.helper.AlertDialogHelper;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.servicehelpers.ServiceHelper;
import com.hst.spv.serviceinterfaces.IServiceListener;
import com.hst.spv.utils.CommonUtils;
import com.hst.spv.utils.SPVConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity implements DialogClickListener, View.OnClickListener, IServiceListener {

    private static final String TAG = SettingsActivity.class.getName();
    private SwitchCompat pushNotification;
    private CheckBox subscribe;
    private String resString;
    private boolean notifyOn = false;
    TextView ed_profile;
    ImageView back;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pushNotification = (SwitchCompat) findViewById(R.id.switchNote);
        pushNotification.setOnClickListener(this);
        subscribe = (CheckBox) findViewById(R.id.check);
        subscribe.setOnClickListener(this);
        ed_profile = (TextView) findViewById(R.id.img_title_1);
        ed_profile.setOnClickListener(this);

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                finish();
            }
        });

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);

        pushNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                getPushNotification();
            }
        });
    }

    private void getPushNotification() {

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            resString = "push_notification";

            JSONObject jsonObject = new JSONObject();
            boolean state = pushNotification.isChecked();
            String status = "";

            if (state){

                status = "1";
            }
            else {
                status = "0";
            }

            try {

                jsonObject.put(SPVConstants.KEY_USER_ID, "");
                jsonObject.put(SPVConstants.KEY_STATUS, status);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = SPVConstants.BUILD_URL + SPVConstants.PUSH_NOTIFICATION;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }

    private void getNewsSubscription() {

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {

            resString = "news_subscription";
            JSONObject jsonObject = new JSONObject();
            boolean state = subscribe.isChecked();
            String status = "";

            if (state){

                status = "1";
            }
            else {
                status = "0";
            }

            try {

                jsonObject.put(SPVConstants.KEY_USER_ID, "");
                jsonObject.put(SPVConstants.KEY_STATUS, status);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = SPVConstants.BUILD_URL + SPVConstants.NEWS_SUBSCRIBE;
            serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
        } else {
            AlertDialogHelper.showSimpleAlertDialog(this, getString(R.string.error_no_net));
        }
    }


    private boolean validateResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(SPVConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (status.equalsIgnoreCase("Success")) {
                        signInSuccess = true;
                    } else {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
//                        AlertDialogHelper.showSimpleAlertDialog(getActivity(), msg);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onClick(View v) {

        if (v == subscribe){

            if(!notifyOn){

                notifyOn = true;
                getNewsSubscription();
            }
            else {

                notifyOn = false;
                getNewsSubscription();
            }
        }

        if (v == ed_profile){

            Intent homeIntent = new Intent(this, ProfileActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }

    @Override
    public void onResponse(JSONObject response) {

        try {

            if (validateResponse(response)) {

                if (resString.equalsIgnoreCase("push_notification")) {

                    Log.d(TAG, response.toString());
                    Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }

                if (resString.equalsIgnoreCase("news_subscription")) {

                    Log.d(TAG, response.toString());
                    Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (JSONException e) {

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
}