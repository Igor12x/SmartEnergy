package com.example.projetotcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Tela_Perfil extends AppCompatActivity {

    private ImageButton btnVoltaPerfil, ibtnDesconectar;
    //private Button btnSim, btnNao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);

        //referencias
        btnVoltaPerfil = findViewById(R.id.btnVoltaPerfil);
        ibtnDesconectar = findViewById(R.id.ibtnDesconectar);

        //Desconectar
        ibtnDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Tela_Perfil.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Tela_Perfil.this).inflate(
                        R.layout.layout_dialog,(ConstraintLayout)findViewById(R.id.layoutDialogContainer)
                );
                builder.setView(view1);
                final AlertDialog alertDialog = builder.create();

                Button btnSim = view1.findViewById(R.id.btnSim);
                btnSim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();

                                SharedPreferences prefs = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), Tela_Login.class);
                                startActivity(intent);
                            }
                        }, 500); // ajuste o valor do atraso conforme necessário
                    }
                });

                Button btnNao = view1.findViewById(R.id.btnNao);
                btnNao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


                alertDialog.show();
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