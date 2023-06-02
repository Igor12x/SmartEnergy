package com.example.projetotcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Interfaces.ICompanhiaEletrica;
import Interfaces.IFatura;
import Interfaces.IMedidorBuscarConsumoDiario;
import Interfaces.IMedidorBuscoConsumoAtual;
import Interfaces.IResidencia;
import Models.CompanhiaEnergiaEletrica;
import Models.FaturaCliente;
import Models.Medidor;
import Models.Residencia;
import Models.ResidenciaAdapter;


public class Tela_Principal extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Calendar calendar = Calendar.getInstance();
    private ImageView imgPerfil;
    private Date date = calendar.getTime();
    private double consumoAtual = 0, consumoProjetado = 0, valorAtual = 0, valorProjetado = 0, tarifaTUSD, tarifaTE;
    private int valorAjuste,  diaFechamentoFatura = 1, idCliente, codigoResidencia;
    private TextView textInicioConsumoProjetado, textInicioConsumoAtual,
            textInicioValorConta, textInicioValorContaProjetado, txtData, textMedidorConsumoDiario, textUltimaFatura, textConsumoAtualLimite,
            textValorLimite, textSaudacao, AjusteLimite, textProgressBarPorcentagem, textProgressBarPorcentagemLimite, txtAjuste;
    private ProgressBar progressConsumoAtual, progressLimiteConsumo;
    private Button btnConfira, btnAjustar;
    private Spinner spinnerResidencia;
    private Intent intent;
    private SeekBar sliderAjuste;

    private AlertDialog dialog;
    private SharedPreferences ler;
    private String nomeCompleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        inicializarViews();
        RequestQueue solicitacao = Volley.newRequestQueue(this);
        AutenticarFirebase();
        carregarDadosCliente();
        ExibirDataAtual(txtData);
        btnTelaPerfil();
        btnLabelOla();
        ajustarLimite();
        btnConfira(getApplicationContext());
        listarResidencias(idCliente, solicitacao);
    }
    public void btnLabelOla() {
        textSaudacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), Tela_Perfil.class);
                startActivity(intent1);
            }
        });
    }
    private void carregarDadosCliente() {
        try {
            ler = getSharedPreferences("usuario", MODE_PRIVATE);
             idCliente = ler.getInt("codigo", 0);
            nomeCompleto = ler.getString("nome", "") + " " + ler.getString("sobrenome", "");
            valorAjuste = ler.getInt("limite" + codigoResidencia, 0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Tela_Principal.this, "Erro ao carregar seus dados de perfil. Por favor, tente logar novamente ou entre em contato com o suporte.", Toast.LENGTH_SHORT).show();
        }
        textSaudacao.setText("Olá, " + nomeCompleto + "!");
        textValorLimite.setText(valorAjuste + " kWh");
    }

    private void btnTelaPerfil() {
        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Tela_Perfil.class);
                startActivity(intent);
            }
        });
    }

    private void ajustarLimite() {
        AjusteLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Tela_Principal.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Tela_Principal.this).inflate(
                        R.layout.layout_dialog_inicio, (ConstraintLayout) findViewById(R.id.layoutDialogInicio)
                );

                builder.setView(view1);
                dialog = builder.create();

                txtAjuste = view1.findViewById(R.id.txtAjuste);
                sliderAjuste = view1.findViewById(R.id.sliderAjuste);
                btnAjustar = view1.findViewById(R.id.btnAjustar);
                TextView txtValorLimite = view1.findViewById(R.id.txtValorLimite);

                txtValorLimite.setText("" + valorAjuste);
                sliderAjuste.setMax(Integer.parseInt(txtValorLimite.getText().toString()));
                sliderAjuste.setProgress(valorAjuste);
                txtAjuste.setText("" + valorAjuste);

                sliderAjuste.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        txtAjuste.setText("" + (int) progress);
                        sliderAjuste.setProgress(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                btnAjustar();
                dialog.show();
            }

        });
    }

    private void btnAjustar() {
        btnAjustar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valorAjuste >= 0) {
                    valorAjuste = Integer.parseInt(txtAjuste.getText().toString());
                    sliderAjuste.setMax(Integer.parseInt(txtAjuste.getText().toString()));
                    SharedPreferences.Editor gravar =
                            getSharedPreferences("usuario", MODE_PRIVATE).edit();
                    gravar.putInt("limite" + codigoResidencia, valorAjuste);
                    gravar.commit();
                    porcentagemGrafico();
                    textValorLimite.setText("Limite: R$ " + valorAjuste );
                    Toast.makeText(Tela_Principal.this, "Limite ajustado para: R$" +
                            valorAjuste, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(Tela_Principal.this, "Valor inválido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void listarResidencias(int idCliente, RequestQueue solicitacao) {
        Residencia.listarResidencias(idCliente, solicitacao, new IResidencia() {
            @Override
            public void onResultado(List<Residencia> residencias) {
                ResidenciaAdapter adaptador = new ResidenciaAdapter(getApplicationContext(), residencias);
                spinnerResidencia.setAdapter(adaptador);

                spinnerResidencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Residencia residenciaSelecionada = (Residencia) parent.getSelectedItem();
                        codigoResidencia = residenciaSelecionada.getCodigo();
                        buscarTarifas(solicitacao, codigoResidencia);
                        buscarUltimaFatura(solicitacao, codigoResidencia);
                        mostrarConsumoMesAtual(codigoResidencia);
                        mostrarConsumoDiario(codigoResidencia);
                        carregarDadosCliente();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }

    private void btnConfira(Context context) {
            btnConfira.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dicas = new Intent(Tela_Principal.this, Tela_Dicas.class);
                    startActivity(dicas);
                }
            });

        }



    public void buscarTarifas(RequestQueue solicitacao, int idResidencia) {
        CompanhiaEnergiaEletrica.BuscarTarifas(idResidencia, solicitacao, new ICompanhiaEletrica() {
            @Override
            public void onResultado(CompanhiaEnergiaEletrica tarifasComImposto) {
                try {
                    tarifaTUSD = tarifasComImposto.getTarifaTEComImposto();
                    tarifaTE = tarifasComImposto.getTarifaTUSDComImposto();
                } catch (Exception e) {
                    tarifaTUSD = 0;
                    tarifaTE = 0;
                }
            }
        });
    }
    public void mostrarConsumoMesAtual(int idResidencia) {
        Medidor.buscarConsumoMesAtual(database, idResidencia, Tela_Principal.this, new IMedidorBuscoConsumoAtual() {
            @Override
            public void onResultado(double consumoAtualResultado) {
                Log.d("consumoAtualResultado", ">>>>>>>>>>" + consumoAtualResultado);
                consumoAtual = formatarDouble(consumoAtualResultado);
                valorAtual = formatarDouble(FaturaCliente.calcularValorFaturaAtual(tarifaTUSD, tarifaTE,
                        consumoAtual));
                ExibirValorConsumoFaturaAtual(consumoAtual, valorAtual);
                ExibirValorConsumoFaturaProjetada(consumoAtual);

                porcentagemGrafico();
            }
        });
            }
    public void mostrarConsumoDiario(int idResidencia) {
            Medidor.buscarConsumoDiario(database, idResidencia, Tela_Principal.this, new IMedidorBuscarConsumoDiario() {
                @Override
                public void onResultado(double consumoDiarioResultado) {
                    textMedidorConsumoDiario.setText(consumoDiarioResultado + " kWh");
                }
            });
            }

    public void buscarUltimaFatura(RequestQueue solicitacao, int idResidencia) {
        FaturaCliente.BuscarValorConsumoUltimaFatura(idResidencia, solicitacao, new IFatura() {
            @Override
            public void onResultado(FaturaCliente fatura) {
                textUltimaFatura.setText("O valor da ultima: " + " R$" + fatura.getValorUltimaFatura() +
                        " com consumo: " + fatura.getConsumoUltimaFatura() + " kWh");
            }
        });
    }

    public double formatarDouble(double valor) {

        DecimalFormat df = new DecimalFormat("0.00");
        String converter = df.format(valor);
        converter = converter.replace(",", ".");
        valor = Double.parseDouble(converter);
        return valor;
    }

    ;

    public void ExibirValorConsumoFaturaAtual(double consumoAtual, double valorAtual) {

        textInicioConsumoAtual.setText(consumoAtual + " kWh");
        textInicioValorConta.setText("R$ " + valorAtual);
        textConsumoAtualLimite.setText("R$ " + valorAtual);
    }

    public void ExibirValorConsumoFaturaProjetada(double consumoAtual) {

        consumoProjetado = formatarDouble(FaturaCliente.calcularProjecaoFatura(consumoAtual,
                FaturaCliente.contadorDias(date, diaFechamentoFatura),
                FaturaCliente.calculardDiasRestantes(date, diaFechamentoFatura)));

        valorProjetado = formatarDouble(FaturaCliente.calcularProjecaoFatura(valorAtual,
                FaturaCliente.contadorDias(date, diaFechamentoFatura),
                FaturaCliente.calculardDiasRestantes(date, diaFechamentoFatura)));

        textInicioValorContaProjetado.setText("R$ " + valorProjetado);
        textInicioConsumoProjetado.setText(consumoProjetado + " kWh");
    }

    public void ExibirDataAtual(TextView textViewData) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd 'de' MMM 'de' yyyy",
                new Locale("pt", "BR"));
        String dataAtual = dateFormat.format(date);
        textViewData.setText(dataAtual);
    }
    public void inicializarViews(){
        progressConsumoAtual = findViewById(R.id.progress_bar);
        progressLimiteConsumo = findViewById(R.id.progress_barLimite);
        textInicioConsumoAtual = findViewById(R.id.textInicioConsumoAtual);
        textInicioConsumoProjetado = findViewById(R.id.textInicioConsumoProjetado);
        textInicioValorConta = findViewById(R.id.textInicioValorConta);
        textInicioValorContaProjetado = findViewById(R.id.textInicioValorContaProjetado);
        textMedidorConsumoDiario = findViewById(R.id.textMedidorConsumoDiario);
        textUltimaFatura = findViewById(R.id.textUltimaFatura);
        textConsumoAtualLimite = findViewById(R.id.textConsumoAtualLimite);
        btnConfira = findViewById(R.id.btnConfira);
        imgPerfil = findViewById(R.id.imgPerfil);
        textProgressBarPorcentagem = findViewById(R.id.textProgressBarPorcentagem);
        textProgressBarPorcentagemLimite = findViewById(R.id.textProgressBarPorcentagemLimite);
        AjusteLimite = findViewById(R.id.textAjusteLimite);
        textValorLimite = findViewById(R.id.textValorLimite);
        textSaudacao = findViewById(R.id.textSaudacao);
        txtData = findViewById(R.id.txtData);
        spinnerResidencia = findViewById(R.id.spinnerResidencia);
        textSaudacao = findViewById(R.id.textSaudacao);
    }
    public void porcentagemGrafico() {
        double grausGraficoConsumoAtual = (consumoAtual / consumoProjetado) * 100;
        double grausGraficoLimiteConsumo = (valorAtual / valorAjuste) * 100;
        textProgressBarPorcentagem.setText((int)grausGraficoConsumoAtual + "%");
        textProgressBarPorcentagemLimite.setText(0 + "%");
        progressConsumoAtual.setProgress((int) grausGraficoConsumoAtual);
        if (valorAjuste != 0) {
            progressLimiteConsumo.setProgress((int) grausGraficoLimiteConsumo);
            textProgressBarPorcentagemLimite.setText((int) grausGraficoLimiteConsumo + "%");
        }
    }
    public void AutenticarFirebase() {

        String userEmail = "igorbtenorioidi@gmail.com";
        String userPassword = "123456789";

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("Authentication", "Autenticado com sucesso");
                        } else {
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e("Authentication", "Erro de autenticação: " + exception.getMessage());
                            }
                        }
                    }
                });
    }


}
