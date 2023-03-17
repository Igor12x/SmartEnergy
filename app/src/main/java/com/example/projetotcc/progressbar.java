package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Random;

public class progressbar extends AppCompatActivity {
    private int preenchimento = 0, //destinada para preencher o gráfico em graus
                kWh = 0, //kWh consumidos no relógio (recebido pelo arduino)
                contatorDias = 0, //criada para contar quantos dias se passaram
                diasRestantes = 0, //quantos dias faltam até a próxima medição/leitura
                max = 10, min = 0, //variavel provisória para ajudar gerar números aleatórios
                limiteProgresso = 0, //limite do preenchimento do gráfico
                limiteConsumo = 0; //limite definido pelo usuário sobre o consumo

    private double  tarifaAneelTE = 0,  tarifaAneelTUSD = 0, //tarifas sem imposto
                    pis = 0, confins = 0, icms = 0, //impostos
                    tarifaTUSD = 0,  tarifaTE = 0, //tarifa final com impostos
                    totalContaAtual = 0, totalProjecao = 0; //valores finais da conta de energia


    Random geradorKw = new Random();
    DecimalFormat df = new DecimalFormat("#.##");


    private ProgressBar progressBar;
    private TextView textViewProgress,txtAtual, txtProjecao, txtDiaMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);

        progressBar = findViewById(R.id.progress_bar);
        textViewProgress = findViewById(R.id.text_view_progress);
        txtAtual = findViewById(R.id.txtConsumoAtual);
        txtProjecao = findViewById(R.id.txtPrevisao);
        txtDiaMes = findViewById(R.id.txtDiaMes);

        progressBar.setProgress(0);
        textViewProgress.setText("0%");
    }
    public void onClickUp(View view) //precisamos trocar pela entrada do banco de dados
    {
        if (preenchimento <= 60)
        {
            preenchimento += 2;
            updateProgressBar();
        }
    }

    public void onClickLow(View view)//precisamos trocar pela entrada do banco de dados
    {
        if (preenchimento >= 10)
        {
            preenchimento -= 5;
            updateProgressBar();
        }
    }

    private void updateProgressBar()
    {
        tarifaAneelTE = 469.78/1000;
        tarifaAneelTUSD = 365.99/1000;
        pis = 0.27/100;
        confins = 1.27/100;
        icms = 25/100;
        tarifaTUSD = (tarifaAneelTUSD)/(1-(pis+confins+icms));
        tarifaTE = (tarifaAneelTE)/(1-(pis+confins+icms));
        kWh = kWh + geradorKw.nextInt(max + 1 - min) + min;
        totalContaAtual = (kWh * tarifaTUSD) + (kWh * tarifaTE);


        progressBar.setProgress(preenchimento);
        contatorDias++;

        diasRestantes = 10;//aqui devemos calcular os dias restantes para próxima leitura

        totalProjecao = ((totalContaAtual/contatorDias)*diasRestantes)+totalContaAtual; //criar um método




        textViewProgress.setText(kWh + " Kw/h");
        txtDiaMes.setText("Dia " + contatorDias);
        txtAtual.setText("R$ " + df.format(totalContaAtual));
        txtProjecao.setText("R$ " + df.format(totalProjecao));
    }

}