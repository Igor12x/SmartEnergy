package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Tela_Esqueceu_Senha extends AppCompatActivity {

    private ImageButton btnVoltaEsqueci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_esqueceu_senha);

        //referencias
        btnVoltaEsqueci = findViewById(R.id.btnVoltaEsqueceuSenha);

        //voltando para tela de login
        btnVoltaEsqueci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Login.class);
                startActivity(intent);
            }
        });

    }
}