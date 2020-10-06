package com.hst.spv.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hst.spv.fragment.AwardFragment;
import com.hst.spv.fragment.BiographyFragment;
import com.hst.spv.fragment.NotableWorksFragment;
import com.hst.spv.fragment.PositionsFragment;

public class SpvAdapter extends FragmentStatePagerAdapter {

    public SpvAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new BiographyFragment();
            case 1:
                return new PositionsFragment();
            case 2:
                return new AwardFragment();
            case 3:
                return new NotableWorksFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
