package com.padangsmartcity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.padangsmartcity.fragment.Statistik;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.padangsmartcity.activity.BaseActivity;
import com.padangsmartcity.activity.Cari;
import com.padangsmartcity.activity.Pemberitahuan;
import com.padangsmartcity.fragment.Aduan;
import com.padangsmartcity.fragment.Beranda;
import com.padangsmartcity.fragment.BottomSheetActivateLocation;
import com.padangsmartcity.fragment.Profil;
import com.padangsmartcity.R;
import com.padangsmartcity.util.PrefManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements BottomSheetActivateLocation.BottomSheetListener{
    private TextView mTextMessage;
    BottomNavigationView navView;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(Html.fromHtml("<b>Padang Smart City</b>"));

        findView();
        initView();
        initListener();
        BerandaFragment();
        getLastLocation();
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_pemberitahuan:
                //action here
                Intent i = new Intent(getApplicationContext(), Pemberitahuan.class);
                startActivity(i);
                return true;
            case R.id.action_cari:
                //action here
                Intent in = new Intent(getApplicationContext(), Cari.class);
                startActivity(in);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void findView() {
        navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
    }

    @Override
    public void initView() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        prefManager = new PrefManager(this);
    }

    @Override
    public void initListener() {
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private  void BerandaFragment(){
        getSupportActionBar().setTitle(Html.fromHtml("<b>Padang Smart City</b>"));
        Beranda beranda = new Beranda();
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.frame_main, beranda);
        fr.commit();
    }
//
//    private void BeritaFragment(){
//        getSupportActionBar().setTitle(Html.fromHtml("<b>BERITA</b>"));
//        Berita berita = new Berita();
//        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
//        fr.replace(R.id.frame_main, berita);
//        fr.commit();
//    }


    private void StatistikFragment(){
        getSupportActionBar().setTitle(Html.fromHtml("<b>Statistik</b>"));
        Statistik statistik = new Statistik();
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.frame_main, statistik);
        fr.commit();
    }
    private void AduanFragment(){
        getSupportActionBar().setTitle(Html.fromHtml("<b>ADUAN</b>"));
        Aduan aduan = new Aduan();
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.frame_main, aduan);
        fr.commit();
    }

    private void ProfilFragment(){
        getSupportActionBar().setTitle(Html.fromHtml("<b>PROFIL</b>"));
        Profil profil = new Profil();
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.frame_main, profil);
        fr.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_beranda:
                    mTextMessage.setText(R.string.title_beranda);
                    BerandaFragment();
                    return true;
                case R.id.navigation_statistik:
                    mTextMessage.setText(R.string.title_stat);
                    StatistikFragment();
                    return true;
                case R.id.navigation_aduan:
                    mTextMessage.setText(R.string.title_aduan);
                    AduanFragment();
                    return true;
                case R.id.navigation_profil:
                    mTextMessage.setText(R.string.title_profil);
                    ProfilFragment();
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    prefManager.setLocaton(location.getLatitude()+", "+location.getLongitude());
                                    prefManager.setLat(location.getLatitude()+"");
                                    prefManager.setLng(location.getLongitude()+"");
                                }
                            }
                        }
                );
            } else {
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage("Silahkan aktifkan lokasi untuk pengalaman yang lebih baik. Aktifkan lokasi?")
//                        .setCancelable(false)
//                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
                BottomSheetActivateLocation bottomSheet = new BottomSheetActivateLocation();
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            prefManager.setLocaton(mLastLocation.getLatitude()+", "+mLastLocation.getLongitude());

            prefManager.setLat(mLastLocation.getLatitude()+"");
            prefManager.setLng(mLastLocation.getLongitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    @Override
    public void onButtonClicked(String text) {

    }
}
