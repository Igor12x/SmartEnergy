package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.ICadastroCliente;
import Models.CadastroCliente;
import Models.Cliente;
import Models.ClienteLogin;

public class Tela_Cadastro extends AppCompatActivity {
    private TextView plainCadNome, plainCadEmail, plainCadTel, plainCadCpf, plainCadSenha, plainCadConfirmarSenha;
    private Button btnCad;

    private ImageButton btnVoltaCad;
    private String nomeCliente, senhaCliente, cpfCliente, emailCliente, telCliente, senha1, senha2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        RequestQueue solicitacao = Volley.newRequestQueue(this);

        //referencias
        plainCadNome = findViewById(R.id.plainCadNome);
        plainCadEmail = findViewById(R.id.plainCadEmail);
        plainCadTel = findViewById(R.id.plainCadTel);
        plainCadCpf = findViewById(R.id.plainCadCpf);
        plainCadSenha = findViewById(R.id.plainCadSenha);
        plainCadConfirmarSenha = findViewById(R.id.plainCadConfirmarSenha);
        btnCad = findViewById(R.id.btnCad);
        btnVoltaCad = findViewById(R.id.btnVoltaCad);

        //voltando para tela inicial
        btnVoltaCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Inicial.class);
                startActivity(intent);
            }
        });

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeCliente = plainCadNome.getText().toString();
                cpfCliente = plainCadCpf.getText().toString();
                emailCliente = plainCadEmail.getText().toString();
                telCliente = plainCadTel.getText().toString();
                senhaCliente = plainCadSenha.getText().toString();
                ValidarCadastro(solicitacao, nomeCliente, cpfCliente, senhaCliente, emailCliente, telCliente);
                plainCadNome.setText("Cadastrado com sucesso");
            }
        });

    }
    public void ValidarCadastro(RequestQueue solicitacao, String nome, String cpf, String senha, String email, String telefone) {
        CadastroCliente cadastrar = new CadastroCliente(nome,cpf , senha, email, telefone);
        CadastroCliente.ValidarCadastroCliente(cadastrar, solicitacao, new ICadastroCliente() {
            @Override
            public void onResultado(boolean cadastrado) {
                if (cadastrado) {
                    Toast.makeText(Tela_Cadastro.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Tela_Cadastro.this, "Erro ao cadastrar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}