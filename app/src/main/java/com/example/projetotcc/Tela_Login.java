package com.example.projetotcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.ILoginCliente;
import Models.Cliente;
import Models.ClienteLogin;
import Models.MascaraCPF;
import Models.Residencia;

public class Tela_Login extends AppCompatActivity {
    private TextView txtCpf, txtSenha, textLogEsqueceuSenha, textLogRegistreAgora;
    private Button btnLogar;
    private ImageButton btnVoltaLogin, btnLogMostrarSenha;
    private String cpfCliente, senhaCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        RequestQueue solicitacao = Volley.newRequestQueue(this);
        initializeViews();
        setOnClick();
        setMascaraCPF();
        btnMostrarSenha();
        btnLogar(solicitacao);
    }
    private void btnMostrarSenha(){
        btnLogMostrarSenha.setOnClickListener(v -> {
            boolean mostrarSenha = !txtSenha.getTransformationMethod().equals(PasswordTransformationMethod.getInstance());
            txtSenha.setTransformationMethod(mostrarSenha ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
            btnLogMostrarSenha.setImageResource(mostrarSenha ? R.drawable.olho_fechado : R.drawable.icone_olho_azul);
        });
    }
    private void btnLogar(RequestQueue solicitacao){
        btnLogar.setOnClickListener(view -> {
            if (!txtCpf.getText().toString().isEmpty() && !txtSenha.getText().toString().isEmpty()) {
                cpfCliente = txtCpf.getText().toString().replaceAll("\\D", "");
                senhaCliente = txtSenha.getText().toString();
                ValidarLogin(solicitacao, cpfCliente, senhaCliente);
                txtCpf.setText("");
                txtSenha.setText("");
            } else {
                Toast.makeText(Tela_Login.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeViews() {
        txtCpf = findViewById(R.id.txtCpf);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLog);
        btnVoltaLogin = findViewById(R.id.btnVoltaLogin);
        btnLogMostrarSenha = findViewById(R.id.btnLogMostrarSenha);
        textLogEsqueceuSenha = findViewById(R.id.textLogEsqueceuSenha);
        textLogRegistreAgora = findViewById(R.id.textLogRegistreAgora);
    }

    private void setOnClick() {
        textLogRegistreAgora.setOnClickListener(view -> abrirTelaCadastro());
        btnVoltaLogin.setOnClickListener(view -> abrirTelaInicial());
        textLogEsqueceuSenha.setOnClickListener(view -> abrirTelaEsqueceuSenha());
    }
    private void setMascaraCPF() {
        txtCpf.addTextChangedListener(new MascaraCPF((EditText) txtCpf, 11));
    }

    private void abrirTelaCadastro() {
        Intent intent = new Intent(getApplicationContext(), Tela_Cadastro.class);
        startActivity(intent);
    }

    private void abrirTelaInicial() {
        Intent intent = new Intent(getApplicationContext(), Tela_Inicial.class);
        startActivity(intent);
    }
    private void abrirTelaPrincipal() {
        Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
        startActivity(intent);
    }

    private void abrirTelaEsqueceuSenha() {
        Intent intent = new Intent(getApplicationContext(), Tela_Esqueceu_Senha.class);
        startActivity(intent);
    }
    private void salvarDadosClienteNoSharedPreferences(Cliente clienteLogado) {
        SharedPreferences sharedPreferences = getSharedPreferences("usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nome", clienteLogado.getNome());
        editor.putString("sobrenome", clienteLogado.getSobrenome());
        editor.putString("cpf", clienteLogado.getCpf());
        editor.putString("email", clienteLogado.getEmail());
        editor.putString("telefone", clienteLogado.getTelefone());
        editor.putString("senha", clienteLogado.getSenha());
        editor.putInt("codigo", clienteLogado.getCodigo());
        editor.apply();
    }

    private void ValidarLogin(RequestQueue solicitacao, String cpf, String senha) {
        ClienteLogin login = new ClienteLogin(cpf, senha);
        ClienteLogin.ValidarLoginCliente(login, solicitacao, new ILoginCliente() {
            @Override
            public void onResultado(Cliente clienteLogado) {
                verificarResidenciaCadastrada(solicitacao, clienteLogado.getCpf(), clienteLogado);
            }

            @Override
            public void onErro(String mensagemErro) {
                Toast.makeText(Tela_Login.this, mensagemErro, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verificarResidenciaCadastrada(RequestQueue solicitacao, String cpf, Cliente clienteLogado) {
        Residencia.verificarResidenciaCadastrada(cpf, solicitacao, cadastrado -> {
            if (cadastrado) {
                salvarDadosClienteNoSharedPreferences(clienteLogado);
                abrirTelaPrincipal();
            } else {
                Toast.makeText(Tela_Login.this, "Você ainda não possui nosso medidor inteligente, aguarde o contato do nosso consultor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
