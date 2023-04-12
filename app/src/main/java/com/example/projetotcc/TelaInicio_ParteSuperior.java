package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.os.Bundle;
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
import model.Fatura;
import model.Medidor;

public class TelaInicio_ParteSuperior extends AppCompatActivity {
    public DecimalFormat df = new DecimalFormat("0.00"); // objetado destinado para formatar numeros decimais
    public double consumoAtual = 0, consumoProjetado = 0, valorAtual = 0, valorProjetado = 0; //destinada para mostrar o consumo atual
    private int preenchimento = 0, //destinada para preencher o gráfico em graus
            contatorDias = 0, //criada para contar quantos dias se passaram
            diasRestantes = 0, //quantos dias faltam até a próxima medição/leitura
            max = 10, min = 0, //variavel provisória para ajudar gerar números aleatórios
            limiteProgresso = 0, //limite do preenchimento do gráfico
            limiteConsumo = 50; //limite definido pelo usuário sobre o consumo (alterar para shared preferrens)

    public double tarifaAneelTE = 469.78, tarifaAneelTUSD = 365.99, //tarifas sem imposto
            kWh, //kWh consumidos no relógio (recebido pelo arduino)
            pis = 27, confins = 1.27, icms = 25,
            tarifaTUSD = 0, tarifaTE = 0, //tarifa final com impostos
            totalContaAtual = 0, totalProjecao = 0; //valores finais da conta de energia

    public List<Medidor> medidorList = new ArrayList<>();

    //todos os dados preenchidos diretamente devemos trocar pelos dados do banco

    public TextView textInicioConsumoProjetado, textInicioConsumoAtual,
            textInicioValorConta, textInicioValorContaProjetado, txtConsumoDiario, txtData, textUltimaFatura;
    public Calculos calculo = new Calculos();
    public ProgressBar progressConsumoAtual, progressLimiteConsumo;

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
        textUltimaFatura = findViewById(R.id.textUltimaFatura);
        txtData = findViewById(R.id.txtData);

        txtConsumoDiario = findViewById(R.id.textInicioValorConta);

        //criando uma solicitação para a rede aonde está a API
        RequestQueue solicitacao = Volley.newRequestQueue(this);

        //enviando solicitação para a API e utlizando uma interface (callback) para trazer o valor do consumo atual
        /*Medidor.buscarConsumoAtual(1, solicitacao, new Medidor.BuscaConsumoListener() {
            @Override
            public void onResultado(double resultado) {

                // formatando o número antes de atribuir
                consumoAtual = Double.parseDouble(df.format(resultado));

                //calculando os valores da tarifa (depois preciso pegar o valores do banco)
                tarifaTE = calculo.calcularTarifaImpostos(tarifaAneelTE, pis, confins, icms);
                tarifaTUSD = calculo.calcularTarifaImpostos(tarifaAneelTUSD, pis, confins, icms);

                //transformando o consumo no valor cobrado pela distribuidora
                valorAtual = Double.parseDouble(df.format(calculo.calcularValorConta(tarifaTUSD, tarifaTE, consumoAtual)));

                //projeção do valor final da conta
                consumoProjetado = Double.parseDouble(df.format(calculo.calcularProjecao(consumoAtual, 6,24))); //alterar por um select no banco
                valorProjetado = Double.parseDouble(df.format(calculo.calcularProjecao(valorAtual, 6, 24))); //alterar por um select no banco

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

                //gerando data de hoje e formatando
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd 'de' MMM 'de' yyyy", new Locale("pt", "BR"));
                Date date = calendar.getTime();
                String dataAtual = dateFormat.format(date);
                txtData.setText(dataAtual);

            }
        });
        //aqui estou enviando uma solicitação (GET) para a API, que me devolve o quanto em kWh
        // foi no consumido no dia de hoje
        Medidor.buscarConsumoDiario(1, solicitacao, new Medidor.BuscaConsumoDiarioListener() {
            @Override
            public void onResultado(double resultado) {
                txtConsumoDiario.setText(resultado + "kWh");
            }
        });*/

        Fatura.BuscarValorConsumoUltimaFatura(1, solicitacao, new Fatura.BuscarValorConsumoUltimaFaturaListener() {
            @Override
            public void onResultado(Fatura fatura) {
                textUltimaFatura.setText("O valor da ultima: " + fatura.getValorUltimaFatura() + " com consumo: " + fatura.getConsumoUltimaFatura());
            }
        });
    }
}
