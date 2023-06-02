package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.IRecuperarSenhaRedefinir;
import Models.Cliente;
import Models.RecuperarSenhaCliente;

public class Tela_Redefinir_Senha extends AppCompatActivity {

    private ImageButton btnVoltaNovaSenha, btnCadSenhaRedefinir, btnConfSenhaRedefinir;
    private Button btnConfirmarSenha;
    private Intent intentTelaEsqueceuSenha, intentTelaSenhaAlterada;
    private EditText[] editTextsSenhas;
    private EditText editTextNovaSenha, editTextNvSenha, editTextCfmSenha;
    private RequestQueue solicitacao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_redefinir_senha);
        solicitacao = Volley.newRequestQueue(this);
        editTextNvSenha = findViewById(R.id.editTextNvSenha);
        editTextCfmSenha = findViewById(R.id.editTextCfmSenha);

        btnCadSenhaRedefinir = findViewById(R.id.btnCadSenhaRedefinir);
        btnConfSenhaRedefinir = findViewById(R.id.btnConfSenhaRedefinir);
        btnVoltaNovaSenha = findViewById(R.id.btnVoltaNovaSenha);
        btnConfirmarSenha = findViewById(R.id.btnCfmSenha);
        editTextNovaSenha = findViewById(R.id.editTextNvSenha);
        editTextsSenhas = new EditText[] {
                findViewById(R.id.editTextNvSenha),
                findViewById(R.id.editTextCfmSenha)
        };

        btnCadSenhaRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mostrarSenhaInicial = !editTextNvSenha.getTransformationMethod().equals(PasswordTransformationMethod.getInstance());
                editTextNvSenha.setTransformationMethod(mostrarSenhaInicial ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                // Atualize o ícone do botão
                btnCadSenhaRedefinir.setImageResource(mostrarSenhaInicial ? R.drawable.olho_fechado : R.drawable.icone_olho_azul);
            }
        });

        btnConfSenhaRedefinir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mostrarSenhaInicialConf = !editTextCfmSenha.getTransformationMethod().equals(PasswordTransformationMethod.getInstance());
                editTextCfmSenha.setTransformationMethod(mostrarSenhaInicialConf ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                // Atualize o ícone do botão
                btnConfSenhaRedefinir.setImageResource(mostrarSenhaInicialConf ? R.drawable.olho_fechado : R.drawable.icone_olho_azul);
            }
        });


        btnConfirmarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (verificarCamposPreenchidos()) {
                   if (validarCamposSenha(editTextsSenhas)) {
                       Cliente novaSenhaCliente = new Cliente(buscarEmailCliente(), editTextNovaSenha.getText().toString());
                       atualizarSenhaCliente(novaSenhaCliente);   
                   } else {
                       Toast.makeText(Tela_Redefinir_Senha.this, "Os campos de senha estão divergentes", Toast.LENGTH_SHORT).show();
                   }                    
               } else {
                   Toast.makeText(Tela_Redefinir_Senha.this, "Preencha os dois campos!", Toast.LENGTH_SHORT).show();
               }
            }
        });

        btnVoltaNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentTelaEsqueceuSenha =  new Intent(getApplicationContext(), Tela_Esqueceu_Senha.class);
                startActivity(intentTelaEsqueceuSenha);
                editTextsSenhas = new EditText[] {
                  findViewById(R.id.editTextNvSenha),
                  findViewById(R.id.editTextCfmSenha)
                };
            }
        });
    }

    private void atualizarSenhaCliente(Cliente senhaNovaCliente) {
        try {
            RecuperarSenhaCliente.RedefinirSenhaCliente(senhaNovaCliente, solicitacao, getApplicationContext(), new IRecuperarSenhaRedefinir() {
                @Override
                public void onResultado(String alterado) {
                    intentTelaSenhaAlterada =  new Intent(getApplicationContext(), Tela_Senha_Alterada.class);
                    startActivity(intentTelaSenhaAlterada);
                }

                @Override
                public void onError(String mensagemErro) {
                    Toast.makeText(Tela_Redefinir_Senha.this, mensagemErro, Toast.LENGTH_LONG).show();

                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String buscarEmailCliente() {
        SharedPreferences dadosRedefinirSenha = getSharedPreferences("dadosRecuperacao", Context.MODE_PRIVATE);
        String emailVerificacao = dadosRedefinirSenha.getString("email", "");
        return  emailVerificacao;
    }

    private boolean verificarCamposPreenchidos() {
        for (EditText editText : editTextsSenhas) {
            if (editText.getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean validarCamposSenha(EditText[] editTextsSenhas) {
        String valorCampoAnterior = null;

        for (EditText campo : editTextsSenhas) {
            String valorCampoAtual = campo.getText().toString();

            if (valorCampoAnterior == null) {
                valorCampoAnterior = valorCampoAtual;
            } else {
                if (!valorCampoAtual.equals(valorCampoAnterior)) {
                    return false;
                }
            }
        }

        return true;
    }
}