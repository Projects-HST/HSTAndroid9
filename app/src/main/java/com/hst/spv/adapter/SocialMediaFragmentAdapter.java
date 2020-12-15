package com.hst.spv.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hst.spv.fragment.FacebookFragment;
import com.hst.spv.fragment.InstagramFragment;
import com.hst.spv.fragment.NewsAllFragment;
import com.hst.spv.fragment.NewsLocalFragment;
import com.hst.spv.fragment.NewsStateFragment;
import com.hst.spv.fragment.TwitterFragment;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaFragmentAdapter extends FragmentPagerAdapter {


    public SocialMediaFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FacebookFragment();
            case 1:
                return new TwitterFragment();
            case 2:
                return new InstagramFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}