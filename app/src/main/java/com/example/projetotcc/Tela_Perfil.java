package com.example.projetotcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Tela_Perfil extends AppCompatActivity {

    private ImageButton btnVoltaPerfil, ibtnDesconectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);

        //referencias
        btnVoltaPerfil = findViewById(R.id.btnVoltaPerfil);
        ibtnDesconectar = findViewById(R.id.ibtnDesconectar);

        //limite
        ibtnDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Tela_Perfil.this);
                builder.setTitle("Desconectar");
                builder.setMessage("Deseja sair mesmo da conta");
                builder.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // ação a ser executada ao clicar no botão positivo
                        Handler atraso = new Handler();
                        atraso.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                            }
                        }, 2500);
                        dialog.dismiss();

                        SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.apply();


                        Intent intent = new Intent(getApplicationContext(), Tela_Login.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // ação a ser executada ao clicar no botão negativo
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------

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