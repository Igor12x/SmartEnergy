package com.example.projetotcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Models.AlterarCadastro;
import Models.Residencia;
import Models.ResidenciaAdapter;

public class Tela_Perfil extends AppCompatActivity {
    private TextView txtNome;
    private AlterarCadastro dados;
    private String emailSemAlteracao = null;
    private String telSemAlteracao = null;
    private Spinner spinnerCasas;
    private SharedPreferences ler;
    private ImageButton btnVoltaPerfil, imgBtnEdit;
    private EditText plainPerfilE, plainPerfilTel, plainCEPPerfil, plainCidadePerfil,
            plainEstadoPerfil, plainBairroPerfil, plainNumPerfil, plainLogradouroPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);

        RequestQueue solicitacao = Volley.newRequestQueue(this);

        inicializarViews();

        BtnVoltaPerfil();
        ImgBtnEdit(solicitacao);

        carregarDadosPerfil();
        carregarDadosResidencia(solicitacao, getIdClienteSharedPreferences());
    }

    private int getIdClienteSharedPreferences() {
        try {
            ler = getSharedPreferences("usuario", MODE_PRIVATE);
            return getSharedPreferences("usuario", MODE_PRIVATE).getInt("codigo", 0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Perfil.this, "Erro ao carregar seus dados de perfil. Por favor, tente logar novamente ou entre em contato com o suporte.", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void ImgBtnEdit(RequestQueue solicitacao) {
        imgBtnEdit.setOnClickListener(v -> {

            String ImgAtual = imgBtnEdit.getTag().toString();

            if (ImgAtual.equals("ImgEditar")) {
                imgBtnEdit.setImageResource(R.drawable.iconeok);
                imgBtnEdit.setTag("ImgConfirmar");
                emailSemAlteracao = plainPerfilE.getText().toString();
                telSemAlteracao = plainPerfilTel.getText().toString();
                plainPerfilE.setEnabled(true);
                plainPerfilTel.setEnabled(true);
            } else if (!emailSemAlteracao.equals(plainPerfilE.getText().toString()) || !telSemAlteracao.equals(plainPerfilTel.getText().toString())) {
                dados = new AlterarCadastro(plainPerfilE.getText().toString(), plainPerfilTel.getText().toString());
                editarCadastro(solicitacao, dados, ler);
                imgBtnEdit.setImageResource(R.drawable.edit_perfil);
                imgBtnEdit.setTag("ImgEditar");
            } else {
                Toast.makeText(Tela_Perfil.this, "Você não realizou alterações", Toast.LENGTH_SHORT).show();
                imgBtnEdit.setImageResource(R.drawable.edit_perfil);
                imgBtnEdit.setTag("ImgEditar");
                plainPerfilE.setEnabled(false);
                plainPerfilTel.setEnabled(false);
            }
        });
    }

    private void BtnVoltaPerfil() {
        btnVoltaPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
            startActivity(intent);
        });
    }

    private void inicializarViews() {
        btnVoltaPerfil = findViewById(R.id.btnVoltaPerfil);
        imgBtnEdit = findViewById(R.id.imgBtnEdit);
        plainPerfilE = findViewById(R.id.plainPerfilE);
        plainPerfilTel = findViewById(R.id.plainPerfilTel);
        txtNome = findViewById(R.id.txtNome);
        plainCEPPerfil = findViewById(R.id.plainCEPPerfil);
        plainCidadePerfil = findViewById(R.id.plainCidadePerfil);
        plainEstadoPerfil = findViewById(R.id.plainEstadoPerfil);
        plainBairroPerfil = findViewById(R.id.plainBairroPerfil);
        plainNumPerfil = findViewById(R.id.plainNumPerfil);
        plainLogradouroPerfil = findViewById(R.id.plainLogradouroPerfil);
        spinnerCasas = findViewById(R.id.spinnerCasas);
    }

    private void carregarDadosPerfil() {
        try {
            ler = getSharedPreferences("usuario", MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Perfil.this, "Erro ao carregar seus dados de perfil. Por favor, tente logar novamente ou entre em contato com o suporte.", Toast.LENGTH_SHORT).show();
        }
        String nomeCompleto = ler.getString("nome", "") + " " + ler.getString("sobrenome", "");
        txtNome.setText(nomeCompleto);
        plainPerfilE.setText(ler.getString("email", ""));
        plainPerfilTel.setText(ler.getString("telefone", ""));
    }

    private void editarCadastro(RequestQueue solicitacao, AlterarCadastro dados, SharedPreferences ler) {
        AlterarCadastro.Alterar(dados, ler.getInt("codigo", 0), solicitacao, clienteAtualizado -> {
            try {
                SharedPreferences salvar =
                        getSharedPreferences("usuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor gravar = salvar.edit();
                gravar.putString("nome", clienteAtualizado.getNome());
                gravar.putString("cpf", clienteAtualizado.getCpf());
                gravar.putString("email", clienteAtualizado.getEmail());
                gravar.putString("telefone", clienteAtualizado.getTelefone());
                gravar.putString("senha", clienteAtualizado.getSenha());
                gravar.putInt("codigo", clienteAtualizado.getCodigo());
                gravar.apply();
                plainPerfilE.setText(clienteAtualizado.getEmail());
                plainPerfilTel.setText(clienteAtualizado.getTelefone());
                plainPerfilE.setEnabled(false);
                plainPerfilTel.setEnabled(false);
                Toast.makeText(Tela_Perfil.this, "Dados alterados com sucesso", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Tela_Perfil.this, "Erro ao alterar o cadastro. Por favor, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTextResidencia(Residencia residenciaSelecionada) {
        plainLogradouroPerfil.setText(residenciaSelecionada.getLogradouro());
        plainCEPPerfil.setText(residenciaSelecionada.getCep());
        plainCidadePerfil.setText(residenciaSelecionada.getMunicipio());
        plainBairroPerfil.setText(residenciaSelecionada.getBairro());
        plainNumPerfil.setText(getString(R.string.perfil_numero, String.valueOf(residenciaSelecionada.getNumero())));
        plainEstadoPerfil.setText(residenciaSelecionada.getUf());
    }

    private void setEnabledResidencia() {
        plainLogradouroPerfil.setEnabled(false);
        plainCEPPerfil.setEnabled(false);
        plainCidadePerfil.setEnabled(false);
        plainBairroPerfil.setEnabled(false);
        plainNumPerfil.setEnabled(false);
        plainEstadoPerfil.setEnabled(false);
    }

    private void carregarDadosResidencia(RequestQueue solicitacao, int idCliente) {
        Residencia.listarResidencias(idCliente, solicitacao, residencias -> {
            try {
                ResidenciaAdapter adaptador = new ResidenciaAdapter(getApplicationContext(), residencias);
                spinnerCasas.setAdapter(adaptador);
                spinnerCasas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Residencia residenciaSelecionada = (Residencia) parent.getSelectedItem();
                        setTextResidencia(residenciaSelecionada);
                        setEnabledResidencia();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        setEnabledResidencia();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Tela_Perfil.this, "Erro ao carregar dados da residencia. Por favor, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}