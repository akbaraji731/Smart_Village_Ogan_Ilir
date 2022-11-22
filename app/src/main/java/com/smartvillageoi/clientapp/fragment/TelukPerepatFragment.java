package com.smartvillageoi.clientapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.smartvillageoi.clientapp.R;

public class TelukPerepatFragment extends Fragment {

    public static TelukPerepatFragment newInstance() {
        return new TelukPerepatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teluk_perepat, container, false);

        return rootView;
    }


}