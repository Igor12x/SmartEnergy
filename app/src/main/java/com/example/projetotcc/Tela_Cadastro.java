package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Models.CadastroCliente;
import Models.Cliente;
import Models.LoginCliente;

public class Tela_Cadastro extends AppCompatActivity {
    private TextView plainCadNome, plainCadEmail, plainCadTel, plainCadCpf, plainCadSenha, plainCadConfirmarSenha;
    private Button btnCad;
    private String nomeCliente, senhaCliente, cpfCliente, emailCliente, telCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        RequestQueue solicitacao = Volley.newRequestQueue(this);

        plainCadNome = findViewById(R.id.plainCadNome);
        plainCadEmail = findViewById(R.id.plainCadEmail);
        plainCadTel = findViewById(R.id.plainCadTel);
        plainCadCpf = findViewById(R.id.plainCadCpf);
        plainCadSenha = findViewById(R.id.plainCadSenha);
        plainCadConfirmarSenha = findViewById(R.id.plainCadConfirmarSenha);

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeCliente = plainCadNome.getText().toString();
                if (plainCadSenha == plainCadConfirmarSenha){
                    senhaCliente = plainCadSenha.getText().toString();
                }else{
                    Toast.makeText(Tela_Cadastro.this, "Os campos da senha não coincidem!!!", Toast.LENGTH_SHORT).show();
                }
                cpfCliente = plainCadCpf.getText().toString();
                emailCliente = plainCadEmail.getText().toString();
                telCliente = plainCadTel.getText().toString();
            }
        });

    }
    public void ValidarCadastro(RequestQueue solicitacao, String nome, String senha, String cpf, String email, String telefone) {
        CadastroCliente cadastrar = new CadastroCliente(nome, senha, cpf, email, telefone);
        CadastroCliente.ValidarCadastroCliente(cadastrar, solicitacao, new CadastroCliente.ValidarCadastroListener() {
            @Override
            public void onResultado(Cliente clienteCadastrado) {
                Toast.makeText(Tela_Cadastro.this, "Cadastrado com sucesso" + clienteCadastrado.getNome(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}