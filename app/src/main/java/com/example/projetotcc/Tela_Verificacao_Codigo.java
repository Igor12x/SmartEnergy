package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Tela_Verificacao_Codigo extends AppCompatActivity {

    private ImageButton btnVoltaVerificacao;
    private Intent intentRedefinirSenha, intentEsqueceuSenha;
    private EditText[] editTexts;
    private Button btnVerificarCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_verificacao_codigo);

        btnVoltaVerificacao = findViewById(R.id.btnVoltaVerificacao);
        btnVerificarCodigo = findViewById(R.id.buttonCodVerificacao);
        editTexts = new EditText[]{
                findViewById(R.id.plainCodVerificacao1),
                findViewById(R.id.plainCodVerificacao2),
                findViewById(R.id.plainCodVerificacao3),
                findViewById(R.id.plainCodVerificacao4)
        };

        for (int i = 0; i < editTexts.length; i++) {
            final EditText editTextAtual = editTexts[i];
            final EditText editTextSeguinte = (i < editTexts.length - 1) ? editTexts[i + 1] : null;

            editTextAtual.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    int maxLength = -1;

                    InputFilter[] filters = editTextAtual.getFilters();
                    for (InputFilter filter : filters) {
                        if (filter instanceof InputFilter.LengthFilter) {
                            maxLength = ((InputFilter.LengthFilter) filter).getMax();
                            break;
                        }
                    }

                    if (maxLength != -1 && editTextAtual.getText().length() >= maxLength && editTextSeguinte != null) {
                        editTextSeguinte.requestFocus();
                    }
                    return false;
                }
            });
        }

        btnVerificarCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCamposPreenchidos()) {
                    validarCodigoVerificacao(obterCodigoVerificacao());
                } else {
                    Toast.makeText(Tela_Verificacao_Codigo.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnVoltaVerificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentEsqueceuSenha = new Intent(getApplicationContext(), Tela_Esqueceu_Senha.class);
                startActivity(intentEsqueceuSenha);
            }
        });
    }

    private String obterCodigoVerificacao() {
        StringBuilder codigoVerificacaoDigitado = new StringBuilder();
        for (EditText editText: editTexts) {
            codigoVerificacaoDigitado.append(editText.getText().toString());
        }
        return codigoVerificacaoDigitado.toString();
    }

    private boolean verificarCamposPreenchidos() {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void validarCodigoVerificacao(String codigoDigitado) {
        SharedPreferences dadosRedefinirSenha = getSharedPreferences("dadosRecuperacao", Context.MODE_PRIVATE);
        String codigoVerificacaoEmail = dadosRedefinirSenha.getString("codigoVerificacao", "");

        if (codigoVerificacaoEmail.equals(codigoDigitado)) {
            SharedPreferences.Editor apagarCodigoVerificacao = dadosRedefinirSenha.edit();
            apagarCodigoVerificacao.remove("codigoVerificacao");
            apagarCodigoVerificacao.apply();
            intentRedefinirSenha = new Intent(getApplicationContext(), Tela_Redefinir_Senha.class);
            startActivity(intentRedefinirSenha);
        } else {
            Toast.makeText(this, "Código digitado é inválido, tente novamente", Toast.LENGTH_SHORT).show();
        }
    }
}