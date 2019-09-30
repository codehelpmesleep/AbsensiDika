package com.juaracoding.absensidika.CheckIn.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.nikartm.button.FitButton;
import com.juaracoding.absensidika.R;

public class ChooseAbsen extends AppCompatActivity {

    FitButton btnSelfie,btnQrcode;
    String absentType ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        absentType = getIntent().getStringExtra("typeAbsent");



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_absen);
        btnSelfie = findViewById(R.id.btnSelfie);
        btnQrcode = findViewById(R.id.btnQRCode);

        btnSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAbsen.this,SelfieActivity.class);
                intent.putExtra("typeAbsent",absentType);
                startActivity(intent);
            }
        });

        btnQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAbsen.this,QRActivity.class);
                intent.putExtra("typeAbsent",absentType);
                startActivity(intent);
            }
        });
    }
}
