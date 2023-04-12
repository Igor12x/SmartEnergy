package model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fatura {
    private double valorUltimaFatura;
    private double consumoUltimaFatura;

    public Fatura(double valorUltimaFatura, double consumoUltimaFatura) {
        this.valorUltimaFatura = valorUltimaFatura;
        this.consumoUltimaFatura = consumoUltimaFatura;
    }

    public double getValorUltimaFatura() {
        return valorUltimaFatura;
    }

    public interface BuscarValorConsumoUltimaFaturaListener {
        void onResultado(Fatura fatura);
    }

    public static void BuscarValorConsumoUltimaFatura(int id, RequestQueue solicitacao, Fatura.BuscarValorConsumoUltimaFaturaListener listener){
        String url = "http://10.0.2.2:5000/api/Fatura/UltimaFatura/1";

        JsonArrayRequest envio = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("onResponse", response.toString());

                try {
                    JSONObject object = response.getJSONObject(0);
                    Fatura ultimaFatura = new Fatura(object.getDouble("valor"),object.getDouble("consumo"));

                    listener.onResultado(ultimaFatura);

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

    public void setValorUltimaFatura(double valorUltimaFatura) {
        this.valorUltimaFatura = valorUltimaFatura;
    }

    public double getConsumoUltimaFatura() {
        return consumoUltimaFatura;
    }

    public void setConsumoUltimaFatura(double consumoUltimaFatura) {
        this.consumoUltimaFatura = consumoUltimaFatura;
    }

}
