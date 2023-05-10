package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.IAlterarCadastro;
import Models.AlterarCadastro;
import Models.Cliente;

public class Tela_Perfil extends AppCompatActivity {

    private AlterarCadastro dados;

    private ImageButton btnVoltaPerfil, imgBtnEdit;
    private EditText plainPerfilE, plainPerfilTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);

        //referencias
        btnVoltaPerfil = findViewById(R.id.btnVoltaPerfil);
        imgBtnEdit = findViewById(R.id.imgBtnEdit);
        plainPerfilE = findViewById(R.id.plainPerfilE);
        plainPerfilTel = findViewById(R.id.plainPerfilTel);
        SharedPreferences ler = getSharedPreferences("usuario", MODE_PRIVATE);

        RequestQueue solicitacao = Volley.newRequestQueue(this);

        //voltando para tela de inicio principal
        btnVoltaPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
                startActivity(intent);
            }
        });
        imgBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plainPerfilE.setEnabled(true);
                plainPerfilTel.setEnabled(true);
                dados = new AlterarCadastro(plainPerfilE.getText().toString(),
                        plainPerfilTel.getText().toString());
                alterarCadastro(solicitacao, dados, ler);
            }
        });
    }
    public void alterarCadastro(RequestQueue solicitacao, AlterarCadastro dados, SharedPreferences ler){

 AlterarCadastro.Alterar(dados, ler.getInt("codigo", 0), solicitacao, new IAlterarCadastro() {
     @Override
     public void onResultado(Cliente clienteAtualizado) {
         SharedPreferences.Editor gravar =
                 getSharedPreferences("usuario", MODE_PRIVATE).edit();
         gravar.putString("nome", clienteAtualizado.getNome());
         gravar.putString("cpf", clienteAtualizado.getCpf());
         gravar.putString("email", clienteAtualizado.getEmail());
         gravar.putString("telefone", clienteAtualizado.getTelefone());
         gravar.putString("senha", clienteAtualizado.getSenha());
         gravar.putInt("codigo", clienteAtualizado.getCodigo());
     }
 });
    }
}