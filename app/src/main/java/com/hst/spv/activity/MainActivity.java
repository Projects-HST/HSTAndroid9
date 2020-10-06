package com.hst.spv.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hst.spv.R;
import com.hst.spv.interfaces.DialogClickListener;
import com.hst.spv.serviceinterfaces.IServiceListener;

import org.json.JSONObject;

/**
 * Created by Admin on 14-09-2020.
 */

public class MainActivity extends AppCompatActivity implements DialogClickListener, IServiceListener {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(String error) {

    }
}