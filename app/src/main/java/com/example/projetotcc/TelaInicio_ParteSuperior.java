package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import model.Calculos;
import model.Medidor;

public class TelaInicio_ParteSuperior extends AppCompatActivity {
    public DecimalFormat df = new DecimalFormat("0.00");
    public double consumoAtual = 0, consumoProjetado = 0, valorAtual = 0, valorProjetado = 0; //destinada para mostrar o consumo atual
    private int preenchimento = 0, //destinada para preencher o gráfico em graus
            contatorDias = 0, //criada para contar quantos dias se passaram
            diasRestantes = 0, //quantos dias faltam até a próxima medição/leitura
            max = 10, min = 0, //variavel provisória para ajudar gerar números aleatórios
            limiteProgresso = 0, //limite do preenchimento do gráfico
            limiteConsumo = 0; //limite definido pelo usuário sobre o consumo

    public double tarifaAneelTE = 469.78, tarifaAneelTUSD = 365.99, //tarifas sem imposto
            kWh, //kWh consumidos no relógio (recebido pelo arduino)
            pis = 27, confins = 1.27, icms = 25,
            tarifaTUSD = 0, tarifaTE = 0, //tarifa final com impostos
            totalContaAtual = 0, totalProjecao = 0; //valores finais da conta de energia
    private List<Medidor> medidorList = new ArrayList<>();

    //todos os dados preenchidos diretamente devemos trocar pelos dados do banco

    private TextView textInicioConsumoProjetado, textInicioConsumoAtual, textInicioValorConta, textInicioValorContaProjetado;
    private Calculos calculo = new Calculos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicio_parte_superior);

        textInicioConsumoAtual = findViewById(R.id.textInicioConsumoAtual);
        textInicioConsumoProjetado = findViewById(R.id.textInicioConsumoProjetado);
        textInicioValorConta = findViewById(R.id.textInicioValorConta);
        textInicioValorContaProjetado = findViewById(R.id.textInicioValorContaProjetado);


        //criando uma solicitação para a rede aonde está a API
        RequestQueue solicitacao = Volley.newRequestQueue(this);
        //Iniciondo
        Medidor.buscarConsumoAtual(1, solicitacao, new Medidor.BuscaConsumoListener() {
            @Override
            public void onResultado(double resultado) {
                consumoAtual = Double.parseDouble(df.format(resultado));
                consumoProjetado = calculo.calcularProjecao(consumoAtual, 4,26); //alterar por um select no banco
                tarifaTE = calculo.calcularTarifaImpostos(tarifaAneelTE, pis, confins, icms);
                tarifaTUSD = calculo.calcularTarifaImpostos(tarifaAneelTUSD, pis, confins, icms);
                valorAtual = Double.parseDouble(df.format(calculo.calcularValorConta(tarifaTUSD, tarifaTE, consumoAtual)));
                valorProjetado = calculo.calcularProjecao(valorAtual, 4, 26);

                textInicioValorContaProjetado.setText("R$" + valorProjetado);
                textInicioValorConta.setText("R$" + valorAtual);
                textInicioConsumoAtual.setText(consumoAtual + "kWh");
                textInicioConsumoProjetado.setText(consumoProjetado + "kWh");


            }
        });




    }

}
