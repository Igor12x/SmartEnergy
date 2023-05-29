package com.example.projetotcc;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
    private Calendar calendar = Calendar.getInstance();
    private ImageView imageView18;
    private Date date = calendar.getTime();
    private double consumoAtual = 0, consumoProjetado = 0, valorAtual = 0, valorProjetado = 0;
    private int limiteConsumo = 200,
            diaFechamentoFatura = 1;
    private TextView textInicioConsumoProjetado, textInicioConsumoAtual,
            textInicioValorConta, textInicioValorContaProjetado,
            txtData, txtMedidorConsumoDiario, textUltimaFatura, textConsumoAtualLimite, textLimite,
            textView2, textAjusteLimite,

            text_view_progress, text_view_progress2;
    private ProgressBar progressConsumoAtual, progressLimiteConsumo;
    private double tarifaTUSD;
    private double tarifaTE;
    private Button btnConfira;
    private Spinner spinnerResidencias;
    private Intent intent;

    private RequestQueue solicitacao = null;
    // Criando uma solicitação para a rede aonde está a API

    //ajuste de limite
    private int valorAjuste = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        solicitacao = Volley.newRequestQueue(this);

        //referencias
        progressConsumoAtual = findViewById(R.id.progress_bar);
        progressLimiteConsumo = findViewById(R.id.progress_bar2);
        textInicioConsumoAtual = findViewById(R.id.textInicioConsumoAtual);
        textInicioConsumoProjetado = findViewById(R.id.textInicioConsumoProjetado);
        textInicioValorConta = findViewById(R.id.textInicioValorConta);
        textInicioValorContaProjetado = findViewById(R.id.textInicioValorContaProjetado);
        txtMedidorConsumoDiario = findViewById(R.id.txtMedidorConsumoDiario);
        textUltimaFatura = findViewById(R.id.textUltimaFatura);
        textConsumoAtualLimite = findViewById(R.id.textConsumoAtualLimite);
        btnConfira = findViewById(R.id.btnConfira);
        imageView18 = findViewById(R.id.imageView18);
        text_view_progress = findViewById(R.id.text_view_progress);
        text_view_progress2 = findViewById(R.id.text_view_progress2);
        textAjusteLimite = findViewById(R.id.textAjusteLimite);

        spinnerResidencias = findViewById(R.id.spinnerEndereco);

        textLimite = findViewById(R.id.textLimite);
        textLimite.setText(limiteConsumo + " kWh");



        textView2 = findViewById(R.id.textView2);
        SharedPreferences ler = getSharedPreferences("usuario", MODE_PRIVATE);
        textView2.setText("Ola " + ler.getString("nome", "")
        );

        intent = new Intent(getApplicationContext(), Tela_Perfil.class);

        txtData = findViewById(R.id.txtData);
        ExibirDataAtual(txtData);


        int idCliente = ler.getInt("codigo", 0);

        imageView18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        //evento do text de limite
        textAjusteLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Tela_Principal.this, R.style.AlertDialogTheme);
                View view1 = LayoutInflater.from(Tela_Principal.this).inflate(
                        R.layout.layout_dialog_inicio, (ConstraintLayout) findViewById(R.id.layoutDialogInicio)
                );

                builder.setView(view1);
                final AlertDialog dialog = builder.create();

                final TextView txtAjuste = view1.findViewById(R.id.txtAjuste);
                final SeekBar sliderAjuste = view1.findViewById(R.id.sliderAjuste);
                Button btnAjustar = view1.findViewById(R.id.btnAjustar);
                TextView txtValorLimite = view1.findViewById(R.id.txtValorLimite);

                if (valorAjuste == 0){
                    valorAjuste = 1200;
                }

                //não mexe, é sério
                txtValorLimite.setText("" + valorAjuste);
                sliderAjuste.setMax(Integer.parseInt(txtValorLimite.getText().toString()));
                sliderAjuste.setProgress(valorAjuste);
                txtAjuste.setText("" + valorAjuste);

                sliderAjuste.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                        txtAjuste.setText("" + (int) progress);
                        // Define a posição da barra de progresso com o valor selecionado
                        sliderAjuste.setProgress(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                btnAjustar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Faça a validação do valor do ajuste
                        if (valorAjuste >= 0) {

                            valorAjuste = Integer.parseInt(txtAjuste.getText().toString());
                            sliderAjuste.setMax(Integer.parseInt(txtAjuste.getText().toString()));
                            //Valor válido
                            Toast.makeText(Tela_Principal.this, "Limite ajustado para: R$" +
                                    valorAjuste, Toast.LENGTH_SHORT).show();
                            dialog.dismiss(); // Fechar o diálogo
                        } else {
                            // Valor inválido
                            Toast.makeText(Tela_Principal.this, "Valor inválido", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }

        });


        Residencia.listarResidencias(idCliente, solicitacao, new IResidencia() {
            @Override
            public void onResultado(List<Residencia> residencias) {
                ResidenciaAdapter adaptador = new ResidenciaAdapter(getApplicationContext(), residencias);
                spinnerResidencias.setAdapter(adaptador);

                spinnerResidencias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Residencia residenciaSelecionada = (Residencia) parent.getSelectedItem();
                        // usar residenciaSelecionada para buscar informações adicionais
                        buscarTarifas(solicitacao, residenciaSelecionada.getCodigo());
                        buscarConsumoAtual(solicitacao, residenciaSelecionada.getCodigo());
                        buscarConsumoDiario(solicitacao, residenciaSelecionada.getCodigo());
                        buscarUltimaFatura(solicitacao, residenciaSelecionada.getCodigo());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });


        btnConfira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(Tela_Principal.this, Tela_Dicas.class);
                startActivity(t);
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

    public void buscarConsumoAtual(RequestQueue solicitacao, int idResidencia) {
        Medidor.buscarConsumoAtual(idResidencia, solicitacao, new IMedidorBuscoConsumoAtual() {
            @Override
            public void onResultado(double consumoAtualResultado) {
                consumoAtual = formatarDouble(consumoAtualResultado);
                textConsumoAtualLimite.setText(consumoAtual + " kWh");
                valorAtual = formatarDouble(FaturaCliente.calcularValorFaturaAtual(tarifaTUSD, tarifaTE,
                        consumoAtual));
                ExibirValorConsumoFaturaAtual(consumoAtual, valorAtual);
                ExibirValorConsumoFaturaProjetada(consumoAtual);

                //definindo a porcentagem que será preenchida pelo gráfico
                double grausGraficoConsumoAtual = (consumoAtual / consumoProjetado) * 100;
                double grausGraficoLimiteConsumo = (consumoAtual / limiteConsumo) * 100;
                text_view_progress.setText((int)grausGraficoConsumoAtual + "%");
                text_view_progress2.setText((int)grausGraficoLimiteConsumo + "%");
                progressConsumoAtual.setProgress((int) grausGraficoConsumoAtual);
                progressLimiteConsumo.setProgress((int) grausGraficoLimiteConsumo);
            }
        });
    }

    public void buscarConsumoDiario(RequestQueue solicitacao, int idResidencia) {
        Medidor.buscarConsumoDiario(idResidencia, solicitacao, new IMedidorBuscarConsumoDiario() {
            @Override
            public void onResultado(double consumoDiarioResultado) {
                txtMedidorConsumoDiario.setText(consumoDiarioResultado + " kWh");
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

    ;


}
