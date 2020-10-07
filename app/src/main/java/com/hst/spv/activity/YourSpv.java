package com.hst.spv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hst.spv.R;
import com.hst.spv.adapter.SpvAdapter;

public class YourSpv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_spv);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_lay);
        tabLayout.addTab(tabLayout.newTab().setText("Biography"));
        tabLayout.addTab(tabLayout.newTab().setText("Position Held"));
        tabLayout.addTab(tabLayout.newTab().setText("Awards"));
        tabLayout.addTab(tabLayout.newTab().setText("Notable Works"));

        tabLayout.setTabTextColors(getResources().getColor(R.color.text_grey), getResources().getColor(R.color.tab_indicator));

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.view_host);

        final SpvAdapter spvAdapter = new SpvAdapter(getSupportFragmentManager());

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
}