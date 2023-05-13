package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Tela_Inicial extends AppCompatActivity {
    private Button iBtnLogin, iBtnCad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        //referencias
        iBtnLogin = findViewById(R.id.iBtnLogin);
        iBtnCad = findViewById(R.id.iBtnCad);

        //Ir para tela de Login
        iBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Login.class);
                startActivity(intent);
            }
        });


        //Ir para tela de Cadastro
        iBtnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Cadastro.class);
                startActivity(intent);
            }
        });
    }
}