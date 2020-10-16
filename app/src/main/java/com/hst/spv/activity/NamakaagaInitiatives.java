package com.hst.spv.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.hst.spv.R;
import com.hst.spv.adapter.NamakaagaAdapter;
import com.hst.spv.adapter.SpvAdapter;

public class NamakaagaInitiatives extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namakaaga_initiatives);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_lay);
        ViewPager viewPager = (ViewPager)findViewById(R.id.view_host);

        tabLayout.addTab(tabLayout.newTab().setText("Namakaaga Ullatchi"));
        tabLayout.addTab(tabLayout.newTab().setText("Amma IAS Academy"));
        tabLayout.addTab(tabLayout.newTab().setText("Nallaramm Trust"));

        tabLayout.setTabTextColors(getResources().getColor(R.color.text_grey), getResources().getColor(R.color.tab_indicator));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_START);

        NamakaagaAdapter namakaagaAdapter = new NamakaagaAdapter(getSupportFragmentManager());
        viewPager.setAdapter(namakaagaAdapter);

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