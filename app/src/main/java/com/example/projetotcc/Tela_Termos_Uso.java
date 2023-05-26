package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Tela_Termos_Uso extends AppCompatActivity {
    private ImageButton imgBtnVoltarTermos;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_termos_uso);
        imgBtnVoltarTermos = findViewById(R.id.imgBtnVoltarTermos);
        btnOk =findViewById(R.id.btnOk);

        imgBtnVoltarTermos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tela_Termos_Uso.this, Tela_Cadastro.class);
                startActivity(intent);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tela_Termos_Uso.this, Tela_Cadastro.class);
                startActivity(intent);
            }
        });
    }
}