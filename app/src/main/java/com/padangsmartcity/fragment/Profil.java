package com.padangsmartcity.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.padangsmartcity.R;
import com.padangsmartcity.activity.Daftar;
import com.padangsmartcity.activity.EditProfil;
import com.padangsmartcity.activity.LihatFoto;
import com.padangsmartcity.activity.Login;
import com.padangsmartcity.util.PrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profil extends BaseFragment {

    View view;
    Button btn_masuk, btn_daftar, btn_keluar, btn_edit;

    RelativeLayout layout_login, layout_koneksi;
    ScrollView scroll_profil;

    TextView nama_user, email_user, telp_user, alamat_user, bio_user, tanggal, aduan, tanggapan, lokasi, jk_user, nik_user;
    ImageView thumb_user, verified;

    String id_user, sfoto_user, sthumb_user;
    boolean loaded = false;

    ShimmerFrameLayout shimmer_profil;

    PrefManager prefManager;

    String inama_user, ipass_user, ibio_user, itelp_user, ialamat_user, inik_user, ijk_user, versi;


    CardView card_tentang, card_rating, card_report;

    public Profil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profil, container, false);
        findView();
        initView();
        initListener();

        AndroidNetworking.initialize(getContext());

        return view;
    }

    @Override
    public void onResume(){
        login_view();
        super.onResume();
    }

    @Override
    public void findView() {
        btn_masuk = view.findViewById(R.id.btn_masuk);
        btn_daftar = view.findViewById(R.id.btn_daftar);
        btn_keluar = view.findViewById(R.id.btn_keluar);
        btn_edit = view.findViewById(R.id.btn_edit);

        layout_koneksi = view.findViewById(R.id.layout_koneksi);
        shimmer_profil = view.findViewById(R.id.shimmer_profil);

        nama_user = view.findViewById(R.id.nama_user);
        email_user = view.findViewById(R.id.email_user);
        jk_user = view.findViewById(R.id.jk_user);
        nik_user = view.findViewById(R.id.nik_user);
        telp_user = view.findViewById(R.id.telp_user);
        alamat_user = view.findViewById(R.id.alamat_user);
        bio_user = view.findViewById(R.id.bio_user);
        tanggal = view.findViewById(R.id.tanggal);
        aduan = view.findViewById(R.id.aduan);
        tanggapan = view.findViewById(R.id.tanggapan);
        thumb_user = view.findViewById(R.id.thumb_user);
        verified = view.findViewById(R.id.verified);
        lokasi = view.findViewById(R.id.lokasi);

        layout_login = view.findViewById(R.id.layout_login);
        scroll_profil = view.findViewById(R.id.scroll_profil);

        card_tentang = view.findViewById(R.id.card_tentang);
        card_rating = view.findViewById(R.id.card_rating);
        card_report = view.findViewById(R.id.card_report);
    }

    @Override
    public void initView() {
        login_view();
        lokasi.setText("LOKASI TERAKHIR ANDA : "+prefManager.getLocation());
        //mendapatkan versi
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versi = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Login.class);
                startActivity(i);
            }
        });
        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), Daftar.class);
                startActivity(i);
            }
        });
        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Perhatian")
                        .setMessage("Apakah anda yakin akan keluar?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                logout();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

        layout_koneksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        thumb_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), LihatFoto.class);
                i.putExtra("image", sfoto_user);
                i.putExtra("title", inama_user.toUpperCase());
                startActivity(i);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), EditProfil.class);
                i.putExtra("nama_user", inama_user);
                i.putExtra("pass_user", ipass_user);
                i.putExtra("bio_user", ibio_user);
                i.putExtra("telp_user", itelp_user);
                i.putExtra("alamat_user", ialamat_user);
                i.putExtra("nik_user", inik_user);
                i.putExtra("jk_user", ijk_user);
                i.putExtra("id_user", id_user);
                i.putExtra("thumb_user", sthumb_user);
                startActivity(i);
            }
        });

        card_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackagename = getActivity().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackagename)));
                }catch (android.content.ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+appPackagename)));
                }
            }
        });

        card_tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setIcon(R.drawable.logo)
                        .setMessage("versi "+versi+"\n\n"+ Html.fromHtml("<b>Pekalongan Smart Regency</b> adalah sebuah aplikasi yang memuat segala informasi tentang Kabupaten Pekalongan"))
                        .setCancelable(false)
                        .setNegativeButton("Ok", null)
                        .show();
            }
        });

        card_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"luwakode@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "BUGS PEKALONGAN SMART REGENCY");
                i.putExtra(Intent.EXTRA_TEXT, "Catatan : Deskripsikan kesalahan setelah ini. Jika perlu lampirkan foto");
                try {
                    startActivity(Intent.createChooser(i, "Kirim email..."));
                }catch (android.content.ActivityNotFoundException e){
                    Toast.makeText(getActivity().getBaseContext(), "Tidak ada aplikasi email client terinstal", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void login_view(){
        prefManager = new PrefManager(getActivity().getApplicationContext());
        id_user = prefManager.getIdUser();
        if (prefManager.getLoginStatus()) {
            layout_login.setVisibility(View.GONE);
            getData();
            Glide.with(getContext())
                    .load(sthumb_user)
                    .placeholder(R.drawable.img_profile)
                    .into(thumb_user);
        }else{
            scroll_profil.setVisibility(View.GONE);
            layout_login.setVisibility(View.VISIBLE);
        }
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



//    private void getData(){
//        set_loading();
//        JsonArrayRequest requestData = new JsonArrayRequest(Request.Method.POST, ServerApi.get_user, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("volley", "response : "+response.toString());
//                        try {
//                            JSONObject data = response.getJSONObject(0);
//                            inama_user = data.getString("nama_user");
//                            ibio_user = data.getString("bio_user");
//                            itelp_user = data.getString("telp_user");
//                            ialamat_user = data.getString("alamat_user");
//                            inik_user = data.getString("nik_user");
//                            ijk_user = data.getString("jk_user");
//
//                            nama_user.setText(data.getString("nama_user"));
//                            bio_user.setText(data.getString("bio_user"));
//                            aduan.setText(data.getString("aduan"));
//                            tanggapan.setText(data.getString("tanggapan"));
//                            tanggal.setText(data.getString("tanggal"));
//                            telp_user.setText(data.getString("telp_user"));
//                            email_user.setText(data.getString("email_user"));
//                            alamat_user.setText(data.getString("alamat_user"));
//                            nik_user.setText(data.getString("nik_user"));
//                            sfoto_user = data.getString("foto_user");
//                            sthumb_user = data.getString("thumb_user");
//
////                            Glide.with(getContext())
////                                    .load(sthumb_user)
////                                    .placeholder(R.drawable.img_profile)
////                                    .into(thumb_user);
//
//                            if (data.getString("jk_user").equals("L")){
//                                jk_user.setText("Laki-laki");
//                            }else if(data.getString("jk_user").equals("P")){
//                                jk_user.setText("Perempuan");
//                            }else{
//                                jk_user.setText(data.getString("jk_user"));
//                            }
//
//                            if (data.getString("verified").equals("0")){
//                                verified.setVisibility(View.GONE);
//                            }else{
//                                verified.setVisibility(View.VISIBLE);
//                            }
//
//                            prefManager.setNamaUser(data.getString("nama_user"));
////                            Glide.with(getContext())
////                                    .load(data.getString("thumb_user"))
////                                    .placeholder(R.drawable.img_profile)
////                                    .into(thumb_user);
//                            loaded = true;
//                            load_success();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
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
//                }){
//            @Override
//            protected Map<String ,String>getParams(){
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id_user", id_user);
//                return params;
//            }
//
//        };
//        AppController.getInstance().addToRequestQueue(requestData);
//    }


    private void getData() {
        set_loading();
//        nama_user.setText(prefManager.getIdUser());

        AndroidNetworking.post("http://hotel.depotmart.id/api/get_user.php")
                .addBodyParameter("id_user", prefManager.getIdUser())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener(){
                    @Override
                    public void onResponse(JSONArray responses) {
                        try {
                            JSONObject response = responses.getJSONObject(0);
                            inama_user = response.getString("nama_user");
                            ipass_user = response.getString("pass_user");
                            ibio_user = response.getString("bio_user");
                            itelp_user = response.getString("telp_user");
                            ialamat_user = response.getString("alamat_user");
                            inik_user = response.getString("nik_user");
                            ijk_user = response.getString("jk_user");

                            nama_user.setText(response.getString("nama_user"));
                            bio_user.setText(response.getString("bio_user"));
                            aduan.setText(response.getString("aduan"));
                            tanggapan.setText(response.getString("tanggapan"));
                            tanggal.setText(response.getString("tanggal"));
                            telp_user.setText(response.getString("telp_user"));
                            email_user.setText(response.getString("email_user"));
                            alamat_user.setText(response.getString("alamat_user"));
                            nik_user.setText(response.getString("nik_user"));
                            sfoto_user = response.getString("foto_user");
                            sthumb_user = response.getString("thumb_user");

                            Picasso.get().load(sfoto_user).into(thumb_user);

//                            Glide.with(getContext())
//                                    .load(sthumb_user)
//                                    .placeholder(R.drawable.img_profile)
//                                    .into(thumb_user);

                            if (response.getString("jk_user").equals("L")){
                                jk_user.setText("Laki-laki");
                            }else if(response.getString("jk_user").equals("P")){
                                jk_user.setText("Perempuan");
                            }else{
                                jk_user.setText(response.getString("jk_user"));
                            }

                            if (response.getString("verified").equals("0")){
                                verified.setVisibility(View.GONE);
                            }else{
                                verified.setVisibility(View.VISIBLE);
                            }

                            prefManager.setNamaUser(response.getString("nama_user"));
//                            Glide.with(getContext())
//                                    .load(data.getString("thumb_user"))
//                                    .placeholder(R.drawable.img_profile)
//                                    .into(thumb_user);
                            loaded = true;
                            load_success();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
//                        Log.d("volley", "error : "+anError.getMessage());
                        Log.d("Error", "error : "+anError);
                        load_fail();

                    }
                });
    }


    private void logout(){
        prefManager.setIdUser("");
        prefManager.setLoginStatus(false);
        prefManager.setNamaUser("");
        login_view();
    }
}
