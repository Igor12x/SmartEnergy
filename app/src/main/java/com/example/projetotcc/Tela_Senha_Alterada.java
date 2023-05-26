package com.example.projetotcc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Tela_Senha_Alterada extends AppCompatActivity {

    private ImageButton btnVoltaSenhaAlt;

    private Button btnSenhaAlt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_senha_alterada);

        //referencias
        btnVoltaSenhaAlt = findViewById(R.id.btnVoltaSenhaAlt);
        btnSenhaAlt =findViewById(R.id.btnSenhaAlt);

        //voltando para tela de inicio principal
        btnVoltaSenhaAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Redefinir_Senha.class);
                startActivity(intent);
            }
        });

        //Após o aceite de recuperação retornar ao login
        btnSenhaAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Login.class);
                startActivity(intent);
            }
        });


    }
}


