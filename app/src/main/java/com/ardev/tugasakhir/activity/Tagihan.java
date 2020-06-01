package com.ardev.tugasakhir.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ardev.tugasakhir.R;

public class Tagihan extends AppCompatActivity {
EditText nop;
ImageButton search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan);

        nop = findViewById(R.id.txt_cari);
        search = findViewById(R.id.btn_cari);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nop.getText().toString().isEmpty()){
                    nop.setError("Apa yang kamu cari?");
                }else {
                    Intent intent = new Intent(Tagihan.this, ListTagihan.class);
                    intent.putExtra("IDNOP", nop.getText().toString());
                    startActivity(intent);
                }

            }
        });

    }
}
