package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Tela_Verificacao_Codigo extends AppCompatActivity {

    private ImageButton btnVoltaVerificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_verificacao_codigo);

        //referencias
        btnVoltaVerificacao = findViewById(R.id.btnVoltaVerificacao);

        //voltando para tela de Esqueci a senha
        btnVoltaVerificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Esqueceu_Senha.class);
                startActivity(intent);
            }
        });
    }
}