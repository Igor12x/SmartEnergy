package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Interfaces.ICadastroCliente;
import Models.CadastroCliente;
import Models.Cliente;
import Models.ClienteLogin;
import Models.MascaraCPF;
import Models.MascaraTelefone;

public class Tela_Cadastro extends AppCompatActivity {
    private TextView plainCadNome, plainCadEmail, plainCadTel, plainCadCpf, plainCadSenha, plainCadConfirmarSenha, txtTermosUso, plainCadSobrenome;
    private Button btnCad;
    private CheckBox checkBoxTermos;
    private ImageButton btnVoltaCad;


    private ImageButton btnCadSenha, btnCadConfirmaSenha;
    private String nomeCliente, senhaCliente, cpfCliente, emailCliente, telCliente, senha1, senha2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        RequestQueue solicitacao = Volley.newRequestQueue(this);

        //referencias
        plainCadNome = findViewById(R.id.plainCadNome);
        plainCadSobrenome = findViewById(R.id.plainCadSobrenome);
        plainCadEmail = findViewById(R.id.plainCadEmail);
        plainCadTel = findViewById(R.id.plainCadTel);
        plainCadCpf = findViewById(R.id.plainCadCpf);
        plainCadSenha = findViewById(R.id.plainCadSenha);
        plainCadConfirmarSenha = findViewById(R.id.plainCadConfirmarSenha);
        btnCad = findViewById(R.id.btnCad);
        btnVoltaCad = findViewById(R.id.btnVoltaCad);
        btnCadSenha = findViewById(R.id.btnCadSenha);
        btnCadConfirmaSenha = findViewById(R.id.btnCadConfirmaSenha);
        plainCadCpf.addTextChangedListener(new MascaraCPF((EditText) plainCadCpf,11));
        plainCadTel.addTextChangedListener(new MascaraTelefone((EditText) plainCadTel,11 ));
        checkBoxTermos = findViewById(R.id.checkBoxTermos);
        txtTermosUso = findViewById(R.id.txtTermosUso);

        txtTermosUso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tela_Cadastro.this, Tela_Termos_Uso.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

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
                if (checkBoxTermos.isChecked()) {
                    if (verificarCamposVazios()) {
                        Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                    } else if(!verificarEmail(plainCadEmail.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Digite um email válido", Toast.LENGTH_SHORT).show();
                    }else if(!validarTelefone(plainCadTel.getText().toString().replaceAll("[^\\d]", "")))
                    {
                        Toast.makeText(Tela_Cadastro.this, "Digite um telefone válido", Toast.LENGTH_SHORT).show();
                    } else if(!validarCPF(plainCadCpf.getText().toString().replaceAll("[^\\d]", "")))
                    {
                        Toast.makeText(Tela_Cadastro.this, "Digite um CPF válido", Toast.LENGTH_SHORT).show();
                    }
                        else if (!plainCadSenha.getText().toString().equals(plainCadConfirmarSenha.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "A senha e a confirmação de senha não correspondem", Toast.LENGTH_SHORT).show();
                    } else {
                        Cliente cadastrar = new Cliente(
                                plainCadNome.getText().toString(),
                                plainCadSobrenome.getText().toString(),
                                plainCadCpf.getText().toString().replaceAll("[^\\d]", ""),
                                plainCadEmail.getText().toString(),
                                plainCadTel.getText().toString().replaceAll("[^\\d]", ""),
                                plainCadSenha.getText().toString());
                        ValidarCadastro(solicitacao, cadastrar);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor, aceite os termos de uso", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCadSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean mostrarSenha = !plainCadSenha.getTransformationMethod().equals(PasswordTransformationMethod.getInstance());

                plainCadSenha.setTransformationMethod(mostrarSenha ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());

                btnCadSenha.setImageResource(mostrarSenha ? R.drawable.icone_olho_branco : R.drawable.olho_fechado_branco);
            }
        });

        btnCadConfirmaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean mostrarSenha = !plainCadConfirmarSenha.getTransformationMethod().equals(PasswordTransformationMethod.getInstance());

                plainCadConfirmarSenha.setTransformationMethod(mostrarSenha ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());

                btnCadConfirmaSenha.setImageResource(mostrarSenha ? R.drawable.icone_olho_branco : R.drawable.olho_fechado_branco);
            }
        });
    }
    public boolean verificarEmail(String emailCliente) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailCliente).matches()) {
            return true;
        } else {
            return false;
        }
    }
    private boolean verificarCamposVazios() {
        String nome = plainCadNome.getText().toString();
        String cpf = plainCadCpf.getText().toString();
        String email = plainCadEmail.getText().toString();
        String telefone = plainCadTel.getText().toString();
        String senha = plainCadSenha.getText().toString();

        return nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty();
    }

    private boolean validarTelefone(String telefone) {
        Log.d("TELEFONE", " >>>>>>>>>>> " + telefone);
        Log.d("TELEFONE", " >>>>>>>>>>> " + telefone.length());
        if (telefone.length() != 10 && telefone.length() != 11) {
            return false;
        }
        return true;
    }
    private boolean validarCPF(String cpf) {
        // Remover caracteres não numéricos do CPF
        cpf = cpf.replaceAll("[^\\d]", "");

        // Verificar se o CPF possui 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verificar se todos os dígitos do CPF são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Calcular o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }

        // Verificar o primeiro dígito verificador
        if (cpf.charAt(9) - '0' != primeiroDigito) {
            return false;
        }

        // Calcular o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }

        // Verificar o segundo dígito verificador
        if (cpf.charAt(10) - '0' != segundoDigito) {
            return false;
        }

        return true;
    }



    public void ValidarCadastro(RequestQueue solicitacao, Cliente cadastrar) {
        CadastroCliente.ValidarCadastroCliente(cadastrar, solicitacao, new ICadastroCliente() {
            @Override
            public void onResultado(String nome) {
                Intent intent = new Intent(getApplicationContext(), Tela_Bem_Vindo.class);
                SharedPreferences.Editor gravar =
                        getSharedPreferences("usuario", MODE_PRIVATE).edit();
                gravar.putString("nome", plainCadNome.getText().toString());
                gravar.commit();
                startActivity(intent);
            }

            @Override
            public void onErro(String mensagemErro) {
                Toast.makeText(getApplicationContext(), mensagemErro, Toast.LENGTH_SHORT).show();

            }
        });
    }
}