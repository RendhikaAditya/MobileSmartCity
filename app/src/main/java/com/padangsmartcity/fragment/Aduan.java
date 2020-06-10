package com.padangsmartcity.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.padangsmartcity.R;
import com.padangsmartcity.activity.BuatAduan;
import com.padangsmartcity.adapter.AdapterAduan;
import com.padangsmartcity.model.ModelAduan;
import com.padangsmartcity.server.ServerApi;
import com.padangsmartcity.util.AppController;
import com.padangsmartcity.util.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Aduan extends BaseFragment {

    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    AdapterAduan mAdapter;
    List<ModelAduan> mItems;
    RecyclerView recycle_aduan;
    RelativeLayout layout_koneksi, layout_kosong;
    ShimmerFrameLayout shimmer_aduan;
    Bundle bundle;

    ProgressBar progres_loadmore;
    LinearLayout load_done;
    TextView txt_load_done;
    int halaman = 1;

    FloatingActionButton fab;
    PrefManager prefManager;

    public Aduan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_aduan, container, false);
        findView();
        initView();
        initListener();
        getData(1);

        return view;
    }

    @Override
    public void findView() {
        recycle_aduan = view.findViewById(R.id.recycle_aduan);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        layout_koneksi = view.findViewById(R.id.layout_koneksi);
        layout_kosong = view.findViewById(R.id.layout_kosong);
        shimmer_aduan = view.findViewById(R.id.shimmer_aduan);

        progres_loadmore = view.findViewById(R.id.progres_loadmore);
        load_done = view.findViewById(R.id.load_done);
        txt_load_done = view.findViewById(R.id.txt_load_done);

        fab = view.findViewById(R.id.fab);
    }

    @Override
    public void initView() {
        mItems = new ArrayList<>();
        mAdapter = new AdapterAduan(mItems, getContext());
        recycle_aduan.setAdapter(mAdapter);
        prefManager = new PrefManager(getActivity().getApplicationContext());
        if (prefManager.getLoginStatus()) {
            // kalau user ternyata udah login langsung di lempar ke main activity tanpa harus login terlebih dahulu
            fab.show();
        }else{
            fab.hide();
        }
    }

    @Override
    public void initListener() {
        layout_koneksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(1);
                halaman = 1;
            }
        });

        layout_kosong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData(1);
                halaman = 1;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(1);
                halaman = 1;
            }
        });

        recycle_aduan.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy < 0) {//scroll up
//                    fab.show();
                    load_done.setVisibility(View.GONE);
                    progres_loadmore.setVisibility(View.GONE);
                } else if (dy > 0) { //scroll down
//                    fab.hide();
                    if(recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN) == false){ //reach end
                        halaman += 1;
                        getMoreData(halaman);
                        Log.d("onScrolled", "halaman: "+halaman);
                    }
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), BuatAduan.class);
                startActivity(i);
            }
        });
    }

    private void set_loading(){
        shimmer_aduan.setVisibility(View.VISIBLE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_aduan.setVisibility(View.GONE);
        shimmer_aduan.startShimmer();
    }

    private void load_success(){
        shimmer_aduan.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_aduan.setVisibility(View.VISIBLE);
        shimmer_aduan.stopShimmer();
    }

    private void load_fail(){
        shimmer_aduan.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.VISIBLE);
        layout_kosong.setVisibility(View.GONE);
        recycle_aduan.setVisibility(View.GONE);
        shimmer_aduan.stopShimmer();
    }

    private void load_empty(){
        shimmer_aduan.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.VISIBLE);
        recycle_aduan.setVisibility(View.GONE);
        shimmer_aduan.stopShimmer();
    }

    private void set_loadingmore(){
        progres_loadmore.setVisibility(View.VISIBLE);
        load_done.setVisibility(View.GONE);
    }

    private void loadmore_end(){
        progres_loadmore.setVisibility(View.GONE);
        load_done.setVisibility(View.VISIBLE);
        txt_load_done.setText("SELESAI");
    }

    private void loadmore_fail(){
        progres_loadmore.setVisibility(View.GONE);
        load_done.setVisibility(View.VISIBLE);
        txt_load_done.setText("TIDAK DAPAT MEMUAT DATA");
    }

    private void loadmore_success(){
        progres_loadmore.setVisibility(View.GONE);
        load_done.setVisibility(View.GONE);
    }

    private void getData(int page){
        set_loading();
        JsonArrayRequest requestData = new JsonArrayRequest(Request.Method.POST, ServerApi.aduan+"?halaman="+page, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("volley", "response : "+response.toString());
                        if (response.length() > 0){
                            load_success();
                            mItems.clear();
                            for (int i = 0; i< response.length(); i++){
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    ModelAduan md = new ModelAduan();
                                    md.setId_aduan(data.getString("id_aduan"));
                                    md.setIsi_aduan(data.getString("isi_aduan"));
                                    md.setStatus_aduan(data.getString("status_aduan"));
                                    md.setFoto_aduan(data.getString("foto_aduan"));
                                    md.setLng(data.getString("lng"));
                                    md.setLat(data.getString("lat"));
                                    md.setTanggal_aduan(data.getString("tanggal_aduan"));
                                    md.setNama_user(data.getString("nama_user"));
                                    md.setThumb_user(data.getString("thumb_user"));
                                    md.setId_user(data.getString("id_user"));
                                    md.setTanggapan(data.getString("tanggapan"));
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : "+error.getMessage());
                        if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                            load_fail();
                        }else{
                            load_fail();
                        }
                    }
                });
        AppController.getInstance().addToRequestQueue(requestData);
    }

    private void getMoreData(final int page){
        set_loadingmore();
        JsonArrayRequest requestData = new JsonArrayRequest(Request.Method.POST, ServerApi.aduan+"?halaman="+page, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("volley", "response : "+response.toString());
                        if (response.length() > 0){
                            loadmore_success();
                            mItems.clear();
                            for (int i = 0; i< response.length(); i++){
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    ModelAduan md = new ModelAduan();
                                    md.setId_aduan(data.getString("id_aduan"));
                                    md.setIsi_aduan(data.getString("isi_aduan"));
                                    md.setStatus_aduan(data.getString("status_aduan"));
                                    md.setFoto_aduan(data.getString("foto_aduan"));
                                    md.setLng(data.getString("lng"));
                                    md.setLat(data.getString("lat"));
                                    md.setTanggal_aduan(data.getString("tanggal_aduan"));
                                    md.setNama_user(data.getString("nama_user"));
                                    md.setThumb_user(data.getString("thumb_user"));
                                    md.setId_user(data.getString("id_user"));
                                    md.setTanggapan(data.getString("tanggapan"));
                                    md.setVerified(data.getString("verified"));
                                    mItems.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }else{
                            loadmore_end();
                            halaman -= 1;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : "+error.getMessage());
                        if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                            loadmore_fail();
                        }else{
                            loadmore_fail();
                        }
                        halaman -= 1;
                    }
                });
        AppController.getInstance().addToRequestQueue(requestData);
    }
}
