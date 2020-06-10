package com.padangsmartcity.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.padangsmartcity.R;
import com.padangsmartcity.model.ModelRt;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Statistik extends Fragment {

    TextView laki, perem, islam, kristen, hindu, budha, khatolik, konghucu, rt1, rt2, rt3, rt4, rt5, rw1, rw2, rw3, rw4, rw5, u1, u2, u3, u4, u5, u6;

    ShimmerFrameLayout shimmer_profil;
    RelativeLayout layout_login, layout_koneksi;
    ScrollView scroll_profil;

    ImageView barl, pbar, baris, barkr,barhi ,barbu, barkh,barko, barrt1, barrt2, barrt3, barrt4, barrt5, barrw1, barrw2, barrw3, barrw4, barrw5, baru1, baru2, baru3, baru4, baru5, baru6;

    private List<ModelRt> rt;
    RecyclerView rvrt;


    public Statistik() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_statistik, container, false);

        layout_koneksi = view.findViewById(R.id.layout_koneksi);
        shimmer_profil = view.findViewById(R.id.shimmer_profil);
        scroll_profil = view.findViewById(R.id.scroll_statistik);

        laki = view.findViewById(R.id.jmllaki);
        perem = view.findViewById(R.id.jmlperempuan);

        islam = view.findViewById(R.id.jmlislam);
        kristen = view.findViewById(R.id.jmlkristen);
        hindu = view.findViewById(R.id.jmlhindu);
        budha = view.findViewById(R.id.jmlbudha);
        khatolik = view.findViewById(R.id.jmlkhatolik);
        konghucu = view.findViewById(R.id.jmlkonghucu);

        rt1 = view.findViewById(R.id.jmlrt1);
        rt2 = view.findViewById(R.id.jmlrt2);
        rt3 = view.findViewById(R.id.jmlrt3);
        rt4 = view.findViewById(R.id.jmlrt4);
        rt5 = view.findViewById(R.id.jmlrt5);

        rw1 = view.findViewById(R.id.jmlrw1);
        rw2 = view.findViewById(R.id.jmlrw2);
        rw3 = view.findViewById(R.id.jmlrw3);
        rw4 = view.findViewById(R.id.jmlrw4);
        rw5 = view.findViewById(R.id.jmlrw5);

        u1 = view.findViewById(R.id.jmlu1);
        u2 = view.findViewById(R.id.jmlu2);
        u3 = view.findViewById(R.id.jmlu3);
        u4 = view.findViewById(R.id.jmlu4);
        u5 = view.findViewById(R.id.jmlu5);
        u6 = view.findViewById(R.id.jmlu6);



        barl = view.findViewById(R.id.barl);
        pbar = view.findViewById(R.id.pbar);

        baris = view.findViewById(R.id.baris);
        barkr = view.findViewById(R.id.barkr);
        barbu = view.findViewById(R.id.barbu);
        barhi = view.findViewById(R.id.barhi);
        barkh = view.findViewById(R.id.barkh);
        barko = view.findViewById(R.id.barko);

        barrt1 = view.findViewById(R.id.barrt1);
        barrt2 = view.findViewById(R.id.barrt2);
        barrt3 = view.findViewById(R.id.barrt3);
        barrt4 = view.findViewById(R.id.barrt4);
        barrt5 = view.findViewById(R.id.barrt5);

        barrw1 = view.findViewById(R.id.barrw1);
        barrw2 = view.findViewById(R.id.barrw2);
        barrw3 = view.findViewById(R.id.barrw3);
        barrw4 = view.findViewById(R.id.barrw4);
        barrw5 = view.findViewById(R.id.barrw5);


        baru1 = view.findViewById(R.id.baru1);
        baru2 = view.findViewById(R.id.baru2);
        baru3 = view.findViewById(R.id.baru3);
        baru4 = view.findViewById(R.id.baru4);
        baru5 = view.findViewById(R.id.baru5);
        baru6 = view.findViewById(R.id.baru6);

        AndroidNetworking.initialize(getContext());

        getData();


        return view;
    }

    private void set_loading(){
        shimmer_profil.setVisibility(View.VISIBLE);
        scroll_profil.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.GONE);
        shimmer_profil.startShimmer();
    }

    private void load_success(){
        shimmer_profil.setVisibility(View.GONE);
        scroll_profil.setVisibility(View.VISIBLE);
        layout_koneksi.setVisibility(View.GONE);
        shimmer_profil.stopShimmer();
    }

    private void load_fail(){
        shimmer_profil.setVisibility(View.GONE);
        scroll_profil.setVisibility(View.GONE);
        layout_koneksi.setVisibility(View.VISIBLE);
        shimmer_profil.stopShimmer();
    }


    private void getData() {
        set_loading();
        AndroidNetworking.post("http://hotel.depotmart.id/api/get_warga.php")
                .addBodyParameter("id","a")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        try {
                            JSONObject response = responses.getJSONObject(0);
                            laki.setText(response.getString("Pria"));
                            perem.setText(response.getString("Wanita"));

                            int l = response.getInt("Pria");
                            int p = response.getInt("Wanita");

                            barl.getLayoutParams().width=l*60;
                            pbar.getLayoutParams().width=p*60;


                            barl.setColorFilter(ContextCompat.getColor(getContext(), R.color.barD));
                            pbar.setColorFilter(ContextCompat.getColor(getContext(), R.color.barE));



                            load_success();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            load_fail();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Error", "error statis gender: "+anError);
                        load_fail();
                    }
                });

        AndroidNetworking.post("http://hotel.depotmart.id/api/get_warga.php")
                .addBodyParameter("id","b")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        try {
                            JSONObject response = responses.getJSONObject(0);
                                islam.setText(response.getString("Islam"));
                                kristen.setText(response.getString("Kristen Protestan"));
                                budha.setText(response.getString("Buddha"));
                                hindu.setText(response.getString("Hindu"));
                                khatolik.setText(response.getString("Kristen Katolik"));
                                konghucu.setText(response.getString("Konghucu"));

                                int is = response.getInt("Islam");
                                int kr = response.getInt("Kristen Protestan");
                                int bu = response.getInt("Buddha");
                                int hi = response.getInt("Hindu");
                                int kh = response.getInt("Kristen Katolik");
                                int ko = response.getInt("Konghucu");

                                baris.getLayoutParams().width=is*60;
                                barkr.getLayoutParams().width=kr*60;
                                barbu.getLayoutParams().width=bu*60;
                                barhi.getLayoutParams().width=hi*60;
                                barkh.getLayoutParams().width=kh*60;
                                barko.getLayoutParams().width=ko*60;

                                baris.setColorFilter(ContextCompat.getColor(getContext(), R.color.barA));
                                barkr.setColorFilter(ContextCompat.getColor(getContext(), R.color.barB));
                                barbu.setColorFilter(ContextCompat.getColor(getContext(), R.color.barC));
                                barhi.setColorFilter(ContextCompat.getColor(getContext(), R.color.barD));
                                barkh.setColorFilter(ContextCompat.getColor(getContext(), R.color.barE));
                                barko.setColorFilter(ContextCompat.getColor(getContext(), R.color.barB));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        AndroidNetworking.post("http://hotel.depotmart.id/api/get_warga.php")
                .addBodyParameter("id","rt")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        if(responses.length()>0){
                            load_success();
                            Log.d("sukses kat", "onResponse" + responses);
                            JSONObject response = null;
                            try {
                                response = responses.getJSONObject(0);
                                rt1.setText(response.getString("1"));
                                rt2.setText(response.getString("2"));
                                rt3.setText(response.getString("3"));
                                rt4.setText(response.getString("4"));
                                rt5.setText(response.getString("5"));

                                int is = response.getInt("1");
                                int kr = response.getInt("2");
                                int bu = response.getInt("3");
                                int hi = response.getInt("4");
                                int kh = response.getInt("5");

                                barrt1.getLayoutParams().width=is*60;
                                barrt2.getLayoutParams().width=kr*60;
                                barrt3.getLayoutParams().width=bu*60;
                                barrt4.getLayoutParams().width=hi*60;
                                barrt5.getLayoutParams().width=kh*60;

                                barrt1.setColorFilter(ContextCompat.getColor(getContext(), R.color.barE));
                                barrt2.setColorFilter(ContextCompat.getColor(getContext(), R.color.barD));
                                barrt3.setColorFilter(ContextCompat.getColor(getContext(), R.color.barB));
                                barrt4.setColorFilter(ContextCompat.getColor(getContext(), R.color.barD));
                                barrt5.setColorFilter(ContextCompat.getColor(getContext(), R.color.barA));



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        AndroidNetworking.post("http://hotel.depotmart.id/api/get_warga.php")
                .addBodyParameter("id","rw")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        if(responses.length()>0){
                            load_success();
                            Log.d("sukses kat", "onResponse" + responses);
                            JSONObject response = null;
                            try {
                                response = responses.getJSONObject(0);
                                rw1.setText(response.getString("1"));
                                rw2.setText(response.getString("2"));
                                rw3.setText(response.getString("3"));
                                rw4.setText(response.getString("4"));
                                rw5.setText(response.getString("5"));

                                int is = response.getInt("1");
                                int kr = response.getInt("2");
                                int bu = response.getInt("3");
                                int hi = response.getInt("4");
                                int kh = response.getInt("5");

                                barrw1.getLayoutParams().width=is*60;
                                barrw2.getLayoutParams().width=kr*60;
                                barrw3.getLayoutParams().width=bu*60;
                                barrw4.getLayoutParams().width=hi*60;
                                barrw5.getLayoutParams().width=kh*60;

                                barrw1.setColorFilter(ContextCompat.getColor(getContext(), R.color.barE));
                                barrw2.setColorFilter(ContextCompat.getColor(getContext(), R.color.barD));
                                barrw3.setColorFilter(ContextCompat.getColor(getContext(), R.color.barA));
                                barrw4.setColorFilter(ContextCompat.getColor(getContext(), R.color.barB));
                                barrw5.setColorFilter(ContextCompat.getColor(getContext(), R.color.barC));



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        AndroidNetworking.post("http://hotel.depotmart.id/api/get_warga.php")
                .addBodyParameter("id","u")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        if(responses.length()>0){
                            load_success();
                            Log.d("sukses kat", "onResponse" + responses);
                            JSONObject response = null;
                            try {
                                response = responses.getJSONObject(0);
                                u1.setText(response.getString("a"));
                                u2.setText(response.getString("b"));
                                u3.setText(response.getString("c"));
                                u4.setText(response.getString("d"));
                                u5.setText(response.getString("e"));
                                u6.setText(response.getString("f"));

                                int is = response.getInt("a");
                                int kr = response.getInt("b");
                                int bu = response.getInt("c");
                                int hi = response.getInt("d");
                                int kh = response.getInt("e");
                                int um = response.getInt("f");

                                baru1.getLayoutParams().width=is*60;
                                baru2.getLayoutParams().width=kr*60;
                                baru3.getLayoutParams().width=bu*60;
                                baru4.getLayoutParams().width=hi*60;
                                baru5.getLayoutParams().width=kh*60;
                                baru6.getLayoutParams().width=um*60;

                                baru1.setColorFilter(ContextCompat.getColor(getContext(), R.color.barB));
                                baru2.setColorFilter(ContextCompat.getColor(getContext(), R.color.barA));
                                baru3.setColorFilter(ContextCompat.getColor(getContext(), R.color.barC));
                                baru4.setColorFilter(ContextCompat.getColor(getContext(), R.color.barE));
                                baru5.setColorFilter(ContextCompat.getColor(getContext(), R.color.barA));
                                baru6.setColorFilter(ContextCompat.getColor(getContext(), R.color.barD));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
