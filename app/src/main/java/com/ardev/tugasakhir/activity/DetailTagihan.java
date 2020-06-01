package com.ardev.tugasakhir.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.ardev.tugasakhir.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailTagihan extends AppCompatActivity {
TextView txt, txtnop, luasbumi, luasbng, njopbumi, njopbng, lokasi, nama,alamatwp, thnpajak, tgltempo,jmlpajak, status, txt_kosong;
ImageView img_kosong;
RelativeLayout layout_kosong;
LinearLayout detailtagih;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tagihan);
        txtnop = findViewById(R.id.idnop);
        luasbumi = findViewById(R.id.luas_bumi);
        luasbng = findViewById(R.id.luas_bng);
        njopbumi = findViewById(R.id.njop_bumi);
        njopbng = findViewById(R.id.njop_bng);
        lokasi = findViewById(R.id.lokasiop);
        nama = findViewById(R.id.idnama);
        alamatwp = findViewById(R.id.alamatwp);
        thnpajak = findViewById(R.id.thn_pajak);
        tgltempo = findViewById(R.id.tgl_tempo);
        jmlpajak = findViewById(R.id.jml_pajak);
        status = findViewById(R.id.status);

        txt_kosong = findViewById(R.id.txt_kosong);
        img_kosong = findViewById(R.id.img_kosong);
        layout_kosong = findViewById(R.id.layout_kosong);
        detailtagih = findViewById(R.id.detailtagih);

        AndroidNetworking.initialize(this);
        getData();
    }
    private void load_empty(){
        layout_kosong.setVisibility(View.VISIBLE);
        detailtagih.setVisibility(View.GONE);
//        txt_kosong.setText(Html.fromHtml("Hasil pencarian <b></b> tidak ditemukan."));
//        img_kosong.setImageDrawable(getResources().getDrawable(R.drawable.img_cari));
    }

    private void getData() {
        Intent intent = getIntent();
        final String id = intent.getStringExtra("IDDD");
        AndroidNetworking.post("http://hotel.depotmart.id/api/get_nop.php")
                .addBodyParameter("iddd", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray responses) {
                        if (responses.length() > 0) {
                            detailtagih.setVisibility(View.VISIBLE);
                            layout_kosong.setVisibility(View.GONE);
                            try {
                                JSONObject response = responses.getJSONObject(0);
                                txtnop.setText(response.getString("nop"));
                                luasbumi.setText(response.getString("lbumi"));
                                luasbng.setText(response.getString("lbng"));
                                njopbumi.setText(response.getString("njopbumi"));
                                njopbng.setText(response.getString("njopbng"));
                                nama.setText(response.getString("nama"));
                                alamatwp.setText(response.getString("alamat"));
                                thnpajak.setText(response.getString("tahun"));
                                tgltempo.setText(response.getString("tanggal"));
                                jmlpajak.setText(response.getString("jumblah"));
                                status.setText(response.getString("status"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            layout_kosong.setVisibility(View.VISIBLE);
                            detailtagih.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Error", "error : "+anError);
                        layout_kosong.setVisibility(View.VISIBLE);
                        detailtagih.setVisibility(View.GONE);
                    }
                });
    }

}
