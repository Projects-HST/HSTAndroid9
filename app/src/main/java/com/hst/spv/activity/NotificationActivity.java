package com.hst.spv.activity;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hst.spv.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener {
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}
