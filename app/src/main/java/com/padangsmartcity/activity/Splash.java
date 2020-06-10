package com.padangsmartcity.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.padangsmartcity.BuildConfig;
import com.padangsmartcity.R;
import com.padangsmartcity.server.ServerApi;
import com.padangsmartcity.util.AppController;
import com.padangsmartcity.util.SetUI;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends Activity {

    SetUI setUI = new SetUI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setUI.StatusBarTransparent(getWindow());

        cek_versi();

    }


    private void cek_versi(){
        JsonObjectRequest requestData = new JsonObjectRequest(Request.Method.POST, ServerApi.cek_versi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("volley", "response : "+response.toString());
                        try {
                            Log.d("version_code", "version_code : "+response.getString("version_code"));
                            if (BuildConfig.VERSION_CODE < response.getInt("version_code")){
                                show_dialog("PEMBARUAN TERSEDIA", String.valueOf(Html.fromHtml("PERUBAHAN VERSI "+response.getString("version_name")+"<br>"+response.getString("changelog"))));
                            }else{
                                redirect();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            redirect();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : "+error.getMessage());
                        redirect();
                    }
                });
        AppController.getInstance().addToRequestQueue(requestData);
    }

    private void redirect(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //setelah loading maka akan langsung berpindah ke home activity
                Intent i = new Intent(Splash.this, Slider.class);
                startActivity(i);
                finish();

            }
        },1000);
    }

    private void show_dialog(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("PERBARUI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        open_store();
                    }
                })
                .setNegativeButton("NANTI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Splash.this, Slider.class);
                        startActivity(i);
                        finish();
                    }
                })
                .show();
    }

    private void open_store(){
        final String appPackagename = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackagename)));
        }catch (android.content.ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+appPackagename)));
        }
        finish();
    }
}
