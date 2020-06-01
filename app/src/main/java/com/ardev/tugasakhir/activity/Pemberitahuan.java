package com.ardev.tugasakhir.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.ardev.tugasakhir.R;
import com.ardev.tugasakhir.adapter.AdapterPemberitahuan;
import com.ardev.tugasakhir.model.ModelPemberitahuan;
import com.ardev.tugasakhir.server.ServerApi;
import com.ardev.tugasakhir.util.AppController;
import com.ardev.tugasakhir.util.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Pemberitahuan extends BaseActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    AdapterPemberitahuan mAdapter;
    List<ModelPemberitahuan> mItems;
    RecyclerView recycle_pemberitahuan;
    RelativeLayout layout_koneksi, layout_kosong, layout_akun;
    ShimmerFrameLayout shimmer_pemberitahuan;
    PrefManager prefManager;
    String id_user;
    TextView txt_kosong;

    Button btn_masuk, btn_daftar;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemberitahuan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(Html.fromHtml("<b>PEMBERITAHUAN</b>"));
        findView();
        initView();
        initListener();

        AndroidNetworking.initialize(this);
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_pemberitahuan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_delete:
                //action here
                new AlertDialog.Builder(this)
                        .setTitle("Perhatian")
                        .setMessage("Apakah anda yakin akan menghapus semua pemberitahuan?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete(id_user);
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        login_view();
    }

    @Override
    public void findView() {
        recycle_pemberitahuan = findViewById(R.id.recycle_pemberitahuan);
        swipeRefreshLayout = findViewById(R.id.swipe);
        layout_koneksi = findViewById(R.id.layout_koneksi);
        layout_kosong = findViewById(R.id.layout_kosong);
        layout_akun = findViewById(R.id.layout_login);
        shimmer_pemberitahuan = findViewById(R.id.shimmer_pemberitahuan);
        txt_kosong = findViewById(R.id.txt_kosong);

        btn_masuk = findViewById(R.id.btn_masuk);
        btn_daftar = findViewById(R.id.btn_daftar);
    }

    @Override
    public void initView() {
        mItems = new ArrayList<>();
        mAdapter = new AdapterPemberitahuan(mItems, getApplicationContext());
        recycle_pemberitahuan.setAdapter(mAdapter);
        prefManager = new PrefManager(this);
        login_view();
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Sedang menghapus pemberitahuan...")
                .setCancelable(false)
                .build();
    }

    @Override
    public void initListener() {
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
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Daftar.class);
                startActivity(i);
            }
        });
    }

    private void login_view(){
        prefManager = new PrefManager(getApplicationContext());
        if (prefManager.getLoginStatus()) {
            id_user = prefManager.getIdUser();
            layout_akun.setVisibility(View.GONE);
            getData();
        }else{
            layout_akun.setVisibility(View.VISIBLE);
            shimmer_pemberitahuan.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            layout_koneksi.setVisibility(View.GONE);
            layout_kosong.setVisibility(View.GONE);
            recycle_pemberitahuan.setVisibility(View.GONE);
            shimmer_pemberitahuan.stopShimmer();
        }
    }

    private void set_not_login(){
        shimmer_pemberitahuan.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_pemberitahuan.setVisibility(View.GONE);
        layout_akun.setVisibility(View.VISIBLE);

    }

    private void set_loading(){
        shimmer_pemberitahuan.setVisibility(View.VISIBLE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_pemberitahuan.setVisibility(View.GONE);
        shimmer_pemberitahuan.startShimmer();
    }

    private void load_success(){
        shimmer_pemberitahuan.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_pemberitahuan.setVisibility(View.VISIBLE);
        shimmer_pemberitahuan.stopShimmer();
    }

    private void load_fail(){
        shimmer_pemberitahuan.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.VISIBLE);
        layout_kosong.setVisibility(View.GONE);
        recycle_pemberitahuan.setVisibility(View.GONE);
        shimmer_pemberitahuan.stopShimmer();
    }

    private void load_empty(){
        shimmer_pemberitahuan.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.VISIBLE);
        recycle_pemberitahuan.setVisibility(View.GONE);
        shimmer_pemberitahuan.stopShimmer();
        txt_kosong.setText("Anda tidak mempunyai pemberitahuan");
    }

    private void getData(){
        set_loading();
        AndroidNetworking.post("http://hotel.depotmart.id/api/pemeritahuan.php")
                .addBodyParameter("id_user", id_user)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0){
                            load_success();
                            mItems.clear();
                            for (int i = 0; i< response.length(); i++){
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    ModelPemberitahuan md = new ModelPemberitahuan();
                                    md.setId_pemberitahuan(data.getString("id_pemberitahuan"));
                                    md.setId_user(data.getString("id_user"));
                                    md.setIsi_pemberitahuan(data.getString("isi_pemberitahuan"));
                                    md.setTanggal(data.getString("tanggal"));
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
                        load_fail();
                    }
                });
    }

//    private void getData(){
//        set_loading();
//        JsonArrayRequest requestData = new JsonArrayRequest(Request.Method.POST, ServerApi.pemberitahuan+id_user, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("volley", "response : "+response.toString());
//                        if (response.length() > 0){
//                            load_success();
//                            mItems.clear();
//                            for (int i = 0; i< response.length(); i++){
//                                try {
//                                    JSONObject data = response.getJSONObject(i);
//                                    ModelPemberitahuan md = new ModelPemberitahuan();
//                                    md.setId_pemberitahuan(data.getString("id_pemberitahuan"));
//                                    md.setId_user(data.getString("id_user"));
//                                    md.setIsi_pemberitahuan(data.getString("isi_pemberitahuan"));
//                                    md.setTanggal(data.getString("tanggal"));
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

    private void delete(final String id_user){
        dialog.show();
        AndroidNetworking.post("http://hotel.depotmart.id/api/delete_pemberitahuan.php")
                .addBodyParameter("id_user", id_user)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        try
                        {
//                            JSONObject data = new JSONObject(response);
                            String code = data.getString("code");
                            dialog.dismiss();
                            // ngecek node error dari api
                            if (code.equals("1")) {
                                snackBar(data.getString("response"), R.color.success);
                                getData();
                            } else if(code.equals("0")) {
                                // terjadi error dan tampilkan pesan error dari API
                                //                        dialog.hide();
                                snackBar(data.getString("response"), R.color.error);
                            }else{
                                snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
                            }
                        } catch (JSONException e) {
                            // JSON error
                            dialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Volley", "Login Error: " + anError.getMessage());
                        dialog.dismiss();
                        //cek error timeout, noconnection dan network error

                        snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);

                    }
                });
    }


//    private void delete(final String id_user) {
//        dialog.show();
//        // Tag biasanya digunakan ketika ingin membatalkan request volley
//        String tag_string_req = "req_login";
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ServerApi.delete_pemberitahuan, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("volley", "Login Response: " + response.toString());
//
//                try
//                {
//                    JSONObject data = new JSONObject(response);
//                    String code = data.getString("code");
//                    dialog.dismiss();
//                    // ngecek node error dari api
//                    if (code.equals("1")) {
//                        snackBar(data.getString("response"), R.color.success);
//                        getData();
//                    } else if(code.equals("0")) {
//                        // terjadi error dan tampilkan pesan error dari API
////                        dialog.hide();
//                        snackBar(data.getString("response"), R.color.error);
//                    }else{
//                        snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
//                    }
//                } catch (JSONException e) {
//                    // JSON error
//                    dialog.dismiss();
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley", "Login Error: " + error.getMessage());
//                dialog.dismiss();
//                //cek error timeout, noconnection dan network error
//                if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
//                    snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // kirim parameter ke server
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id_user", id_user);
//
//                return params;
//            }
//        };
//        // menggunakan fungsi volley adrequest yang kita taro di appcontroller
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }

    private void snackBar(String pesan, int warna){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), pesan, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), warna));
        snackbar.show();
    }
}
