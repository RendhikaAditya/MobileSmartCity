package com.padangsmartcity.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.padangsmartcity.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class Daftar extends BaseActivity {

    EditText txt_nama_user, txt_email_user, txt_password, txt_repassword;
    TextView txt_masuk;
    Button btn_daftar;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        findView();
        initView();
        initListener();

        AndroidNetworking.initialize(this);
    }

    @Override
    public void findView() {
        txt_nama_user = findViewById(R.id.txt_nama_user);
        txt_email_user = findViewById(R.id.txt_email_user);
        txt_password = findViewById(R.id.txt_password);
        txt_repassword = findViewById(R.id.txt_repassword);

        txt_masuk = findViewById(R.id.txt_masuk);
        btn_daftar = findViewById(R.id.btn_daftar);
    }

    @Override
    public void initView() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Sedang mendaftar...")
                .setCancelable(false)
                .build();
    }

    @Override
    public void initListener() {
        txt_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_nama_user.getText().toString().isEmpty()){
                    txt_nama_user.setError("Nama lengkap harus diisi");
                }else if(txt_email_user.getText().toString().isEmpty()){
                    txt_email_user.setError("Email harus diisi");
                }else if(txt_password.getText().toString().isEmpty()){
                    txt_password.setError("Kata sandi harus diisi");
                }else if(txt_repassword.getText().toString().isEmpty()){
                    txt_repassword.setError("Ketikkan ulang kata sandi anda");
                }else{
                    if (!txt_repassword.getText().toString().equals(txt_password.getText().toString())){
                        txt_repassword.setError("Kata sandi tidak sesuai");
                    }else{
                        daftar(txt_nama_user.getText().toString(), txt_email_user.getText().toString(), txt_repassword.getText().toString());
                    }
                }
            }
        });
    }

    private void daftar(String nama, String email, String password) {
        dialog.show();
        //                params.put("nama_user", nama);
//                params.put("email_user", email);
//                params.put("password_user", password);
        AndroidNetworking.post("http://hotel.depotmart.id/api/add_user.php")
                .addBodyParameter("nama_user", nama)
                .addBodyParameter("email_user", email)
                .addBodyParameter("password_user", password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){

                    @Override
                    public void onResponse(JSONObject data) {
                    try
                    {
    //                    JSONObject data = new JSONObject(response);
                        String code = data.getString("code");
                        dialog.dismiss();
                        // ngecek node error dari api
                        if (code.equals("1")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Daftar.this);
                            builder.setMessage(data.getString("response"))
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }else if(code.equals("2")) {
                            // terjadi error dan tampilkan pesan error dari API
                            snackBar(data.getString("response"), R.color.error);
                        }else if(code.equals("0")) {
                            // terjadi error dan tampilkan pesan error dari API
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

                    }
                });

    }

//    private void daftar(final String nama, final String email, final String password) {
//        dialog.show();
//        // Tag biasanya digunakan ketika ingin membatalkan request volley
//        String tag_string_req = "req_daftar";
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ServerApi.daftar, new Response.Listener<String>() {
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
//                    if (code.equals("1")){
//                        AlertDialog.Builder builder = new AlertDialog.Builder(Daftar.this);
//                        builder.setMessage(data.getString("response"))
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        finish();
//                                    }
//                                });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//                    }else if(code.equals("2")) {
//                        // terjadi error dan tampilkan pesan error dari API
//                        snackBar(data.getString("response"), R.color.error);
//                    }else if(code.equals("0")) {
//                        // terjadi error dan tampilkan pesan error dari API
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
//                params.put("nama_user", nama);
//                params.put("email_user", email);
//                params.put("password_user", password);
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
