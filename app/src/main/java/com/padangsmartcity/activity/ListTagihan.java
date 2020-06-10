package com.padangsmartcity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.padangsmartcity.R;
import com.padangsmartcity.adapter.AdapterNop;
import com.padangsmartcity.model.ModelNop;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListTagihan extends AppCompatActivity {
TextView nop,txt_kosong;
    RecyclerView rvnop;
    List<ModelNop> mNop;
    ImageView img_kosong;
    RelativeLayout layout_kosong, layout_koneksi;
    LinearLayout layout;
    ShimmerFrameLayout shimmer_pemberitahuan;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tagihan);

        swipeRefreshLayout = findViewById(R.id.swipetagih);

        layout_koneksi = findViewById(R.id.layout_koneksi);
        shimmer_pemberitahuan = findViewById(R.id.shimmer_tagih);
        txt_kosong = findViewById(R.id.txt_kosong);
        img_kosong = findViewById(R.id.img_kosong);
        layout_kosong = findViewById(R.id.layout_kosong);
        layout = findViewById(R.id.listtagih);

        nop = findViewById(R.id.idNOP);

        rvnop = findViewById(R.id.rvnop);
        rvnop.setHasFixedSize(true);
        rvnop.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mNop = new ArrayList<>();

        Intent intent = getIntent();
        final String id = intent.getStringExtra("IDNOP");
        nop.setText(id);

        AndroidNetworking.initialize(this);

        layout_koneksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        layout_kosong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        getData();

    }

    private void load_empty(){
        layout_kosong.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.GONE);
        shimmer_pemberitahuan.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
//        txt_kosong.setText(Html.fromHtml("Hasil pencarian <b></b> tidak ditemukan."));
//        img_kosong.setImageDrawable(getResources().getDrawable(R.drawable.img_cari));
    }

    private void set_loading(){
        layout_kosong.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.GONE);
        shimmer_pemberitahuan.setVisibility(View.VISIBLE);
    }

    private void load_koneksi(){
        layout_kosong.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        shimmer_pemberitahuan.setVisibility(View.GONE);
    }

    private void load_sukses(){
        layout_kosong.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.GONE);
        shimmer_pemberitahuan.setVisibility(View.GONE);
    }

    private void getData() {
        Intent intent = getIntent();
        final String id = intent.getStringExtra("IDNOP");
        set_loading();
        AndroidNetworking.post("http://hotel.depotmart.id/api/get_tagihan.php")
                .addBodyParameter("nop_tagihan", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()>0){
                            load_sukses();
                            mNop.clear();
                        Log.d("Sukses ", "onResponse"+response);
                        try {
                            for (int i = 0; i<response.length(); i++){
                                JSONObject nopm = response.getJSONObject(i);
                                mNop.add(new ModelNop(
                                        nopm.getString("id"),
                                        nopm.getString("nop"),
                                        nopm.getString("lbumi"),
                                        nopm.getString("lbng"),
                                        nopm.getString("njopbumi"),
                                        nopm.getString("njopbng"),
                                        nopm.getString("nama"),
                                        nopm.getString("alamat"),
                                        nopm.getString("tahun"),
                                        nopm.getString("tanggal"),
                                        nopm.getInt("jumblah"),
                                        nopm.getString("status")
                                ));
                            }
                            AdapterNop adapterNop = new AdapterNop(mNop);
                            rvnop.setAdapter(adapterNop);
                            adapterNop.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            load_empty();
                        }}else {
                            load_empty();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "onError :"+anError);
                        load_koneksi();
                    }
                });
    }
}
