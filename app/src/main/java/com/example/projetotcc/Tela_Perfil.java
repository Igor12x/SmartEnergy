package com.example.projetotcc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Models.AlterarCadastro;
import Models.Residencia;
import Models.ResidenciaAdapter;

public class Tela_Perfil extends AppCompatActivity {


    //private Button btnSim, btnNao;
    private TextView txtNome;
    private String emailSemAlteracao = null;
    private String telSemAlteracao = null;
    private Spinner spinnerCasas;
    private SharedPreferences ler;
    private ImageButton btnVoltaPerfil, ibtnDesconectar, imgBtnEdit;
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
        BtnDesconectar();

        carregarDadosPerfil();
        carregarDadosResidencia(solicitacao, getIdClienteSharedPreferences());
    }

    private void BtnDesconectar() {
        ibtnDesconectar.setOnClickListener(view -> {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(Tela_Perfil.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Tela_Perfil.this).inflate(R.layout.layout_dialog, findViewById(R.id.layoutDialogContainer));
                builder.setView(view1);
                final AlertDialog alertDialog = builder.create();

                Button btnSim = view1.findViewById(R.id.btnSim);
                //btnSim.setOnClickListener(v -> desconectar(alertDialog));
                btnSim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        desconectar(alertDialog);
                        Intent intent = new Intent(Tela_Perfil.this, Tela_Inicial.class);
                        startActivity(intent);
                    }
                });

                Button btnNao = view1.findViewById(R.id.btnNao);

                btnNao.setOnClickListener(v -> alertDialog.dismiss());
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Tela_Perfil.this, "Ocorreu um erro ao exibir o diálogo de desconexão. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void desconectar(AlertDialog alertDialog) {
        try {
            //Handler handler = new Handler();
            //handler.postDelayed(() -> {
                alertDialog.dismiss();
                limparSharedPreferences();
                //Intent intent = new Intent(getApplicationContext(), Tela_Inicial.class);
                //startActivity(intent);
           // }, 500); // ajuste o valor do atraso conforme necessário
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Perfil.this, "Ocorreu um erro ao desconectar. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void limparSharedPreferences() {
        try {
            ler = getSharedPreferences("usuario", MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = ler.edit();
        editor.clear();
        editor.apply();
    }

    private int getIdClienteSharedPreferences() {
        try {
            ler = getSharedPreferences("usuario", MODE_PRIVATE);
            return ler.getInt("codigo", 0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Perfil.this, "Erro ao carregar seus dados de perfil. Por favor, tente logar novamente ou entre em contato com o suporte.", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void ImgBtnEdit(RequestQueue solicitacao) {
        imgBtnEdit.setOnClickListener(v -> {
            String imgAtual = imgBtnEdit.getTag().toString();

            try {
                if (imgAtual.equals("ImgEditar")) {
                    habilitarEdicao();
                } else if (!emailSemAlteracao.equals(plainPerfilE.getText().toString())
                        || !telSemAlteracao.equals(plainPerfilTel.getText().toString())) {
                    if (camposPreenchidos()) {
                        realizarAlteracao(solicitacao);
                    } else {
                        Toast.makeText(Tela_Perfil.this, "Há campos vazios, preencha todos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Tela_Perfil.this, "Não foi realizada nenhuma alteração", Toast.LENGTH_SHORT).show();
                    restaurarEstadoInicial();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Tela_Perfil.this, "Ocorreu um erro. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void habilitarEdicao() {
        try {
            imgBtnEdit.setImageResource(R.drawable.iconeok);
            imgBtnEdit.setTag("ImgConfirmar");
            emailSemAlteracao = plainPerfilE.getText().toString();
            telSemAlteracao = plainPerfilTel.getText().toString();
            plainPerfilE.setEnabled(true);
            plainPerfilTel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Perfil.this, "Ocorreu um erro. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean camposPreenchidos() {
        try {
            return !plainPerfilE.getText().toString().isEmpty() && !plainPerfilTel.getText().toString().isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Perfil.this, "Ocorreu um erro. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void realizarAlteracao(RequestQueue solicitacao) {
        try {
            AlterarCadastro dados = new AlterarCadastro(plainPerfilE.getText().toString(), plainPerfilTel.getText().toString());
            editarCadastro(solicitacao, dados, ler);
            restaurarEstadoInicial();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Perfil.this, "Ocorreu um erro. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void restaurarEstadoInicial() {
        try {
            imgBtnEdit.setImageResource(R.drawable.edit_perfil);
            imgBtnEdit.setTag("ImgEditar");
            plainPerfilE.setEnabled(false);
            plainPerfilTel.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Perfil.this, "Ocorreu um erro. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void BtnVoltaPerfil() {
        btnVoltaPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Tela_Principal.class);
            startActivity(intent);
        });
    }

    private void inicializarViews() {
        btnVoltaPerfil = findViewById(R.id.btnVoltaPerfil);
        ibtnDesconectar = findViewById(R.id.ibtnDesconectar);
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