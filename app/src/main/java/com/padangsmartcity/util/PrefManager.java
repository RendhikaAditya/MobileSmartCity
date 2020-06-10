package com.padangsmartcity.util;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "data_app";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setLocaton(String location){
        editor.putString("location", location);
        editor.apply();
    }

    public String getLocation(){
        return pref.getString("location", "");
    }

    public void setIdUser(String id_user){
        editor.putString("id_user", id_user);
        editor.apply();
    }

    public String getIdUser(){
        return pref.getString("id_user", "");
    }



    public void setNamaUser(String nama_user){
        editor.putString("nama_user", nama_user);
        editor.apply();
    }

    public String getNamaUser(){
        return pref.getString("nama_user", "");
    }

    public void setLoginStatus(boolean islogin){
        editor.putBoolean("login", islogin);
        editor.apply();
    }

    public boolean getLoginStatus(){
        return pref.getBoolean("login", false);
    }

    public void setLat(String lat){
        editor.putString("lat", lat);
        editor.apply();
    }

    public String getLat(){
        return pref.getString("lat", "");
    }

    public void setLng(String lng){
        editor.putString("lng", lng);
        editor.apply();
    }

    public String getLng(){
        return pref.getString("lng", "");
    }

}