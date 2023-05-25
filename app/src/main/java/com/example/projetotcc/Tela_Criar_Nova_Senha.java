package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Tela_Criar_Nova_Senha extends AppCompatActivity {

    private ImageButton btnVoltaNovaSenha, btnTrocaSenha, btnRedefinirSenha;
    private TextView txtSenhaRecuperar, txtRedefinirSenhaRecuperar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar_nova_senha);

        //referencias
        btnVoltaNovaSenha = findViewById(R.id.btnVoltaNovaSenha);
        btnTrocaSenha = findViewById(R.id.btnTrocaSenha);
        btnRedefinirSenha = findViewById(R.id.btnRedefinirSenha);
        txtSenhaRecuperar = findViewById(R.id.txtSenhaRecuperar);
        txtRedefinirSenhaRecuperar = findViewById(R.id.txtConfirmeSenhaRecuperar);

        //voltando para tela de Código verifição
        btnVoltaNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Verificacao_Senha.class);
                startActivity(intent);
            }
        });
        btnTrocaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean mostrarSenha = !txtSenhaRecuperar.getTransformationMethod().equals(PasswordTransformationMethod.getInstance());

                txtSenhaRecuperar.setTransformationMethod(mostrarSenha ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());

                // Atualize o ícone do botão
                btnTrocaSenha.setImageResource(mostrarSenha ? R.drawable.olho_fechado : R.drawable.icone_olho_azul);
            }
        });

        btnRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mostrarSenha = !txtRedefinirSenhaRecuperar.getTransformationMethod().equals(PasswordTransformationMethod.getInstance());

                txtRedefinirSenhaRecuperar.setTransformationMethod(mostrarSenha ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());

                // Atualize o ícone do botão
                btnRedefinirSenha.setImageResource(mostrarSenha ? R.drawable.olho_fechado : R.drawable.icone_olho_azul);
            }
        });
    }
}