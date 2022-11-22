package com.smartvillageoi.clientapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.PaymentResultListener;
import com.smartvillageoi.clientapp.R;
import com.smartvillageoi.clientapp.fragment.AddressListFragment;
import com.smartvillageoi.clientapp.fragment.AlarmFragment;
import com.smartvillageoi.clientapp.fragment.CartFragment;
import com.smartvillageoi.clientapp.fragment.CategoryFragment;
import com.smartvillageoi.clientapp.fragment.DrawerFragment;
import com.smartvillageoi.clientapp.fragment.HomeFragment;
import com.smartvillageoi.clientapp.fragment.MainFragment;
import com.smartvillageoi.clientapp.fragment.MenuCCTVFragment;
import com.smartvillageoi.clientapp.fragment.OrderPlacedFragment;
import com.smartvillageoi.clientapp.fragment.PelayananFragment;
import com.smartvillageoi.clientapp.fragment.ProductDetailFragment;
import com.smartvillageoi.clientapp.fragment.ProductListFragment;
import com.smartvillageoi.clientapp.fragment.SubCategoryFragment;
import com.smartvillageoi.clientapp.fragment.TelukPerepatFragment;
import com.smartvillageoi.clientapp.fragment.TrackOrderFragment;
import com.smartvillageoi.clientapp.fragment.TrackerDetailFragment;
import com.smartvillageoi.clientapp.fragment.ViewPelayananFragment;
import com.smartvillageoi.clientapp.fragment.WalletTransactionFragment;
import com.smartvillageoi.clientapp.fragment.WisataFragmentWeb;
import com.smartvillageoi.clientapp.helper.ApiConfig;
import com.smartvillageoi.clientapp.helper.Constant;
import com.smartvillageoi.clientapp.helper.DatabaseHelper;
import com.smartvillageoi.clientapp.helper.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PaymentResultListener, PaytmPaymentTransactionCallback {

    static final String TAG = "MAIN ACTIVITY";
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;
    public static BottomNavigationView bottomNavigationView;
    public static Fragment active;
    public static FragmentManager fm = null;
    public static Fragment homeFragment, categoryFragment, favoriteFragment, trackOrderFragment, drawerFragment, mainFragment, pelayananFragment, hasilPelayananFragment, menuCCTV, CCTV1, wisataFragment, teluk_perepatFragment, alarmFragment;
    public static boolean homeClicked = false, categoryClicked = false, favoriteClicked = false, menuClicked = false, drawerClicked = false;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    public Session session;
    boolean doubleBackToExitPressedOnce = false;
    Menu menu;
    DatabaseHelper databaseHelper;
    String from;
    TextView toolbarTitle;
    ImageView imageMenu, imageHome;
    CardView cardViewHamburger;

    String lokasi = "1", userLogin, userAdmin = "Jovan";
    NotificationManagerCompat notificationManagerCompat;
    Notification notification;
    int tampil1 = 0;
    int tampil2 = 0;
    int tampil3 = 0;
    int hasil_siaga = 0;
    String level_air = "1";
    String siaga_banjir = "1";

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageMenu = findViewById(R.id.imageMenu);
        imageHome = findViewById(R.id.imageHome);
        cardViewHamburger = findViewById(R.id.cardViewHamburger);

        activity = MainActivity.this;
        session = new Session(activity);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        from = getIntent().getStringExtra(Constant.FROM);
        databaseHelper = new DatabaseHelper(activity);

        if (session.getBoolean(Constant.IS_USER_LOGIN)) {
            ApiConfig.getCartItemCount(activity, session);
        } else {
            session.setData(Constant.STATUS, "1");
            databaseHelper.getTotalItemOfCart(activity);
        }

        setAppLocal("id"); //Change you language code here

        fm = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        categoryFragment = new CategoryFragment();
        trackOrderFragment = new TrackOrderFragment();
        drawerFragment = new DrawerFragment();

        mainFragment = new MainFragment();
        pelayananFragment = new PelayananFragment();
        hasilPelayananFragment = new ViewPelayananFragment();
        menuCCTV = new MenuCCTVFragment();
        wisataFragment = new WisataFragmentWeb();
        teluk_perepatFragment = new TelukPerepatFragment();
        alarmFragment = new AlarmFragment();
        userLogin = session.getData(Constant.NAME);

        Bundle bundle = new Bundle();
        bottomNavigationView.setSelectedItemId(R.id.navMain);
        active = homeFragment;
        homeClicked = true;
        drawerClicked = false;
        favoriteClicked = false;
        menuClicked = false;
        categoryClicked = false;

        try {
            if (!getIntent().getStringExtra("json").isEmpty()) {
                bundle.putString("json", getIntent().getStringExtra("json"));
            }
            homeFragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.container, homeFragment).commit();
        } catch (Exception e) {
            fm.beginTransaction().add(R.id.container, homeFragment).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            {
                Fragment selectedFragment = homeFragment;
                switch (item.getItemId()) {
                    case R.id.navMain:
                        selectedFragment = homeFragment;
                        active = homeFragment;
                        break;
                    case R.id.navCategory:
                        selectedFragment = categoryFragment;
                        active = categoryFragment;
                        break;
                    case R.id.navWishList:
                        selectedFragment = mainFragment;
                        active = mainFragment;
                        break;
                    case R.id.navProfile:
                        selectedFragment = drawerFragment;
                        active = drawerFragment;
                        break;
                }

                fm.beginTransaction().replace(R.id.container, selectedFragment).commit();
                return true;
            }
        });

        switch (from) {
            case "checkout":
                bottomNavigationView.setVisibility(View.GONE);
                ApiConfig.getCartItemCount(activity, session);
                Fragment fragment = new AddressListFragment();
                Bundle bundle00 = new Bundle();
                bundle00.putString(Constant.FROM, "login");
                bundle00.putDouble("total", Double.parseDouble(ApiConfig.StringFormat("" + Constant.FLOAT_TOTAL_AMOUNT)));
                fragment.setArguments(bundle00);
                fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                break;
            case "share":
                Fragment fragment0 = new ProductDetailFragment();
                Bundle bundle0 = new Bundle();
                bundle0.putInt("variantPosition", getIntent().getIntExtra("variantPosition", 0));
                bundle0.putString("id", getIntent().getStringExtra("id"));
                bundle0.putString(Constant.FROM, "share");
                fragment0.setArguments(bundle0);
                fm.beginTransaction().add(R.id.container, fragment0).addToBackStack(null).commit();
                break;
            case "product":
                Fragment fragment1 = new ProductDetailFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("variantPosition", getIntent().getIntExtra("variantPosition", 0));
                bundle1.putString("id", getIntent().getStringExtra("id"));
                bundle1.putString(Constant.FROM, "product");
                fragment1.setArguments(bundle1);
                fm.beginTransaction().add(R.id.container, fragment1).addToBackStack(null).commit();
                break;
            case "category":
                Fragment fragment2 = new SubCategoryFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("id", getIntent().getStringExtra("id"));
                bundle2.putString("name", getIntent().getStringExtra("name"));
                bundle2.putString(Constant.FROM, "category");
                fragment2.setArguments(bundle2);
                fm.beginTransaction().add(R.id.container, fragment2).addToBackStack(null).commit();
                break;
            case "order":
                Fragment fragment3 = new TrackerDetailFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("model", "");
                bundle3.putString("id", getIntent().getStringExtra("id"));
                fragment3.setArguments(bundle3);
                fm.beginTransaction().add(R.id.container, fragment3).addToBackStack(null).commit();
                break;
            case "tracker":
                fm.beginTransaction().add(R.id.container, new TrackOrderFragment()).addToBackStack(null).commit();
                break;
            case "payment_success":
                fm.beginTransaction().add(R.id.container, new OrderPlacedFragment()).addToBackStack(null).commit();
                break;
            case "wallet":
                fm.beginTransaction().add(R.id.container, new WalletTransactionFragment()).addToBackStack(null).commit();
                break;
        }

        fm.addOnBackStackChangedListener(() -> {
            toolbar.setVisibility(View.VISIBLE);
            Fragment currentFragment = fm.findFragmentById(R.id.container);
            assert currentFragment != null;
            currentFragment.onResume();
        });

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            session.setData(Constant.FCM_ID, token);
            Register_FCM(token);
        });


        panggilData();
        GetProductsName();
        notifSiaga1();
        notifSiaga2();
        notifBahaya();
        getDataSiaga();
        getDataAir();

        dijalankan();
    }

    @Override
    protected void onPause() {
        invalidateOptionsMenu();
        super.onPause();
        dijalankan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dijalankan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dijalankan();
    }

    public void setAppLocal(String languageCode) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(languageCode.toLowerCase()));
        resources.updateConfiguration(configuration, dm);
        bottomNavigationView.setLayoutDirection(activity.getResources().getConfiguration().getLayoutDirection());
    }

    public void Register_FCM(String token) {
        Map<String, String> params = new HashMap<>();
        if (session.getBoolean(Constant.IS_USER_LOGIN)) {
            params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        }
        params.put(Constant.FCM_ID, token);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        session.setData(Constant.FCM_ID, token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, activity, Constant.REGISTER_DEVICE_URL, params, false);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        if (fm.getBackStackEntryCount() == 0) {
            if (active != homeFragment) {
                this.doubleBackToExitPressedOnce = false;
                bottomNavigationView.setSelectedItemId(R.id.navMain);
                homeClicked = true;
                fm.beginTransaction().replace(R.id.container, homeFragment).commit();
                active = homeFragment;
            } else {
                Toast.makeText(this, getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbar_cart) {
            MainActivity.fm.beginTransaction().add(R.id.container, new CartFragment()).addToBackStack(null).commit();
        } else if (id == R.id.toolbar_search) {
            Fragment fragment = new ProductListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.FROM, "search");
            bundle.putString(Constant.NAME, activity.getString(R.string.search));
            bundle.putString(Constant.ID, "");
            fragment.setArguments(bundle);
            MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
        } else if (id == R.id.toolbar_logout) {
            session.logoutUserConfirmation(activity);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.toolbar_cart).setVisible(true);
        menu.findItem(R.id.toolbar_search).setVisible(true);
        menu.findItem(R.id.toolbar_cart).setIcon(ApiConfig.buildCounterDrawable(Constant.TOTAL_CART_ITEM, activity));

        if (fm.getBackStackEntryCount() > 0) {
            toolbarTitle.setText(Constant.TOOLBAR_TITLE);
            bottomNavigationView.setVisibility(View.GONE);

            cardViewHamburger.setCardBackgroundColor(getColor(R.color.colorPrimaryLight));
            imageMenu.setOnClickListener(v -> fm.popBackStack());

            imageMenu.setVisibility(View.VISIBLE);
            imageHome.setVisibility(View.GONE);
        } else {
            if (session.getBoolean(Constant.IS_USER_LOGIN)) {
                toolbarTitle.setText(getString(R.string.hi) + session.getData(Constant.NAME) + "!");
            } else {
                toolbarTitle.setText(getString(R.string.hi_user));
            }
            bottomNavigationView.setVisibility(View.VISIBLE);
            cardViewHamburger.setCardBackgroundColor(getColor(R.color.transparent));
            imageMenu.setVisibility(View.GONE);
            imageHome.setVisibility(View.VISIBLE);
        }

        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        Objects.requireNonNull(fragment).onActivityResult(requestCode, resultCode, data);

    }

    public void GetProductsName() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.GET_ALL_PRODUCTS_NAME, Constant.GetVal);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        session.setData(Constant.GET_ALL_PRODUCTS_NAME, jsonObject.getString(Constant.DATA));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.GET_ALL_PRODUCTS_URL, params, false);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            WalletTransactionFragment.payFromWallet = false;
            new WalletTransactionFragment().AddWalletBalance(activity, new Session(activity), WalletTransactionFragment.amount, WalletTransactionFragment.msg);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onPaymentSuccess  ", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(activity, getString(R.string.order_cancel), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onPaymentError  ", e);
        }
    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(activity, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(activity, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(activity, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

    public void panggilData() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        String data = prefs.getString("lokasi", "1");
        if(data != null){
            lokasi = data;
        }
    }

    public void notifSiaga1(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Siaga 1";
            String description = "Alarm Siaga Bencana Banjir";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("siaga1", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "siaga1")
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle("Alarm Siaga Bencana Banjir")
                .setContentText("SIAGA 1! : Ketinggian Air Mencapai " + level_air + " Cm");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }

    public void notifSiaga2(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Siaga 2";
            String description = "Alarm Siaga Bencana Banjir";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("siaga2", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "siaga2")
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle("Alarm Siaga Bencana Banjir")
                .setContentText("SIAGA 2! : Ketinggian Air Mencapai " + level_air + " Cm");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }

    public void notifBahaya(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "BAHAYA";
            String description = "Alarm Siaga Bencana Banjir";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("bahaya", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "bahaya")
                .setSmallIcon(R.drawable.ic_water)
                .setContentTitle("Alarm Siaga Bencana Banjir")
                .setContentText("BAHAYA BANJIR! : Ketinggian Air Mencapai " + level_air + " Cm");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);
    }

    public void dijalankan(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                userLogin = session.getData(Constant.NAME);

                panggilData();
                getDataAir();
                getDataSiaga();
                cekKirimData();
                handler.postDelayed(this, 10000);
            }
        });
    }

    public void getDataAir(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlAir = "https://dashboard.smartvillagedev.com/includes/monitoring_air.php?status=baca&titik=level_" + lokasi;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAir,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        level_air = response.trim();
//                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                level_air = " -- ";
            }
        });
        queue.add(stringRequest);
    }

    public void getDataSiaga(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlSiaga = "https://dashboard.smartvillagedev.com/includes/monitoring_air.php?status=baca&titik=siaga_"+lokasi;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSiaga,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null && response.length() < 3){
                            hasil_siaga = Integer.parseInt(response);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                siaga_banjir = "Gagal Mengambil Data!";
            }
        });
        queue.add(stringRequest);

        NotifSiaga1();
        NotifSiaga2();
        NotifSiagaBahaya();
    }

    public void cekKirimData(){
        if(hasil_siaga == 1){
            siaga_banjir = "Aman";
        } else if(hasil_siaga == 2){
            siaga_banjir = "Siaga 1";
        } else if(hasil_siaga == 3){
            siaga_banjir = "Siaga 2";
        } else if(hasil_siaga == 4){
            siaga_banjir = "BAHAYA BANJIR!";
        }

        Bundle bundle = new Bundle();
        bundle.putString("level_air", level_air.toString());
        bundle.putString("siaga_banjir", siaga_banjir.toString());
        bundle.putString("lokasi", lokasi.toString());
        alarmFragment.setArguments(bundle);
    }

    public void NotifSiaga1(){
        if(hasil_siaga == 2){
            tampil1++;
            if (tampil1 <= 3){
                Toast.makeText(getApplicationContext(), level_air + " Cm : " + "Siaga 1",
                        Toast.LENGTH_SHORT).show();
                notifSiaga1();
                notificationManagerCompat.notify(2, notification);
            } else{}
        } else {
            tampil1 = 0;
        }
    }

    public void NotifSiaga2(){
        if(hasil_siaga == 3){
            tampil2++;
            if (tampil2 <= 3){
                Toast.makeText(getApplicationContext(), level_air + " Cm : " + "Siaga 2",
                        Toast.LENGTH_SHORT).show();
                notifSiaga2();
                notificationManagerCompat.notify(1, notification);
            } else{}
        } else {
            tampil2 = 0;
        }
    }

    public void NotifSiagaBahaya(){
        if(hasil_siaga == 4){
            tampil3++;
            if (tampil3 <= 3){
                Toast.makeText(getApplicationContext(), level_air + " Cm : " + "BAHAYA BANJIR!",
                        Toast.LENGTH_SHORT).show();
                notifBahaya();
                notificationManagerCompat.notify(1, notification);
            } else{}
        } else {
            tampil3 = 0;
        }
    }

    // Masuk Link
    public void lokasi_teluk_perepat(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/K53jUKFVCnZfh44q9"));
        startActivity(intent);
    }

    public void wa_teluk_perepat(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=6281320342443"));
        startActivity(intent);
    }

    public void hilookVision(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.mcu.hilook&hl=id&gl=US"));
        startActivity(intent);
    }

    public void wa_admin_cctv(View view) {
        if (session.getBoolean(Constant.IS_USER_LOGIN)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send/?phone=6281320342443&text=Aplikasi%20SmartVillageOI.%20User%20" + session.getData(Constant.NAME) + ", ingin meminta izin akses CCTV pada aplikasi HilookVision. Terimakasih" + "&type=phone_number&app_absent=0"));
            startActivity(intent);
        } else {
            Toast.makeText(this, "UNTUK MENDAPATKAN NOMOR WA ADMIN, SILAHKAN MASUKKAN ATAU DAFTARKAN AKUN TERLEBIH DAHULU!", Toast.LENGTH_LONG).show();
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }
    }
    //


    public void pelayanan(View view) {
        if (userLogin == userAdmin) {
            fm.beginTransaction().replace(R.id.container, hasilPelayananFragment).commit();
        } else {
            fm.beginTransaction().replace(R.id.container, pelayananFragment).commit();
        }

    }

    public void siskamling(View view) {
        fm.beginTransaction().replace(R.id.container, menuCCTV).commit();
    }

    public void wisata(View view) {
        fm.beginTransaction().replace(R.id.container, wisataFragment).commit();
    }

    public void teluk_perepat(View view) {
        fm.beginTransaction().replace(R.id.container, teluk_perepatFragment).commit();
    }

    public void alarm(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("level_air", level_air.toString());
        bundle.putString("siaga_banjir", siaga_banjir.toString());
        bundle.putString("lokasi", lokasi.toString());
        alarmFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.container, alarmFragment).commit();
    }
}