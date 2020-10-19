package com.hst.spv.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hst.spv.fragment.NewsAllFragment;
import com.hst.spv.fragment.NewsLocalFragment;
import com.hst.spv.fragment.NewsStateFragment;

public class NewsTabAdapter extends FragmentStatePagerAdapter {

    public NewsTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewsAllFragment();
            case 1:
                return new NewsLocalFragment();
            case 2:
                return new NewsStateFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}