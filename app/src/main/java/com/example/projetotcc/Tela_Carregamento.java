package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Tela_Carregamento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_carregamento);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
        Intent intent = new Intent(getApplicationContext(), Tela_Inicial.class);
        startActivity(intent);
        }, 4000);

    }
}