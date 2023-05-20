package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Tela_Redefinir_Senha extends AppCompatActivity {

    private ImageButton btnVoltaNovaSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha);

        //referencias
        btnVoltaNovaSenha = findViewById(R.id.btnVoltaNovaSenha);

        //voltando para tela de Código verifição
        btnVoltaNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Verificacao_Codigo.class);
                startActivity(intent);
            }
        });
    }
}