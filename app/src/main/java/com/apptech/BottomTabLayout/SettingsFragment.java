package com.apptech.BottomTabLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apptech.apptechcomponents.R;

/**
 * Created by nirob on 6/13/17.
 */

public class SettingsFragment extends Fragment {


    public static Fragment newInstance() {
        Fragment frag = new SettingsFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }
}
