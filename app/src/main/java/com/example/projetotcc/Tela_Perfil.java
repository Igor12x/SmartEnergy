package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

import Interfaces.IAlterarCadastro;
import Interfaces.IResidencia;
import Models.AlterarCadastro;
import Models.Cliente;
import Models.Residencia;
import Models.ResidenciaAdapter;

public class Tela_Perfil extends AppCompatActivity {
    private TextView txtNome;
    private AlterarCadastro dados;
    private Spinner spinnerCasas;
    private SharedPreferences ler;
    private ImageButton btnVoltaPerfil, imgBtnEdit;
    private EditText plainPerfilE, plainPerfilTel, plainCEPPerfil, plainCidadePerfil,
            plainEstadoPerfil, plainBairroPerfil, plainNumPerfil, plainLogradouroPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_perfil);

        //referencias
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

        ler = getSharedPreferences("usuario", MODE_PRIVATE);
        int idCliente = ler.getInt("codigo", 0);
        RequestQueue solicitacao = Volley.newRequestQueue(this);

        //preenchendos os campos da tela de perfil
        txtNome.setText(ler.getString("nome", "") + " " + ler.getString("sobrenome",""));
        plainPerfilE.setText(ler.getString("email", ""));
        plainPerfilTel.setText(ler.getString("telefone", ""));
        buscarResidencias(solicitacao, idCliente);

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

                String ImgAtual = imgBtnEdit.getTag().toString();

                if (!ImgAtual.equals("ImgEditar")) {
                    Log.d("IFBotãoImg", "cheguei aqui no if");
                    dados = new AlterarCadastro(plainPerfilE.getText().toString(),
                            plainPerfilTel.getText().toString());
                    imgBtnEdit.setImageResource(R.drawable.edit_perfil);
                    alterarCadastro(solicitacao, dados, ler);
                    plainPerfilE.setEnabled(false);
                    plainPerfilTel.setEnabled(false);
                    imgBtnEdit.setTag("ImgEditar");
                } else {
                    Log.d("ElseBotãoImg", "cheguei aqui");
                    imgBtnEdit.setTag("ImgConfirmar");
                    imgBtnEdit.setImageResource(R.drawable.iconeok);
                    plainPerfilE.setEnabled(true);
                    plainPerfilTel.setEnabled(true);
                }

            }
        });
    }
    public void alterarCadastro(RequestQueue solicitacao, AlterarCadastro dados, SharedPreferences ler){

 AlterarCadastro.Alterar(dados, ler.getInt("codigo", 0), solicitacao, new IAlterarCadastro() {
     @Override
     public void onResultado(Cliente clienteAtualizado) {
         Log.d("OnResultadoAlterarCadastro", " >>>>>>>> " + clienteAtualizado.getTelefone());
         SharedPreferences salvar =
                 getSharedPreferences("usuario", Context.MODE_PRIVATE);
         SharedPreferences.Editor gravar = salvar.edit();
         gravar.putString("nome", clienteAtualizado.getNome());
         gravar.putString("cpf", clienteAtualizado.getCpf());
         gravar.putString("email", clienteAtualizado.getEmail());
         gravar.putString("telefone", clienteAtualizado.getTelefone());
         gravar.putString("senha", clienteAtualizado.getSenha());
         gravar.putInt("codigo", clienteAtualizado.getCodigo());
         gravar.commit();
         plainPerfilE.setText(clienteAtualizado.getEmail());
         plainPerfilTel.setText(clienteAtualizado.getTelefone());
     }
 });
    }
    public void setTextResidencia(Residencia residenciaSelecionada){
        plainLogradouroPerfil.setText(residenciaSelecionada.getLogradouro());
        plainCEPPerfil.setText(residenciaSelecionada.getCep());
        plainCidadePerfil.setText(residenciaSelecionada.getMunicipio());
        plainBairroPerfil.setText(residenciaSelecionada.getBairro());
        plainNumPerfil.setText("Nº " + residenciaSelecionada.getNumero());
        plainEstadoPerfil.setText(residenciaSelecionada.getUf());
    }
    public void setEnabledResidencia () {
        plainLogradouroPerfil.setEnabled(false);
        plainCEPPerfil.setEnabled(false);
        plainCidadePerfil.setEnabled(false);
        plainBairroPerfil.setEnabled(false);
        plainNumPerfil.setEnabled(false);
        plainEstadoPerfil.setEnabled(false);
    }
    public void buscarResidencias (RequestQueue solicitacao, int idCliente){
        Residencia.listarResidencias(idCliente, solicitacao, new IResidencia() {
            @Override
            public void onResultado(List<Residencia> residencias) {
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
            }
        });
    }


}