package com.hst.spv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hst.spv.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView pushNotification;
    private ImageView subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pushNotification = (ImageView)findViewById(R.id.notification);
        pushNotification.setOnClickListener(this);
        subscribe = (ImageView)findViewById(R.id.news);
        subscribe.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == pushNotification){

        }
        if (v == subscribe){

        }
    }
}