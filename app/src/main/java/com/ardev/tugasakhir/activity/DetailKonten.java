package com.ardev.tugasakhir.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ardev.tugasakhir.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class DetailKonten extends BaseActivity {

    Bundle bundle;
    TextView title_konten_app, subtitle_konten_app, txt_jarak;
    ImageView image_konten_app;
    WebView konten_app;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_konten);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        findView();
        initView();
        initListener();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void findView() {
        title_konten_app = findViewById(R.id.title_konten_app);
        subtitle_konten_app = findViewById(R.id.subtitle_konten_app);
        txt_jarak = findViewById(R.id.txt_jarak);
        konten_app = findViewById(R.id.konten_app);
        image_konten_app = findViewById(R.id.image_konten_app);
        fab = findViewById(R.id.fab);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(Html.fromHtml("<b>"+bundle.getString("title_konten_app")+"</b>"));

        title_konten_app.setText(bundle.getString("title_konten_app"));
        subtitle_konten_app.setText(bundle.getString("subtitle_konten_app"));
        txt_jarak.setText(bundle.getString("jarak"));

        konten_app.setHorizontalScrollBarEnabled(false);
        konten_app.setVerticalScrollBarEnabled(false);
        konten_app.loadDataWithBaseURL(null, "<style>p{color: grey} img{display: inline; height: auto; max-width: 100%;}</style>"+bundle.getString("konten_app"), "text/html", "charset=UTF-8", null);

        try {
            Glide.with(getApplicationContext())
                    .load(new URL(bundle.getString("image_konten_app")))
                    .placeholder(R.drawable.no_image)
                    .into(image_konten_app);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (bundle.getString("lat").equals("null")){
            fab.setVisibility(View.GONE);
        }else{
            fab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListener() {
        image_konten_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LihatFoto.class);
                i.putExtra("image", bundle.getString("image_konten_app"));
                i.putExtra("title", bundle.getString("title_konten_app"));
                startActivity(i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format("https://maps.google.com/maps?q="+bundle.getString("lat")+","+bundle.getString("lng"));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
    }
}
