package com.hst.spv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.hst.spv.R;
import com.hst.spv.adapter.SpvAdapter;

public class YourSpvActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_spv);

        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_lay);
        ViewPager viewPager = (ViewPager)findViewById(R.id.view_host);

        tabLayout.addTab(tabLayout.newTab().setText("Biography"));
        tabLayout.addTab(tabLayout.newTab().setText("Position Held"));
        tabLayout.addTab(tabLayout.newTab().setText("Awards"));
        tabLayout.addTab(tabLayout.newTab().setText("Notable Works"));

        tabLayout.setTabTextColors(getResources().getColor(R.color.text_grey), getResources().getColor(R.color.tab_indicator));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_START);

        SpvAdapter spvAdapter = new SpvAdapter(getSupportFragmentManager());
        viewPager.setAdapter(spvAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getCurrentItem();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == back){

            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }
}