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

        Fatura.BuscarValorConsumoUltimaFatura(1, solicitacao, new Fatura.BuscarValorConsumoUltimaFaturaListener() {
            @Override
            public void onResultado(Fatura fatura) {
                textUltimaFatura.setText("O valor da ultima: " + fatura.getValorUltimaFatura() + " com consumo: " + fatura.getConsumoUltimaFatura());
            }
        });
    }
}
