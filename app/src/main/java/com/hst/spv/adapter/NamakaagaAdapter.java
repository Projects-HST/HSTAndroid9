package com.hst.spv.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.hst.spv.fragment.AmmaIASAcademy;
import com.hst.spv.fragment.NallarammTrust;
import com.hst.spv.fragment.NamakaagaUllatchi;

public class NamakaagaAdapter extends FragmentStatePagerAdapter {

    public NamakaagaAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                return new NamakaagaUllatchi();
            case 1:
                return new AmmaIASAcademy();
            case 2:
                return new NallarammTrust();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
