package model;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetotcc.Progressbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Medidor extends Progressbar {
    public double consumo;

    public Medidor(double consumo) {
        this.consumo = consumo;
    }

    //interface para salvar o resultado da API
    public interface BuscaConsumoListener {
        void onResultado(Medidor medidor);

    }

    public interface BuscaConsumoDiarioListener {
        void onResultado(Medidor medidor);

    }

    public static void buscarConsumoAtual(int id, RequestQueue solicitacao, BuscaConsumoListener listener) {
        String url = "http://10.0.2.2:5000/api/Medidor/BuscarConsumo";

        //Criar um objeto da classe Volley para configurar as requisições ao webservice
        //Configurando a requisição a ser enviada

        JsonArrayRequest envio = new JsonArrayRequest(Request.Method.GET, url + "/" + id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("onResponse", response.toString());

                try {
                    //Pegando do JSON da API o objeto de index 0
                    JSONObject object = response.getJSONObject(0);
                    //criando o objeto medidor passando os atributos do objeto JSON no construtor
                    Medidor medidor = new Medidor(object.getDouble("consumo"));
                    //chamando método onResultado como callback
                    listener.onResultado(medidor);
                    Log.i("medor", medidor.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponse", error.toString());

            }
        });

        solicitacao.add(envio);

    }


    public static void buscarConsumoDiario(int id, RequestQueue solicitacao, BuscaConsumoDiarioListener listener) {
        String url = "http://10.0.2.2:5000/api/Medidor/BuscarConsumoDiario";

        //Criar um objeto da classe Volley para configurar as requisições ao webservice
        //Configurando a requisição a ser enviada

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());

                try {
                    //Pegando do JSON da API o objeto de index 0
                    JSONObject object = response;

                    //chamando método onResultado como callback
                    Medidor medidor = new Medidor(object.getDouble("consumo"));
                    listener.onResultado(medidor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponse", error.toString());
            }
        });
        solicitacao.add(envio);
    }
}
