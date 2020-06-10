package com.padangsmartcity.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.padangsmartcity.R;

import java.net.MalformedURLException;
import java.net.URL;

public class LihatFoto extends BaseActivity {

    ImageView image;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_foto);
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
        image = findViewById(R.id.image);
    }

    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(Html.fromHtml("<b>"+bundle.getString("title")+"</b>"));
        try {
            Glide.with(getApplicationContext())
                    .load(new URL(bundle.getString("image")))
                    .placeholder(R.drawable.no_image)
                    .into(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {

    }
}
