package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Tela_Perfil extends AppCompatActivity {

    private ImageButton btnVoltaPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);

        //referencias
        btnVoltaPerfil = findViewById(R.id.btnVoltaPerfil);

        //voltando para tela de inicio principal
        btnVoltaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
                startActivity(intent);
            }
        });
    }
}