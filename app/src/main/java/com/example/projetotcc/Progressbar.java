package com.example.projetotcc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Calculos;
import model.Arduino;

public class Progressbar extends AppCompatActivity {
    public double resultado = 0;
    private int preenchimento = 0, //destinada para preencher o gráfico em graus
                contatorDias = 0, //criada para contar quantos dias se passaram
                diasRestantes = 0, //quantos dias faltam até a próxima medição/leitura
                max = 10, min = 0, //variavel provisória para ajudar gerar números aleatórios
                limiteProgresso = 0, //limite do preenchimento do gráfico
                limiteConsumo = 0; //limite definido pelo usuário sobre o consumo

    private double  tarifaAneelTE = 469.78,  tarifaAneelTUSD = 365.99, //tarifas sem imposto
                    kWh = 0, //kWh consumidos no relógio (recebido pelo arduino)
                    pis = 27, confins = 1.27, icms = 25,
                    tarifaTUSD = 0,  tarifaTE = 0, //tarifa final com impostos
                    totalContaAtual = 0, totalProjecao = 0; //valores finais da conta de energia
    private List<Arduino> arduinoList = new ArrayList<>();

    //todos os dados preenchidos diretamente devemos trocar pelos dados do banco


    Random geradorKw = new Random();
    DecimalFormat df = new DecimalFormat("#.##");


    private ProgressBar progressBar;
    private TextView textViewProgress,txtAtual, txtProjecao, txtDiaMes;
    private Calculos calculo = new Calculos();

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
        txtDiaMes.setText(kWh + " Kw/h");
        /*if (preenchimento <= 60)
        {
            preenchimento += 2;
            updateProgressBar();
        }*/
    }

    public void onClickLow(View view)//precisamos trocar pela entrada do banco de dados
    {

        txtDiaMes.setText(kWh + " Kw/h");
        /*if (preenchimento >= 10)
        {
            preenchimento -= 5;
            updateProgressBar();
        }*/
    }

    private void updateProgressBar()
    {

        kWh = consumoHoje();
        contatorDias++; //provisório
        diasRestantes = 10;//aqui devemos calcular os dias restantes para próxima leitura (provisório)


        tarifaTUSD = calculo.calcularTarifaImpostos(tarifaAneelTUSD, pis, confins, icms);
        tarifaTE = calculo.calcularTarifaImpostos(tarifaAneelTE, pis, confins, icms);
        totalContaAtual = calculo.calcularValorConta(tarifaTUSD, tarifaTE, kWh);
        totalProjecao = calculo.calcularProjecao(totalContaAtual, contatorDias, diasRestantes);

        progressBar.setProgress(preenchimento);


        /*textViewProgress.setText(kWh + " Kw/h");
        txtDiaMes.setText("Dia " + contatorDias);
        txtAtual.setText("R$ " + df.format(totalContaAtual));
        txtProjecao.setText("R$ " + df.format(totalProjecao));*/
    }
    public double consumoHoje() {

        //Configuração do endpoint (url) da requisição
        String url = "https://localhost:7202/api/Arduino/1";
        //Criar um objeto da classe Volley para configurar as requisições ao webservice
        RequestQueue solicitacao = Volley.newRequestQueue(this);
        //Configurando a requisição a ser enviada
        JsonArrayRequest envio = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    //Recuperar cada objeto do webservice
                    JSONObject object = response.getJSONObject(0);
                    //atribui cada coluno em uma varíavel apenas para organizar melhor
                    int codigo = object.getInt("codigo");
                    double kWh = object.getDouble("kWh");
                    String registro_dia = object.getString("registro_dia");
                    String registro_horario = object.getString("registro_horario");

                    Arduino a = new Arduino(codigo, kWh, registro_dia, registro_horario);
                    resultado = a.kWh;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        solicitacao.add(envio);
        return resultado;

    }
}