package com.hst.spv.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hst.spv.fragment.EventAllFragment;
import com.hst.spv.fragment.EventLocalFragment;
import com.hst.spv.fragment.EventStateFragment;
import com.hst.spv.fragment.NewsAllFragment;
import com.hst.spv.fragment.NewsLocalFragment;
import com.hst.spv.fragment.NewsStateFragment;

public class EventTabAdapter extends FragmentStatePagerAdapter {

    public EventTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EventAllFragment();
            case 1:
                return new EventLocalFragment();
            case 2:
                return new EventStateFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}