package com.ardev.tugasakhir.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ardev.tugasakhir.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LupaSandi extends BaseActivity {
Button hubungi;
EditText email;
public int cEmail;
String b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_sandi);

        AndroidNetworking.initialize(this);
        hubungi = findViewById(R.id.btn_hubungi);
        email = findViewById(R.id.email_lupa);

       b = email.getText().toString();

            hubungi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (email.getText().toString().isEmpty()) {
                        email.setError("Masukan Email yang terdaftar");
                    }else{
                        cekEmail();
                    }
                }
            });

    }

    private void cekEmail() {
        AndroidNetworking.post("http://hotel.depotmart.id/api/cekemail.php")
                .addBodyParameter("id_user", email.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cEmail = response.getInt("code");
                            if((cEmail == 1)){
                                String number = "6281904963972";
                                String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + "Saya melupakan password "+email.getText().toString() ;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }else {
                                email.setError("Email anda tidak terdaftar");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public void findView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }
}
