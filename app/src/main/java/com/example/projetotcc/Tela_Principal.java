package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ProgressBar;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
            textInicioValorConta, textInicioValorContaProjetado,
            txtData, txtMedidorConsumoDiario, textUltimaFatura;

    private ProgressBar progressConsumoAtual, progressLimiteConsumo;

    private double tarifaTUSD;
    private double tarifaTE;

    // Criando uma solicitação para a rede aonde está a API
    public RequestQueue solicitacao = Volley.newRequestQueue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio_parte_superior);

        // Referenciando aos componentes
        txtData = findViewById(R.id.txtData);
        progressConsumoAtual = findViewById(R.id.progress_bar);
        progressLimiteConsumo = findViewById(R.id.progress_bar2);
        textInicioConsumoAtual = findViewById(R.id.textInicioConsumoAtual);
        textInicioConsumoProjetado = findViewById(R.id.textInicioConsumoProjetado);
        textInicioValorConta = findViewById(R.id.textInicioValorConta);
        textInicioValorContaProjetado = findViewById(R.id.textInicioValorContaProjetado);
        txtMedidorConsumoDiario = findViewById(R.id.txtMedidorConsumoDiario);

        ExibirDataAtual();

        Medidor.buscarConsumoAtual(59, solicitacao, new Medidor.BuscaConsumoListener() {
            @Override
            public void onResultado(double resultado) {

                consumoAtual = formatarDouble(resultado);
                CalcularImpostosFatura();
                valorAtual = Fatura.calcularValorFaturaAtual(tarifaTUSD, tarifaTE, consumoAtual);
                ExibirValorConsumoFaturaAtual(consumoAtual, valorAtual);
                ExibirValorConsumoFaturaProjetada(consumoAtual);


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
    }

    public double formatarDouble(double valor) {
        DecimalFormat df = new DecimalFormat("0.00");
        String converter = df.format(valor);
        converter = converter.replace(",", ".");
        valor = Double.parseDouble(converter);
        return valor;
    };

    public void ExibirValorConsumoFaturaAtual(double consumoAtual, double valorAtual) {
        textInicioConsumoAtual.setText(consumoAtual + "kWh");
        textInicioValorConta.setText("R$" + valorAtual);
    }

    public void ExibirValorConsumoFaturaProjetada(double consumoAtual) {
        consumoProjetado = formatarDouble(Fatura.calcularProjecaoFatura(consumoAtual,
                Fatura.contadorDias(date, diaFechamentoFatura),
                Fatura.calculardDiasRestantes(date, diaFechamentoFatura)));

        valorProjetado =  formatarDouble(Fatura.calcularProjecaoFatura(valorAtual,
                Fatura.contadorDias(date, diaFechamentoFatura),
                Fatura.calculardDiasRestantes(date, diaFechamentoFatura)));

        textInicioValorContaProjetado.setText("R$" + valorProjetado);
        textInicioConsumoProjetado.setText(consumoProjetado + "kWh");
    }

    public void CalcularImpostosFatura() {
        CompanhiaEletrica.BuscarTarifas(1, solicitacao, new CompanhiaEletrica.BuscarTarifasListener() {
            @Override
            public void onResultado(CompanhiaEletrica tarifasComImpostoCompanhia) {
                tarifaTUSD = tarifasComImpostoCompanhia.getTarifaTEComImposto();
                tarifaTE = tarifasComImpostoCompanhia.getTarifaTUSDComImposto();
            }
        });
    }

    public void ExibirDataAtual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd 'de' MMM 'de' yyyy", new Locale("pt", "BR"));
        String dataAtual = dateFormat.format(date);
        txtData.setText(dataAtual);
    };
}
