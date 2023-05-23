package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Tela_Bem_Vindo extends AppCompatActivity {
    private Button btnBemVindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_bem_vindo);

        //Referencias
        btnBemVindo = findViewById(R.id.btnBemVindo);

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