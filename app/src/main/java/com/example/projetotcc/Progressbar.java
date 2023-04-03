package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Calculos;
import model.Medidor;

public class Progressbar extends AppCompatActivity {
    public double consumo = 0;
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

    private TextView txtDiaMes;
    private Calculos calculo = new Calculos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progressbar);

        txtDiaMes = findViewById(R.id.txtDiaMes);

        //criando uma solicitação para a rede aonde está a API
        RequestQueue solicitacao = Volley.newRequestQueue(this);
        //Iniciondo
        Medidor.buscarConsumoAtual(1, solicitacao, new Medidor.BuscaConsumoListener() {
            @Override
            public void onResultado(double resultado) {
                consumo = resultado;
            }
        });
    }
    public void onClick(View view)//precisamos trocar pela entrada do banco de dados
    {
        txtDiaMes.setText(consumo + "kWh");
    }
}