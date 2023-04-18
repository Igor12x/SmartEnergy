package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ProgressBar;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Models.Calculos;
import Models.CompanhiaEletrica;
import Models.Fatura;
import Models.Medidor;

public class Tela_Principal extends AppCompatActivity {
    private Calendar calendar = Calendar.getInstance();
    private Date date = calendar.getTime();
    private double consumoAtual = 0, consumoProjetado = 0, valorAtual = 0, valorProjetado = 0; //destinada para mostrar o consumo atual
    private int limiteConsumo = 50,
            diaFechamentoFatura = 1; //limite definido pelo usuário sobre o consumo (alterar para shared preferrens)

    private TextView textInicioConsumoProjetado, textInicioConsumoAtual,
            textInicioValorConta, textInicioValorContaProjetado, txtConsumoDiario,
            txtData, txtMedidorConsumoDiario, textUltimaFatura;
    private Calculos calculo = new Calculos();

    private ProgressBar progressConsumoAtual, progressLimiteConsumo;

    private double tarifaTUSD;
    private double tarifaTE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio_parte_superior);

        //referenciando aos componentes
        progressConsumoAtual = findViewById(R.id.progress_bar);
        progressLimiteConsumo = findViewById(R.id.progress_bar2);

        textInicioConsumoAtual = findViewById(R.id.textInicioConsumoAtual);
        textInicioConsumoProjetado = findViewById(R.id.textInicioConsumoProjetado);
        textInicioValorConta = findViewById(R.id.textInicioValorConta);
        textInicioValorContaProjetado = findViewById(R.id.textInicioValorContaProjetado);
        txtMedidorConsumoDiario = findViewById(R.id.txtMedidorConsumoDiario);
        txtData = findViewById(R.id.txtData);
        txtConsumoDiario = findViewById(R.id.textInicioValorConta);

        // Criando uma solicitação para a rede aonde está a API
        RequestQueue solicitacao = Volley.newRequestQueue(this);

        Medidor.buscarConsumoAtual(59, solicitacao, new Medidor.BuscaConsumoListener() {
            @Override
            public void onResultado(double resultado) {

                // formatando o número antes de atribuir
                consumoAtual = calculo.formatarDouble(resultado);

                //transformando o consumo no valor cobrado pela distribuidora
                //valorAtual = calculo.formatarDouble(calculo.calcularValorConta(tarifaTUSD, tarifaTE, consumoAtual));

                //gerando data de hoje e formatando
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd 'de' MMM 'de' yyyy", new Locale("pt", "BR"));
                String dataAtual = dateFormat.format(date);
                txtData.setText(dataAtual);

                //projeção do valor final da conta
                consumoProjetado = calculo.formatarDouble(calculo.calcularProjecao(consumoAtual,
                        calculo.contadorDias(date, diaFechamentoFatura),
                        calculo.calculardDiasRestantes(date, diaFechamentoFatura)));

                valorProjetado =  calculo.formatarDouble(calculo.calcularProjecao(valorAtual,
                        calculo.contadorDias(date, diaFechamentoFatura),
                        calculo.calculardDiasRestantes(date, diaFechamentoFatura)));

                //mostrando na tela, em valores:
                textInicioValorContaProjetado.setText("R$" + valorProjetado);
                textInicioValorConta.setText("R$" + valorAtual);
                //em KWh
                textInicioConsumoAtual.setText(consumoAtual + "kWh");
                textInicioConsumoProjetado.setText(consumoProjetado + "kWh");

                //definindo a porcentagem que será preenchida pelo gráfico
                double grausGraficoConsumoAtual = (consumoAtual/consumoProjetado) * 100;
                double grausGraficoLimiteConsumo = (consumoAtual/limiteConsumo) * 100;
                progressConsumoAtual.setProgress((int)grausGraficoConsumoAtual);
                progressLimiteConsumo.setProgress((int)grausGraficoLimiteConsumo);
            }
        });

        Medidor.buscarConsumoDiario(1, solicitacao, new Medidor.BuscaConsumoDiarioListener() {
            @Override
            public void onResultado(String resultado) {
                txtMedidorConsumoDiario.setText(resultado);
            }
        });

        Fatura.BuscarValorConsumoUltimaFatura(1, solicitacao, new Fatura.BuscarValorConsumoUltimaFaturaListener() {
            @Override
            public void onResultado(Fatura fatura) {
                textUltimaFatura.setText("O valor da ultima: " + fatura.getValorUltimaFatura() + " com consumo: " + fatura.getConsumoUltimaFatura());
            }
        });

        CompanhiaEletrica.BuscarTarifas(1, solicitacao, new CompanhiaEletrica.BuscarTarifasListener() {
            @Override
            public void onResultado(CompanhiaEletrica tarifasComImpostoCompanhia) {
                tarifaTUSD = tarifasComImpostoCompanhia.getTarifaTEComImposto();
                tarifaTE = tarifasComImpostoCompanhia.getTarifaTUSDComImposto();
            }
        });
    }
}
