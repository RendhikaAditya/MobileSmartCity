package com.ardev.tugasakhir.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ardev.tugasakhir.R;

import java.net.MalformedURLException;
import java.net.URL;

public class DetailBerita extends BaseActivity {

    Bundle bundle;
    TextView title_konten_app, subtitle_konten_app, tanggal_konten_app;
    ImageView image_konten_app;
    WebView konten_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);
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
        tanggal_konten_app = findViewById(R.id.tanggal_konten_app);
        konten_app = findViewById(R.id.konten_app);
        image_konten_app = findViewById(R.id.image_konten_app);
    }

    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        getSupportActionBar().setTitle(Html.fromHtml("<b>DETAIL BERITA</b>"));

        title_konten_app.setText(bundle.getString("title_konten_app"));
        subtitle_konten_app.setText(bundle.getString("subtitle_konten_app"));
        tanggal_konten_app.setText(bundle.getString("tanggal_konten_app"));

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
    }
}
