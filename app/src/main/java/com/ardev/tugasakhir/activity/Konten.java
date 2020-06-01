package com.ardev.tugasakhir.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ardev.tugasakhir.R;
import com.ardev.tugasakhir.adapter.AdapterKonten;
import com.ardev.tugasakhir.model.ModelKonten;
import com.ardev.tugasakhir.server.ServerApi;
import com.ardev.tugasakhir.util.AppController;
import com.ardev.tugasakhir.util.SetUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Konten extends BaseActivity {

    SwipeRefreshLayout swipeRefreshLayout;

    AdapterKonten mAdapter;
    List<ModelKonten> mItems;
    RecyclerView recycle_konten;
    RelativeLayout layout_koneksi, layout_kosong;
    ShimmerFrameLayout shimmer_konten;
    Bundle bundle;



    ProgressBar progres_loadmore;
    LinearLayout load_done;
    TextView txt_load_done;
    int halaman = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konten);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        
        findView();
        initView();
        initListener();
        getData(1);

        AndroidNetworking.initialize(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void findView() {
        recycle_konten = findViewById(R.id.recycle_konten);
        swipeRefreshLayout = findViewById(R.id.swipe);
        layout_koneksi = findViewById(R.id.layout_koneksi);
        layout_kosong = findViewById(R.id.layout_kosong);
        shimmer_konten = findViewById(R.id.shimmer_konten);

        progres_loadmore = findViewById(R.id.progres_loadmore);
        load_done = findViewById(R.id.load_done);
        txt_load_done = findViewById(R.id.txt_load_done);
    }

    @Override
    public void initView() {
        mItems = new ArrayList<>();
        mAdapter = new AdapterKonten(mItems, getApplicationContext());
        recycle_konten.setAdapter(mAdapter);
        bundle = getIntent().getExtras();

        getSupportActionBar().setTitle(Html.fromHtml("<b>"+bundle.getString("title_submenu_app")+"</b>"));
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

        recycle_konten.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy < 0) {//scroll up
                    load_done.setVisibility(View.GONE);
                    progres_loadmore.setVisibility(View.GONE);
                } else if (dy > 0) { //scroll down
                    if(recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN) == false){ //reach end
                        halaman += 1;
                        getMoreData(halaman);
                        Log.d("onScrolled", "halaman: "+halaman);
                    }
                }
            }
        });
    }

    private void set_loading(){
        shimmer_konten.setVisibility(View.VISIBLE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_konten.setVisibility(View.GONE);
        shimmer_konten.startShimmer();
    }

    private void load_success(){
        shimmer_konten.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        recycle_konten.setVisibility(View.VISIBLE);
        shimmer_konten.stopShimmer();
    }

    private void load_fail(){
        shimmer_konten.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.VISIBLE);
        layout_kosong.setVisibility(View.GONE);
        recycle_konten.setVisibility(View.GONE);
        shimmer_konten.stopShimmer();
    }

    private void load_empty(){
        shimmer_konten.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.VISIBLE);
        recycle_konten.setVisibility(View.GONE);
        shimmer_konten.stopShimmer();
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

    private void getData(int page){
        set_loading();
        AndroidNetworking.post("http://hotel.depotmart.id/api/konten.php")
                .addBodyParameter("kategori_id",bundle.getString("id_submenu_app"))
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
                                    ModelKonten md = new ModelKonten();
                                    md.setId_konten_app(data.getString("id_konten_app"));
                                    md.setTitle_konten_app(data.getString("title_konten_app"));
                                    md.setSubtitle_konten_app(data.getString("subtitle_konten_app"));
                                    md.setImage_konten_app(data.getString("image_konten_app"));
                                    md.setKonten_app(data.getString("konten_app"));
                                    md.setLat(data.getString("lat"));
                                    md.setLng(data.getString("lng"));
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
//    private void getData(int page){
//        set_loading();
//        JsonArrayRequest requestData = new JsonArrayRequest(Request.Method.POST, ServerApi.konten_app+"?id="+bundle.getString("id_submenu_app")+"&halaman="+page, null,
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
//                                    ModelKonten md = new ModelKonten();
//                                    md.setId_konten_app(data.getString("id_konten_app"));
//                                    md.setTitle_konten_app(data.getString("title_konten_app"));
//                                    md.setSubtitle_konten_app(data.getString("subtitle_konten_app"));
//                                    md.setImage_konten_app(data.getString("image_konten_app"));
//                                    md.setKonten_app(data.getString("konten_app"));
//                                    md.setLat(data.getString("lat"));
//                                    md.setLng(data.getString("lng"));
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

    private void getMoreData(final int page){
        set_loadingmore();
        JsonArrayRequest requestData = new JsonArrayRequest(Request.Method.POST, ServerApi.konten_app+"?id="+bundle.getString("id_submenu_app")+"&halaman="+page, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("volley", "response : "+response.toString());
                        if (response.length() > 0){
                            for (int i = 0; i< response.length(); i++){
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    ModelKonten md = new ModelKonten();
                                    md.setId_konten_app(data.getString("id_konten_app"));
                                    md.setTitle_konten_app(data.getString("title_konten_app"));
                                    md.setSubtitle_konten_app(data.getString("subtitle_konten_app"));
                                    md.setImage_konten_app(data.getString("image_konten_app"));
                                    md.setKonten_app(data.getString("konten_app"));
                                    md.setLat(data.getString("lat"));
                                    md.setLng(data.getString("lng"));
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
