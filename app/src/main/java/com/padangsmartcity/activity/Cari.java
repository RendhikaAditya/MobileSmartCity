package com.padangsmartcity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.padangsmartcity.R;
import com.padangsmartcity.util.SetUI;

public class Cari extends Activity {

    SetUI setUI = new SetUI();
    EditText txt_cari;
    ImageButton btn_cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari);
        setUI.StatusBarTransparent(getWindow());

        txt_cari = findViewById(R.id.txt_cari);
        btn_cari = findViewById(R.id.btn_cari);

        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_cari.getText().toString().isEmpty()){
                    txt_cari.setError("Apa yang kamu cari?");
                }else{
                    Intent i = new Intent(Cari.this, HasilCari.class);
                    i.putExtra("q", txt_cari.getText().toString());
                    startActivity(i);
                }
            }
        });
    }
}
