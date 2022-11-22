package com.smartvillageoi.clientapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smartvillageoi.clientapp.R;
import com.smartvillageoi.clientapp.activity.LoginActivity;
import com.smartvillageoi.clientapp.helper.Constant;
import com.smartvillageoi.clientapp.helper.Session;

import java.util.HashMap;
import java.util.Map;

public class PelayananFragment extends Fragment {
    public static PelayananFragment newInstance() {
        return new PelayananFragment();
    }
    // Deklarasi Nama Fungsi Fragment Pelayanan
    public Session session;
    public static Activity activity;
    public static final String url = "https://dashboard.smartvillagedev.com/includes/kirim_pelayanan.php";
    TextView Keterangan;
    Button kirimSurat;
    EditText Nama, NIK, Alamat, JenisSurat, IsiSurat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pelayanan, container, false);

        activity = getActivity();
        session = new Session(activity);
        // Fungsi Kirim Surat dari Fragment Pelayanan
        Nama = rootView.findViewById(R.id.inNama);
        NIK = rootView.findViewById(R.id.inNIK);
        Alamat = rootView.findViewById(R.id.inAlamat);
        JenisSurat=  rootView.findViewById(R.id.inJenisSurat);
        IsiSurat=  rootView.findViewById(R.id.inIsiSurat);
        Keterangan =  rootView.findViewById(R.id.keterangan);

        kirimSurat = rootView.findViewById(R.id.kirimSurat);
        kirimSurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.getBoolean(Constant.IS_USER_LOGIN)) {
                    inputData();
                } else {
                    Toast.makeText(getActivity(), "SILAHKAN MASUKKAN ATAU DAFTARKAN AKUN TERLEBIH DAHULU!", Toast.LENGTH_LONG).show();
                    login();
                }
            }
        });
        // Batas
        return rootView;
    }

    public void login()
    {
        Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
        startActivity(intent);
    }

    // Fungsi Kirim data Pelayanan
    void inputData(){
        String nama = Nama.getText().toString();
        String nik = NIK.getText().toString();
        String alamat = Alamat.getText().toString();
        String jenis_surat = JenisSurat.getText().toString();
        String isi_surat = IsiSurat.getText().toString();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Keterangan.setText(response);
                    }
                }, new Response.ErrorListener()
                    { @Override
                         public void onErrorResponse(VolleyError error) {
                        Keterangan.setText("That didn't work!");
                    }
            })
        {
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("nama",nama);
                params.put("nik",nik);
                params.put("alamat",alamat);
                params.put("jenis_surat",jenis_surat);
                params.put("isi_surat",isi_surat);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    // Batas Fungsi kirim data Fragment Pelayanan

}