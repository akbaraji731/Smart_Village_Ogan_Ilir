package com.smartvillageoi.clientapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.smartvillageoi.clientapp.R;
import com.smartvillageoi.clientapp.activity.LoginActivity;
import com.smartvillageoi.clientapp.helper.Constant;
import com.smartvillageoi.clientapp.helper.Session;

public class AlarmFragment extends Fragment{
    public static AlarmFragment newInstance() {
        return new AlarmFragment();
    }
    public Session session;
    public static Activity activity;
    public static Fragment alarmFragment;
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;
    TextView TinggiAir, SiagaBanjir, Tempat;
    Button btnFind;
    EditText KeteranganData;
    String user, lokasi,siaga_banjir = "1", level_air = "1", latitude = "1", longitude = "1", keteranganLokasi = "1";
    RadioButton lokasi_1, lokasi_2, lokasi_3, lokasi_4, lokasi_5;
    private FusedLocationProviderClient locationProviderClient;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);
        activity = getActivity();
        session = new Session(activity);
        user = session.getData(Constant.NAME);

        TinggiAir =  rootView.findViewById(R.id.airData);
        SiagaBanjir =  rootView.findViewById(R.id.siagaData);
        Tempat =  rootView.findViewById(R.id.lokasi);
        KeteranganData = rootView.findViewById(R.id.keteranganLokasi);
        btnFind =  rootView.findViewById(R.id.bagikanLokasi);
        lokasi_1 = rootView.findViewById(R.id.titik_1);
        lokasi_2 = rootView.findViewById(R.id.titik_2);
        lokasi_3 = rootView.findViewById(R.id.titik_3);
        lokasi_4 = rootView.findViewById(R.id.titik_4);
        lokasi_5 = rootView.findViewById(R.id.titik_5);
        getData();
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                getData();
                handler.postDelayed(this, 5000);
            }
        });

        locationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getBoolean(Constant.IS_USER_LOGIN)) {
                    getLocation();
                } else {
                    Toast.makeText(getActivity(), "SILAHKAN MASUKKAN ATAU DAFTARKAN AKUN TERLEBIH DAHULU!", Toast.LENGTH_LONG).show();
                }
            }
        });

        lokasi_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kirimData("1");
            }
        });
        lokasi_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kirimData("2");
            }
        });
        lokasi_3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kirimData("3");
            }
        });
        lokasi_4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kirimData("4");
            }
        });
        lokasi_5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kirimData("5");
            }
        });
        notifShareLokasi();
        cekButton();
        return rootView;
    }

    public void login()
    {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void notifShareLokasi(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Pemberitahuan";
            String description = "Notifikasi Minta Bantuan atau Pemberitahuan";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("lokasi", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = getContext().getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Button Lokasi Pada Notifikasi
        final int flag =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + latitude + "%2C" + longitude ));
        intent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent button = PendingIntent.getActivity(getActivity(), 0, intent, flag);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "lokasi")
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle(user + " Mengirimkan Lokasinya!")
                .addAction(R.drawable.ic_water, "Buka Lokasi", button)
                .setContentText("Keterangan : " + keteranganLokasi);

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(getContext().getApplicationContext());
    }

    public void kirimData(String data){
        SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("lokasi", data);
        editor.apply();
    }

    private void cekButton(){
        if(lokasi == "1"){
            lokasi_1.setChecked(true);
            lokasi_2.setChecked(false);
            lokasi_3.setChecked(false);
            lokasi_4.setChecked(false);
            lokasi_5.setChecked(false);
        } else if(lokasi == "2"){
            lokasi_2.setChecked(true);
            lokasi_1.setChecked(false);
            lokasi_3.setChecked(false);
            lokasi_4.setChecked(false);
            lokasi_5.setChecked(false);
        } else if(lokasi == "3"){
            lokasi_3.setChecked(true);
            lokasi_1.setChecked(false);
            lokasi_2.setChecked(false);
            lokasi_4.setChecked(false);
            lokasi_5.setChecked(false);
        } else if(lokasi == "4"){
            lokasi_4.setChecked(true);
            lokasi_1.setChecked(false);
            lokasi_2.setChecked(false);
            lokasi_3.setChecked(false);
            lokasi_5.setChecked(false);
        } else if(lokasi == "5"){
            lokasi_5.setChecked(true);
            lokasi_1.setChecked(false);
            lokasi_2.setChecked(false);
            lokasi_3.setChecked(false);
            lokasi_4.setChecked(false);
        } else if(lokasi == null){

        }
    }


    void getData(){
        Bundle bundle = this.getArguments();
        lokasi = bundle.getString("lokasi");
        siaga_banjir = bundle.getString("siaga_banjir");
        level_air = bundle.getString("level_air");


        TinggiAir.setText(level_air + " Cm");
        SiagaBanjir.setText(siaga_banjir);
        Tempat.setText(lokasi);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10){
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Izin lokasi tidak di aktifkan!", Toast.LENGTH_SHORT).show();
            }else{
                getLocation();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // get Permission
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 10);
        }else {
            // get Location
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null) {
                        keteranganLokasi = KeteranganData.getText().toString();
                        Toast.makeText(getActivity(), "Keterangan : " + keteranganLokasi, Toast.LENGTH_SHORT).show();
                        latitude =  String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                        notifShareLokasi();
                        notificationManagerCompat.notify(1, notification);
                        // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + String.valueOf(location.getLatitude()) + "%2C" + String.valueOf(location.getLongitude())));
                        // startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "Lokasi tidak aktif!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}