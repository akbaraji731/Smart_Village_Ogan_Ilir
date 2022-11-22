package com.smartvillageoi.clientapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.smartvillageoi.clientapp.R;

public class MenuCCTVFragment extends Fragment {

    public static MenuCCTVFragment newInstance() {
        return new MenuCCTVFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu_cctv, container, false);
        return rootView;
    }

}