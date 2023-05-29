package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.ILoginCliente;
import Models.Cliente;
import Models.ClienteLogin;
import Models.MascaraCPF;

public class Tela_Login extends AppCompatActivity {
    private TextView txtCpf, txtSenha, textLogEsqueceuSenha;
    private Button btnLogar;

    private ImageButton btnVoltaLogin, btnLogMostrarSenha;
    private String cpfCliente, senhaCliente;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        //getSupportActionBar().hide();

        RequestQueue solicitacao = Volley.newRequestQueue(this);
    try {
        //referencias
        txtCpf = findViewById(R.id.txtCpf);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLog);
        btnVoltaLogin = findViewById(R.id.btnVoltaLogin);
        btnLogMostrarSenha = findViewById(R.id.btnLogMostrarSenha);
        intent = new Intent(getApplicationContext(), Tela_Principal.class);
        textLogEsqueceuSenha =  findViewById(R.id.textLogEsqueceuSenha);

        txtCpf.addTextChangedListener(new MascaraCPF((EditText) txtCpf, 11) );

        //voltando para tela inicial
        btnVoltaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Inicial.class);
                startActivity(intent);
            }
        });

        //indo para a tela de Esqueci a senha
        textLogEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Tela_Esqueceu_Senha.class);
                startActivity(intent);
            }
        });

        //voltando para tela inicial
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpfCliente = txtCpf.getText().toString().replaceAll("[^\\d]", "");
                senhaCliente = txtSenha.getText().toString();
                ValidarLogin(solicitacao, cpfCliente, senhaCliente);
                txtCpf.setText("");
                txtSenha.setText("");
            }
        });

    } catch (Exception e) {
        Log.d("ERRO LOGIN", "login" + e);
    }


        //evento de mudança do icone da senha
        btnLogMostrarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean mostrarSenha = !txtSenha.getTransformationMethod().equals(PasswordTransformationMethod.getInstance());

                txtSenha.setTransformationMethod(mostrarSenha ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());

                // Atualize o ícone do botão
                btnLogMostrarSenha.setImageResource(mostrarSenha ? R.drawable.olho_fechado : R.drawable.icone_olho_azul);
            }


        });

    }

    public void ValidarLogin(RequestQueue solicitacao, String cpf, String senha) {

        ClienteLogin login = new ClienteLogin(cpf, senha);
        ClienteLogin.ValidarLoginCliente(login, solicitacao, new ILoginCliente() {
            @Override
            public void onResultado(Cliente clienteLogado) {
                SharedPreferences.Editor gravar =
                        getSharedPreferences("usuario", MODE_PRIVATE).edit();
                gravar.putString("nome", clienteLogado.getNome());
                gravar.putString("sobrenome", clienteLogado.getSobrenome());
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