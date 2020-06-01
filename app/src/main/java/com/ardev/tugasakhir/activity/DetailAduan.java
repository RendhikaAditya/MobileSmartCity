package com.ardev.tugasakhir.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ardev.tugasakhir.R;
import com.ardev.tugasakhir.adapter.AdapterTanggapan;
import com.ardev.tugasakhir.model.ModelTanggapan;
import com.ardev.tugasakhir.server.ServerApi;
import com.ardev.tugasakhir.util.AppController;
import com.ardev.tugasakhir.util.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class DetailAduan extends BaseActivity {

    TextView nama_user, tanggal_aduan, isi_aduan, tanggapan, status_aduan, txt_kosong;
    ImageView thumb_user, foto_aduan, verified;
    Bundle bundle;
    CardView card_foto_aduan;

    AdapterTanggapan mAdapter;
    List<ModelTanggapan> mItems;
    RecyclerView recycle_tanggapan;
    RelativeLayout layout_koneksi, layout_kosong;
    ProgressBar progress_tanggapan;

    LinearLayout input_tanggapan;
    TextView txt_isi_tanggapan;
    FloatingActionButton fab_kirim;

    PrefManager prefManager;
    AlertDialog dialog;
    String id_user, id_aduan, id_user_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_aduan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(Html.fromHtml("<b>DETAIL ADUAN</b>"));
        findView();
        initView();
        initListener();
        getData();
        AndroidNetworking.initialize(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void findView() {
        nama_user = findViewById(R.id.nama_user);
        tanggal_aduan = findViewById(R.id.tanggal_aduan);
        isi_aduan = findViewById(R.id.isi_aduan);
        tanggapan = findViewById(R.id.tanggapan);
        status_aduan = findViewById(R.id.status_aduan);
        thumb_user = findViewById(R.id.thumb_user);
        foto_aduan = findViewById(R.id.foto_aduan);
        card_foto_aduan = findViewById(R.id.card_foto_aduan);
        verified = findViewById(R.id.verified);

        txt_kosong = findViewById(R.id.txt_kosong);

        recycle_tanggapan = findViewById(R.id.recycle_tanggapan);
        layout_koneksi = findViewById(R.id.layout_koneksi);
        layout_kosong = findViewById(R.id.layout_kosong);
        progress_tanggapan = findViewById(R.id.progress_tanggapan);

        input_tanggapan = findViewById(R.id.input_tanggapan);
        txt_isi_tanggapan = findViewById(R.id.txt_isi_tanggapan);
        fab_kirim = findViewById(R.id.fab_kirim);
    }

    @Override
    public void initView() {
        prefManager = new PrefManager(getApplicationContext());
        if (prefManager.getLoginStatus()) {
            input_tanggapan.setVisibility(View.VISIBLE);
        }else{
            input_tanggapan.setVisibility(View.GONE);
        }
        id_user = prefManager.getIdUser();
        bundle = getIntent().getExtras();
        id_user_a = bundle.getString("id_user");
        id_aduan = bundle.getString("id_aduan");

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Sedang mengirim tanggapan...")
                .setCancelable(false)
                .build();

        nama_user.setText(bundle.getString("nama_user"));
        tanggal_aduan.setText(bundle.getString("tanggal_aduan"));
        isi_aduan.setText(bundle.getString("isi_aduan"));
        tanggapan.setText(bundle.getString("tanggapan"));
        status_aduan.setText(bundle.getString("status_aduan"));

        if (bundle.getString("verified").equals("0")){
            verified.setVisibility(View.GONE);
        }else{
            verified.setVisibility(View.VISIBLE);
        }

        if (bundle.getString("foto_aduan").equals("null")){
            card_foto_aduan.setVisibility(View.GONE);
        }else{
            try {
                Glide.with(this)
                        .load(new URL(bundle.getString("foto_aduan")))
                        .placeholder(R.drawable.no_image)
                        .into(foto_aduan);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        try {
            Glide.with(this)
                    .load(new URL(bundle.getString("thumb_user")))
                    .placeholder(R.drawable.no_image)
                    .into(thumb_user);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        mItems = new ArrayList<>();
        mAdapter = new AdapterTanggapan(mItems, getApplicationContext());
        recycle_tanggapan.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        card_foto_aduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LihatFoto.class);
                i.putExtra("image", bundle.getString("foto_aduan"));
                i.putExtra("title", "FOTO ADUAN");
                startActivity(i);
            }
        });

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

        fab_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_isi_tanggapan.getText().toString().isEmpty()){
                    txt_isi_tanggapan.setError("Tidak dapat mengirimkan tanggapan kosong");
                }else{
                    kirim_tanggapan(id_user_a, id_user, id_aduan, txt_isi_tanggapan.getText().toString());
                }
            }
        });
    }

    private void set_loading(){
        progress_tanggapan.setVisibility(View.VISIBLE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_tanggapan.setVisibility(View.GONE);
    }

    private void load_success(){
        progress_tanggapan.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_tanggapan.setVisibility(View.VISIBLE);
    }

    private void load_fail(){
        progress_tanggapan.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.VISIBLE);
        layout_kosong.setVisibility(View.GONE);
        recycle_tanggapan.setVisibility(View.GONE);
    }

    private void load_empty(){
        progress_tanggapan.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.VISIBLE);
        recycle_tanggapan.setVisibility(View.GONE);
        txt_kosong.setText("Belum ada tanggapan. Jadilah yang pertama menanggapi aduan ini.");
    }

    private void getData() {
        set_loading();
        AndroidNetworking.post("http://hotel.depotmart.id/api/get_tanggapan.php")
                .addBodyParameter("id_aduan", bundle.getString("id_aduan"))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        tanggapan.setText(String.valueOf(response.length()));
                        if (response.length() > 0){
                            load_success();
                            mItems.clear();
                            for (int i = 0; i< response.length(); i++){
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    ModelTanggapan md = new ModelTanggapan();
                                    md.setId_tanggapan(data.getString("id_tanggapan"));
                                    md.setIsi_tanggapan(data.getString("isi_tanggapan"));
                                    md.setNama_admin(data.getString("nama_admin"));
                                    md.setThumb_admin(data.getString("thumb_admin"));
                                    md.setNama_user(data.getString("nama_user"));
                                    md.setThumb_user(data.getString("thumb_user"));
                                    md.setTanggal_tanggapan(data.getString("tanggal_tanggapan"));
                                    md.setVerified(data.getString("verified"));
                                    mItems.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }else{
                            load_empty();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("volley", "error : "+anError.getMessage());

                            load_empty();

                    }
                });
    }

//    private void getData(){
//        set_loading();
//        JsonArrayRequest requestData = new JsonArrayRequest(Request.Method.POST, ServerApi.tanggapan+"?id="+bundle.getString("id_aduan"), null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("volley", "response : "+response.toString());
//                        tanggapan.setText(String.valueOf(response.length()));
//                        if (response.length() > 0){
//                            load_success();
//                            mItems.clear();
//                            for (int i = 0; i< response.length(); i++){
//                                try {
//                                    JSONObject data = response.getJSONObject(i);
//                                    ModelTanggapan md = new ModelTanggapan();
//                                    md.setId_tanggapan(data.getString("id_tanggapan"));
//                                    md.setIsi_tanggapan(data.getString("isi_tanggapan"));
//                                    md.setNama_admin(data.getString("nama_admin"));
//                                    md.setThumb_admin(data.getString("thumb_admin"));
//                                    md.setNama_user(data.getString("nama_user"));
//                                    md.setThumb_user(data.getString("thumb_user"));
//                                    md.setTanggal_tanggapan(data.getString("tanggal_tanggapan"));
//                                    md.setVerified(data.getString("verified"));
//                                    mItems.add(md);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            mAdapter.notifyDataSetChanged();
//                        }else{
//                            load_empty();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("volley", "error : "+error.getMessage());
//                        if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
//                            load_fail();
//                        }else{
//                            load_fail();
//                        }
//                    }
//                });
//        AppController.getInstance().addToRequestQueue(requestData);
//    }

//        private void kirim_tanggapan(final String id_user_a, final String id_user, final String id_aduan, final String isi_tanggapan){
//            dialog.show();
//            AndroidNetworking.post("")
//        }


    private void kirim_tanggapan(final String id_user_a, final String id_user, final String id_aduan, final String isi_tanggapan){
        dialog.show();
        // Tag biasanya digunakan ketika ingin membatalkan request volley
        String tag_string_req = "req_kirim_aduan";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServerApi.buat_tanggapan, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("volley", "kirim aduan Response: " + response.toString());
                dialog.dismiss();
                try
                {
                    JSONObject data = new JSONObject(response);
                    String code = data.getString("code");
                    // ngecek node error dari api
                    if (code.equals("1")) {
                        mItems.clear();
                        getData();
                        txt_isi_tanggapan.setText("");
                    } else if(code.equals("2")) {
                        // terjadi error dan tampilkan pesan error dari API
//                        dialog.hide();
                        snackBar(data.getString("response"), R.color.error);
                    } else if(code.equals("0")) {
                        // terjadi error dan tampilkan pesan error dari API
//                        dialog.hide();
                        snackBar(data.getString("response"), R.color.error);
                    }else{
                        snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Login Error: " + error.getMessage());
                dialog.dismiss();
                //cek error timeout, noconnection dan network error
                if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                    snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // kirim parameter ke server
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user_a", id_user_a);
                params.put("id_user", id_user);
                params.put("id_aduan", id_aduan);
                params.put("isi_tanggapan", isi_tanggapan);

                return params;
            }
        };
        // menggunakan fungsi volley adrequest yang kita taro di appcontroller
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    private void snackBar(String pesan, int warna){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), pesan, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), warna));
        snackbar.show();
    }
}
