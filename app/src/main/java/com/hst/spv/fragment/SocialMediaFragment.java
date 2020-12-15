package com.hst.spv.fragment;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.hst.spv.R;
import com.hst.spv.adapter.SocialMediaFragmentAdapter;
import com.hst.spv.adapter.SpvAdapter;
import com.hst.spv.helper.ProgressDialogHelper;
import com.hst.spv.servicehelpers.ServiceHelper;

public class SocialMediaFragment extends Fragment {

    private static final String TAG = SocialMediaFragment.class.getName();
    private View view;
    private ServiceHelper serviceHelper;
    private ProgressDialogHelper progressDialogHelper;

    private TabLayout.TabLayoutOnPageChangeListener tabatab;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[]tabIcons = {R.drawable.tab_fb, R.drawable.tab_insta};
    public static SocialMediaFragment newInstance(int pos) {
        SocialMediaFragment fragment = new SocialMediaFragment();
        Bundle args = new Bundle();
        args.putInt("position", pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_social_media, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        initialiseTabs();
        return view;
    }

    private void initialiseTabs() {

        TextView faceBook = (TextView)LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        faceBook.setText(getString(R.string.facebook));
        faceBook.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_fb, 0, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(faceBook));

        TextView twitter = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        twitter.setText(getString(R.string.twitter));
        twitter.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_twitter, 0, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(twitter));

        TextView insta = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        insta.setText(getString(R.string.instagram));
        insta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.tab_insta, 0, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(insta));

        SocialMediaFragmentAdapter adapter = new SocialMediaFragmentAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);

        tabatab = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        viewPager.addOnPageChangeListener(tabatab);
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
                viewPager.setCurrentItem(tab.getPosition());
                viewPager.getCurrentItem();
            }
        });
        //Bonus Code : If your tab layout has more than 2 tabs then tab will scroll other wise they will take whole width of the screen
        if (tabLayout.getTabCount() <= 2) {
            tabLayout.setTabMode(TabLayout.
                    MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.
                    MODE_SCROLLABLE);
        }
    }
//    private void setupViewPager(ViewPager viewPager) {
//        SocialMediaFragmentAdapter adapter = new SocialMediaFragmentAdapter(getChildFragmentManager());
//        adapter.addFrag(new FacebookFragment(), "Facebook");
//        adapter.addFrag(new TwitterFragment(), "Twitter");
//        adapter.addFrag(new InstagramFragment(), "Instagram");
//        viewPager.setAdapter(adapter);
//    }
}