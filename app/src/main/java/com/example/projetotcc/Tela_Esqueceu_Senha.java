package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.IRecuperarSenhaCodigoVerificacao;
import Models.RecuperarSenhaCliente;

public class Tela_Esqueceu_Senha extends AppCompatActivity {

    private ImageButton btnVoltaEsqueci;
    private Button btnEnviarEmail;
    private EditText editTextEmail;

    private TextView txtVolteAoLogin;
    private String emailCliente;
    private Intent intentVerificarCodigo;
    private RequestQueue solicitacao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_esqueceu_senha);

        solicitacao = Volley.newRequestQueue(this);
        intentVerificarCodigo = new Intent(getApplicationContext(), Tela_Verificacao_Codigo.class);

        btnVoltaEsqueci = findViewById(R.id.btnVoltaEsqueceuSenha);
        btnEnviarEmail = findViewById(R.id.btnEscSenha);
        editTextEmail = findViewById(R.id.editTextEmail);

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

                if (emailCliente.isEmpty()) {
                    Toast.makeText(Tela_Esqueceu_Senha.this, "Preencha o campo com um email", Toast.LENGTH_SHORT).show();
                } else {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailCliente).matches()) {
                        EnviarCodigoVerificacao(emailCliente);
                    } else {
                        Toast.makeText(Tela_Esqueceu_Senha.this, "Digite um email válido", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void EnviarCodigoVerificacao(String email){
        try {
            RecuperarSenhaCliente.ReceberCodigoVerificacao(email, solicitacao, getApplicationContext(), new IRecuperarSenhaCodigoVerificacao() {
                @Override
                public void onResultado(String codigoVerificacao) {
                    SharedPreferences.Editor gravarDadosRecuperacao = getSharedPreferences("dadosRecuperacao", MODE_PRIVATE).edit();
                    gravarDadosRecuperacao.putString("email", emailCliente);
                    gravarDadosRecuperacao.putString("codigoVerificacao", codigoVerificacao);
                    gravarDadosRecuperacao.commit();
                    startActivity(intentVerificarCodigo);
                }

                @Override
                public void onError(String mensagemErro) {
                    Toast.makeText(Tela_Esqueceu_Senha.this, mensagemErro, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(Tela_Esqueceu_Senha.this, "Email não cadastrado!", Toast.LENGTH_SHORT).show();
        }
    }

}