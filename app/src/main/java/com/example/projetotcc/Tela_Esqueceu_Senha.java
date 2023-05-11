package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import Interfaces.IRecuperarSenhaCliente;
import Models.RecuperarSenhaCliente;

public class Tela_Esqueceu_Senha extends AppCompatActivity {

    private ImageButton btnVoltaEsqueci;
    private Button btnEnviarEmail;
    private EditText editTextEmail;
    private RequestQueue solicitacao = null;
    private String codigoVerificacaoEmail;
    private String emailCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_esqueceu_senha);

        //referencias
        btnVoltaEsqueci = findViewById(R.id.btnVoltaEsqueceuSenha);
        btnEnviarEmail = findViewById(R.id.btnEscSenha);
        editTextEmail = findViewById(R.id.editTextEmail);

        //voltando para tela de login
        btnVoltaEsqueci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Login.class);
                startActivity(intent);
            }
        });

        btnEnviarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailCliente = editTextEmail.getText().toString();
                ReceberCodigoVerificacao(solicitacao, emailCliente);
            }
        });

    }

    public void ReceberCodigoVerificacao(RequestQueue solicitacao, String email) {
        try {
            RecuperarSenhaCliente.ReceberCodigoVerificacao(email, solicitacao, new IRecuperarSenhaCliente() {
                @Override
                public void onResultado(String codigoVerificacao) {
                    codigoVerificacaoEmail = codigoVerificacao;
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Email não cadastrado", Toast.LENGTH_SHORT).show();
        }

    }
}