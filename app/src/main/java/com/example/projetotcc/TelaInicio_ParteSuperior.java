package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ProgressBar;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.Calculos;
import model.Medidor;

public class TelaInicio_ParteSuperior extends AppCompatActivity {
    private Calendar calendar = Calendar.getInstance();
    private Date date = calendar.getTime();
    private double consumoAtual = 0, consumoProjetado = 0, valorAtual = 0, valorProjetado = 0; //destinada para mostrar o consumo atual
    private int limiteConsumo = 50,
            diaFechamentoFatura = 1; //limite definido pelo usuário sobre o consumo (alterar para shared preferrens)


    private double tarifaAneelTE = 469.78, tarifaAneelTUSD = 365.99, //tarifas sem imposto
            kWh, //kWh consumidos no relógio (recebido pelo arduino)
            pis = 27, confins = 1.27, icms = 25,
            tarifaTUSD = 0, tarifaTE = 0, //tarifa final com impostos
            totalContaAtual = 0, totalProjecao = 0; //valores finais da conta de energia

    //todos os dados preenchidos diretamente devemos trocar pelos dados do banco

    private TextView textInicioConsumoProjetado, textInicioConsumoAtual,
            textInicioValorConta, textInicioValorContaProjetado, txtConsumoDiario,
            txtData, txtMedidorConsumoDiario;
    private Calculos calculo = new Calculos();

    private ProgressBar progressConsumoAtual, progressLimiteConsumo;

    private Spinner spinnerResidencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio_parte_superior);

        //referenciando aos componentes
        progressConsumoAtual = findViewById(R.id.progress_bar);
        progressLimiteConsumo = findViewById(R.id.progress_bar2);

        spinnerResidencias = findViewById(R.id.spinnerEndereco);


        textInicioConsumoAtual = findViewById(R.id.textInicioConsumoAtual);
        textInicioConsumoProjetado = findViewById(R.id.textInicioConsumoProjetado);
        textInicioValorConta = findViewById(R.id.textInicioValorConta);
        textInicioValorContaProjetado = findViewById(R.id.textInicioValorContaProjetado);
        txtMedidorConsumoDiario = findViewById(R.id.txtMedidorConsumoDiario);
        txtData = findViewById(R.id.txtData);
        txtConsumoDiario = findViewById(R.id.textInicioValorConta);

        //criando uma solicitação para a rede aonde está a API
        RequestQueue solicitacao = Volley.newRequestQueue(this);

        //enviando solicitação para a API e utlizando uma interface (callback) para
        // trazer o valor do consumo atual
        Medidor.buscarConsumoAtual(59 //mudar para uma variavél que recupera valores do sharedPreferences
                , solicitacao, new Medidor.BuscaConsumoListener() {
            @Override
            public void onResultado(double resultado) {

                // formatando o número antes de atribuir
                consumoAtual = calculo.formatarDouble(resultado);

                //calculando os valores da tarifa (depois preciso pegar o valores do banco)
                tarifaTE = calculo.calcularTarifaImpostos(tarifaAneelTE, pis, confins, icms);
                tarifaTUSD = calculo.calcularTarifaImpostos(tarifaAneelTUSD, pis, confins, icms);

                //transformando o consumo no valor cobrado pela distribuidora
                valorAtual = calculo.formatarDouble(calculo.calcularValorConta(tarifaTUSD, tarifaTE, consumoAtual));

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
        //aqui estou enviando uma solicitação (GET) para a API, que me devolve o quanto em kWh
        // foi no consumido no dia de hoje
        Medidor.buscarConsumoDiario(1, solicitacao, new Medidor.BuscaConsumoDiarioListener() {
            @Override
            public void onResultado(String resultado) {
                txtMedidorConsumoDiario.setText(resultado);
            }
        });
    }
}
