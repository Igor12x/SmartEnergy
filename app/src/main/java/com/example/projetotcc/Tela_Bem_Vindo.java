package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Tela_Bem_Vindo extends AppCompatActivity {
    private Button btnBemVindo;
    private TextView txtBemVindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bem_vindo);

        //Referencias
        btnBemVindo = findViewById(R.id.btnBemVindo);
        txtBemVindo = findViewById(R.id.txtBemVindo);

        // Recuperar o nome da Intent
        SharedPreferences ler = getSharedPreferences("usuario", MODE_PRIVATE);
        String nome = ler.getString("nome", "");
        if (nome != null && !nome.isEmpty()) {
            txtBemVindo.setText(nome + ", SEJA BEM-VINDO A SMART ENERGY!");
        } else {
            txtBemVindo.setText("SEJA BEM-VINDO A SMART ENERGY!");
        }


        //Volte a tela de login
        btnBemVindo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Login.class);
                startActivity(intent);
            }
        });




    }
}