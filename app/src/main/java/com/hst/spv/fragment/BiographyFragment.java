package com.hst.spv.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hst.spv.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BiographyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BiographyFragment extends Fragment {

    public BiographyFragment() {
        // Required empty public constructor
    }
    public static BiographyFragment newInstance(int position) {
        BiographyFragment fragment = new BiographyFragment();
        Bundle args = new Bundle();
        args.putInt("biography", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_biography, container, false);
    }
}