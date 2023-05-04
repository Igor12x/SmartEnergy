package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.ILoginCliente;
import Models.Cliente;
import Models.LoginCliente;

public class Tela_Login extends AppCompatActivity {
    private TextView txtCpf, txtSenha;
    private Button btnLogar;
    private String cpfCliente, senhaSliente;

    private Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        RequestQueue solicitacao = Volley.newRequestQueue(this);

        txtCpf = findViewById(R.id.txtCpf);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLogin);


        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpfCliente = txtCpf.getText().toString();
                senhaSliente = txtSenha.getText().toString();
                ValidarLogin(solicitacao, cpfCliente, senhaSliente);
                txtCpf.setText("");
                txtSenha.setText("");
            }
        });
    }

    public void ValidarLogin(RequestQueue solicitacao, String cpf, String senha) {

        LoginCliente login = new LoginCliente(cpf, senha);
        LoginCliente.ValidarLoginCliente(login, solicitacao, new ILoginCliente() {
            @Override
            public void onResultado(Cliente clienteLogado) {
                SharedPreferences.Editor gravar =
                        getSharedPreferences("usuario", MODE_PRIVATE).edit();
                gravar.putString("nome", clienteLogado.getNome());
                gravar.putString("cpf", clienteLogado.getCpf());
                gravar.putString("email", clienteLogado.getEmail());
                gravar.putString("telefone", clienteLogado.getTelefone());
                gravar.putString("senha", clienteLogado.getSenha());
                gravar.putInt("codigo", clienteLogado.getCodigo());

                if(gravar.commit()){
                    startActivity(intent);
                }else {
                    Toast.makeText(Tela_Login.this, "Senha ou/e e-mail incorretos",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}