package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Models.Cliente;
import Models.LoginCliente;

public class Tela_Login extends AppCompatActivity {
    private TextView txtCpf, txtSenha;
    private Button btnLogar;
    private String cpfCliente, senhaCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        RequestQueue solicitacao = Volley.newRequestQueue(this);

        txtCpf = findViewById(R.id.txtCpf);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLog);

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpfCliente = txtCpf.getText().toString();
                senhaCliente = txtSenha.getText().toString();
                ValidarLogin(solicitacao, cpfCliente, senhaCliente);
                txtCpf.setText("");
                txtSenha.setText("");
            }
        });
    }

    public void ValidarLogin(RequestQueue solicitacao, String cpf, String senha) {
        LoginCliente login = new LoginCliente(cpf, senha);
        LoginCliente.ValidarLoginCliente(login, solicitacao, new LoginCliente.ValidarLoginListener() {
            @Override
            public void onResultado(Cliente clienteLogado) {
                Toast.makeText(Tela_Login.this, "Logado com sucesso" + clienteLogado.getNome(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}