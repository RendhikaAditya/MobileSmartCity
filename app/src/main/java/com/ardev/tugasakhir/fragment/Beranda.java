package com.ardev.tugasakhir.fragment;


import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.ardev.tugasakhir.adapter.AdapterTagihan;
import com.ardev.tugasakhir.model.ModelTagih;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ardev.tugasakhir.R;
import com.ardev.tugasakhir.adapter.AdapterKonten;
import com.ardev.tugasakhir.adapter.AdapterKontenBeranda;
import com.ardev.tugasakhir.adapter.AdapterKontenBerandaWisata;
import com.ardev.tugasakhir.adapter.AdapterMenu;
import com.ardev.tugasakhir.adapter.SliderAdapterExample;
import com.ardev.tugasakhir.helper.Helper;
import com.ardev.tugasakhir.model.ModelKonten;
import com.ardev.tugasakhir.model.ModelMenu;
import com.ardev.tugasakhir.model.ModelSlider;
import com.ardev.tugasakhir.server.ServerApi;
import com.ardev.tugasakhir.util.AppController;
import com.ardev.tugasakhir.util.PrefManager;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class Beranda extends BaseFragment {

    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    ShimmerFrameLayout shimmer_beranda;
    NestedScrollView beranda_content;

    RelativeLayout layout_koneksi, layout_kosong;

    SliderView sliderView;
    SliderAdapterExample adapter;
    List<ModelSlider> mItems;

    AdapterMenu adapterMenu;
    List<ModelMenu> modelMenus;
    RecyclerView recyclerViewMenu;

    AdapterTagihan adapterTagihan;
    List<ModelTagih> modelTagihs;
    RecyclerView recycler_tagihan;

    TextView txt_greeting;
    PrefManager prefManager;

    AdapterKontenBerandaWisata mAdapterWisata;
    AdapterKontenBeranda mAdapterBudaya, mAdapterAcara;
    List<ModelKonten> mWisata, mBudaya, mAcara;
    RecyclerView recycle_wisata, recycle_budaya, recycle_acara ;

    public Beranda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_beranda, container, false);

        findView();
        initView();
        initListener();
        getData();

        return view;
    }

    @Override
    public void findView() {
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        beranda_content = view.findViewById(R.id.beranda_content);
        shimmer_beranda = view.findViewById(R.id.shimmer_beranda);

        layout_koneksi = view.findViewById(R.id.layout_koneksi);
        layout_kosong = view.findViewById(R.id.layout_kosong);

        sliderView = view.findViewById(R.id.imageSlider);
        recyclerViewMenu = view.findViewById(R.id.recycle_menu);

        recycler_tagihan = view.findViewById(R.id.recycle_tagihan);

        txt_greeting = view.findViewById(R.id.txt_greeting);

        recycle_wisata = view.findViewById(R.id.recycle_konten);
        recycle_budaya = view.findViewById(R.id.recycle_budaya);
        recycle_acara = view.findViewById(R.id.recycle_acara);

    }

    @Override
    public void initView() {
        mItems = new ArrayList<>();
        adapter = new SliderAdapterExample(mItems, getContext());
        adapter.setCount(4);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setCircularHandlerEnabled(true);
        sliderView.startAutoCycle();

        mWisata = new ArrayList<>();
        mAdapterWisata = new AdapterKontenBerandaWisata(mWisata, getContext());
        recycle_wisata.setAdapter(mAdapterWisata);
        mBudaya = new ArrayList<>();
        mAdapterBudaya = new AdapterKontenBeranda(mBudaya, getContext());
        recycle_budaya.setAdapter(mAdapterBudaya);
        mAcara = new ArrayList<>();
        mAdapterAcara = new AdapterKontenBeranda(mAcara, getContext());
        recycle_acara.setAdapter(mAdapterAcara);

        modelMenus = new ArrayList<>();
        adapterMenu = new AdapterMenu(modelMenus, getContext());
        recyclerViewMenu.setAdapter(adapterMenu);
        recyclerViewMenu.setHasFixedSize(true);
        recyclerViewMenu.setLayoutManager(new GridLayoutManager(getContext(),4));

        modelTagihs = new ArrayList<>();
        adapterTagihan = new AdapterTagihan(modelTagihs, getContext());
        recycler_tagihan.setAdapter(adapterTagihan);
        recycler_tagihan.setHasFixedSize(true);
        recycler_tagihan.setLayoutManager(new GridLayoutManager(getContext(), 4));

        prefManager = new PrefManager(getActivity().getApplicationContext());
        if (prefManager.getLoginStatus()) {
            txt_greeting.setText("Selamat "+ Helper.waktu()+", "+prefManager.getNamaUser().toUpperCase());
        }else{
            txt_greeting.setText("Selamat "+ Helper.waktu()+", warga");
        }



    }

    @Override
    public void initListener() {
        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
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
    }

    private void set_loading(){
        shimmer_beranda.setVisibility(View.VISIBLE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        beranda_content.setVisibility(View.GONE);
        shimmer_beranda.startShimmer();
    }

    private void load_success(){
        swipeRefreshLayout.setRefreshing(false);
        shimmer_beranda.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.GONE);
        layout_kosong.setVisibility(View.GONE);
        beranda_content.setVisibility(View.VISIBLE);
        shimmer_beranda.stopShimmer();
    }

    private void load_fail(){
        swipeRefreshLayout.setRefreshing(false);
        shimmer_beranda.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.VISIBLE);
        layout_kosong.setVisibility(View.GONE);
        beranda_content.setVisibility(View.GONE);
        shimmer_beranda.stopShimmer();
    }

    private void load_empty(){
        swipeRefreshLayout.setRefreshing(false);
        shimmer_beranda.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.VISIBLE);
        layout_kosong.setVisibility(View.GONE);
        beranda_content.setVisibility(View.GONE);
        shimmer_beranda.stopShimmer();
    }

    private void getData(){
        set_loading();
        StringRequest requestData = new StringRequest(Method.POST, ServerApi.beranda,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("volley", "response : "+response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray slider = jsonObject.getJSONArray("slider");
                            JSONArray menu_app = jsonObject.getJSONArray("menu_app");
                            JSONArray menu_tagih = jsonObject.getJSONArray("menu_tagih");
                            JSONArray wisata = jsonObject.getJSONArray("wisata");
                            JSONArray budaya = jsonObject.getJSONArray("budaya");
                            JSONArray acara = jsonObject.getJSONArray("acara");

                            //slider
                            mItems.clear();
                            adapter.setCount(slider.length());
                            for (int i = 0; i< slider.length(); i++){
                                JSONObject data_slider = slider.getJSONObject(i);
                                ModelSlider mdslider = new ModelSlider();
                                mdslider.setId_slider(data_slider.getString("id_slider"));
                                mdslider.setSlider(data_slider.getString("slider"));
                                mdslider.setCaption(data_slider.getString("caption"));
                                mdslider.setJenis(data_slider.getString("jenis"));
                                mItems.add(mdslider);
                            }
                            adapter.notifyDataSetChanged();

                            //menu app
                            modelMenus.clear();
                            for (int i = 0; i< menu_app.length(); i++){
                                JSONObject data_menu_app = menu_app.getJSONObject(i);
                                ModelMenu modelMenu = new ModelMenu();
                                modelMenu.setId_menu_app(data_menu_app.getString("id_menu_app"));
                                modelMenu.setTitle_menu_app(data_menu_app.getString("title_menu_app"));
                                modelMenu.setSubtitle_menu_app(data_menu_app.getString("subtitle_menu_app"));
                                modelMenu.setImage_menu_app(data_menu_app.getString("image_menu_app"));
                                modelMenus.add(modelMenu);
                            }
                            adapterMenu.notifyDataSetChanged();

                            //menu tagih
                            modelTagihs.clear();
                            for (int i = 0; i< menu_tagih.length(); i++){
                                JSONObject data_menu_app = menu_tagih.getJSONObject(i);
                                ModelTagih modelTagih = new ModelTagih();
                                modelTagih.setId_menu_app(data_menu_app.getString("id_menu_app"));
                                modelTagih.setTitle_menu_app(data_menu_app.getString("title_menu_app"));
                                modelTagih.setSubtitle_menu_app(data_menu_app.getString("subtitle_menu_app"));
                                modelTagih.setImage_menu_app(data_menu_app.getString("image_menu_app"));
                                modelTagihs.add(modelTagih);
                            }
                            adapterTagihan.notifyDataSetChanged();


                            //wisata app
                            mWisata.clear();
                            for (int i = 0; i< 5; i++){
                                JSONObject data_wisata = wisata.getJSONObject(i);
                                ModelKonten md = new ModelKonten();
                                md.setId_konten_app(data_wisata.getString("id_konten_app"));
                                md.setTitle_konten_app(data_wisata.getString("title_konten_app"));
                                md.setSubtitle_konten_app(data_wisata.getString("subtitle_konten_app"));
                                md.setImage_konten_app(data_wisata.getString("image_konten_app"));
                                md.setKonten_app(data_wisata.getString("konten_app"));
                                md.setLat(data_wisata.getString("lat"));
                                md.setLng(data_wisata.getString("lng"));
                                mWisata.add(md);
                            }
                            mAdapterWisata.notifyDataSetChanged();

                            //budaya app
                            mBudaya.clear();
                            for (int i = 0; i< 5; i++){
                                JSONObject data_budaya = budaya.getJSONObject(i);
                                ModelKonten md = new ModelKonten();
                                md.setId_konten_app(data_budaya.getString("id_konten_app"));
                                md.setTitle_konten_app(data_budaya.getString("title_konten_app"));
                                md.setSubtitle_konten_app(data_budaya.getString("subtitle_konten_app"));
                                md.setImage_konten_app(data_budaya.getString("image_konten_app"));
                                md.setKonten_app(data_budaya.getString("konten_app"));
                                md.setLat(data_budaya.getString("lat"));
                                md.setLng(data_budaya.getString("lng"));
                                mBudaya.add(md);
                            }
                            mAdapterBudaya.notifyDataSetChanged();

                            //acara app
                            mAcara.clear();
                            for (int i = 0; i< 5; i++){
                                JSONObject data_acara = acara.getJSONObject(i);
                                ModelKonten md = new ModelKonten();
                                md.setId_konten_app(data_acara.getString("id_konten_app"));
                                md.setTitle_konten_app(data_acara.getString("title_konten_app"));
                                md.setSubtitle_konten_app(data_acara.getString("subtitle_konten_app"));
                                md.setImage_konten_app(data_acara.getString("image_konten_app"));
                                md.setKonten_app(data_acara.getString("konten_app"));
                                md.setLat(data_acara.getString("lat"));
                                md.setLng(data_acara.getString("lng"));
                                mAcara.add(md);
                            }
                            mAdapterAcara.notifyDataSetChanged();

                            load_success();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            load_fail();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : "+error.getMessage());
                        if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                            load_fail();
                        }
                    }
                });
        AppController.getInstance().addToRequestQueue(requestData);
    }
}
