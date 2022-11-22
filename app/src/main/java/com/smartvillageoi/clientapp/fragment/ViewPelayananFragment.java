package com.smartvillageoi.clientapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.smartvillageoi.clientapp.R;

public class ViewPelayananFragment extends Fragment {
    public static ViewPelayananFragment newInstance() {
        return new ViewPelayananFragment();
    }

    TextView user,nama,nik,alamat,jenis_surat,isi_surat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pelayanan_item_fake, container, false);

        user =  rootView.findViewById(R.id.userItem);
        nama =  rootView.findViewById(R.id.namaItem);
        nik =  rootView.findViewById(R.id.nikItem);
        alamat =  rootView.findViewById(R.id.alamatItem);
        jenis_surat =  rootView.findViewById(R.id.jenisSuratItem);
        isi_surat =  rootView.findViewById(R.id.keteranganSuratItem);


        user.setText("Fajarul Akbar Aji");
        nama.setText("M Fajarul Akbar Aji Putra");
        nik.setText("1671063004020007");
        alamat.setText("Jalan Perintis Kemerdekaan");
        jenis_surat.setText("Surat Pengantar");
        isi_surat.setText("Perjalanan Dinas");

        return rootView;
    }

}