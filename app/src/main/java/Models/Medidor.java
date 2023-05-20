package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Interfaces.IMedidorBuscarConsumoDiario;
import Interfaces.IMedidorBuscoConsumoAtual;

public class Medidor {
    public double consumo;
    public Medidor(double consumo) {
        this.consumo = consumo;
    }
    //interface para salvar o resultado da API
    public static void buscarConsumoAtual(int idResidencia, RequestQueue solicitacao, IMedidorBuscoConsumoAtual listener) {
        String url = "http://10.0.2.2:5000/api/Medidor/BuscarConsumo/" + idResidencia;
        //String url = "http://localhost:5000/api/Medidor/BuscarConsumo/" + idResidencia;

        //Criar um objeto da classe Volley para configurar as requisições ao webservice
        //Configurando a requisição a ser enviada

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());

                try {
                    //criando o objeto medidor passando os atributos do objeto JSON no construtor
                    Medidor medidor = new Medidor(response.getDouble("Consumo"));
                    //chamando método onResultado como callback
                    listener.onResultado(medidor.consumo);
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


    public static void buscarConsumoDiario(int idResidencia, RequestQueue solicitacao, IMedidorBuscarConsumoDiario listener) {
        String url = "http://10.0.2.2:5000/api/Medidor/BuscarConsumoDiario/" + idResidencia;

        //Criar um o
        //bjeto da classe Volley para configurar as requisições ao webservice
        //Configurando a requisição a ser enviada

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());

                try {
                    //Pegando do JSON da API o objeto de index 0
                    JSONObject object = response;

                    Medidor medidor = new Medidor(object.getDouble("Consumo"));
                    //chamando método onResultado como callback
                    listener.onResultado(medidor.consumo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponseMedidor", error.toString());
            }
        });
        solicitacao.add(envio);
    }
}
