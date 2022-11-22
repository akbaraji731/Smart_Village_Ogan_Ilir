package com.smartvillageoi.clientapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.smartvillageoi.clientapp.R;


public class WisataFragmentWeb extends Fragment {

    public static WisataFragmentWeb newInstance() {
        return new WisataFragmentWeb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wisata_web, container, false);

        return rootView;
    }

}