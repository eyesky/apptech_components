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

public class AssistanceFragment extends Fragment {


    public static Fragment newInstance() {
        Fragment frag = new AssistanceFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assistance, container, false);
        return view;
    }
}
