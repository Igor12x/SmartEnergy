package com.example.projetotcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.ILoginCliente;
import Models.Cliente;
import Models.ClienteLogin;
import Models.Residencia;

public class Tela_Carregamento extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_carregamento);

        RequestQueue solicitacao = Volley.newRequestQueue(this);
        logar(solicitacao);
    }

    private void logar(RequestQueue solicitacao){
        if(!possuiUsuarioGravado()) {
            Log.d("logarTeladeCarregamentoTrue", "IF");
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent(getApplicationContext(), Tela_Inicial.class);
                startActivity(intent);
            }, 4000);
        } else {
            Log.d("logarTeladeCarregamento",">>>>>>>>> aqui no else");
            SharedPreferences ler = getSharedPreferences("usuario", MODE_PRIVATE);
            ValidarLogin(solicitacao, ler.getString("cpf", ""), ler.getString("senha", ""));
            Log.d("sharedPreferences", ">>>>>>> CPF" + ler.getString("cpf", ""));
        }
    }
    private boolean possuiUsuarioGravado() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE);
            return sharedPreferences.contains("cpf");
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private void abrirTelaPrincipal() {
        Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
        startActivity(intent);
    }
    private void abrirTelaLogin() {
        Intent intent = new Intent(getApplicationContext(), Tela_Login.class);
        startActivity(intent);
    }
    public void ValidarLogin(RequestQueue solicitacao, String cpf, String senha) {
        ClienteLogin login = new ClienteLogin(cpf, senha);
        ClienteLogin.ValidarLoginCliente(login, solicitacao, new ILoginCliente() {
            @Override
            public void onResultado(Cliente clienteLogado) {
                verificarResidenciaCadastrada(solicitacao, clienteLogado.getCpf());
                Log.d("ValidarLogin", ">>>>>>>>>> " + clienteLogado.getCpf());
            }

            @Override
            public void onErro(String mensagemErro) {
                Toast.makeText(Tela_Carregamento.this, mensagemErro, Toast.LENGTH_SHORT).show();
                abrirTelaLogin();
            }
        });
    }

    public void verificarResidenciaCadastrada(RequestQueue solicitacao, String cpf) {
        Log.d("verificarResidenciaCadastrada", ">>>>>>>>>>" + "estou no metodo");
        Residencia.verificarResidenciaCadastrada(cpf, solicitacao, cadastrado -> {
            Log.d("verificarResidenciaCadastrada", ">>>>>>> " + cadastrado);
            if (cadastrado) {
                abrirTelaPrincipal();
            } else {
                Toast.makeText(Tela_Carregamento.this, "Você ainda não possui nosso medidor inteligente, aguarde o contato do nosso consultor", Toast.LENGTH_SHORT).show();
                abrirTelaLogin();
            }
        });
    }
}