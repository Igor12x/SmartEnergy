package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Fatura {
    private String valorUltimaFatura;
    private String consumoUltimaFatura;

    public Fatura(String valorUltimaFatura, String consumoUltimaFatura) {
        this.valorUltimaFatura = valorUltimaFatura;
        this.consumoUltimaFatura = consumoUltimaFatura;
    }

    public String getValorUltimaFatura() {
        return valorUltimaFatura;
    }

    public interface BuscarValorConsumoUltimaFaturaListener {
        void onResultado(Fatura fatura);
    }

    public static void BuscarValorConsumoUltimaFatura(int id, RequestQueue solicitacao, Fatura.BuscarValorConsumoUltimaFaturaListener listener){
        String url = "http://localhost:5000/api/Fatura/UltimaFatura/1";

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());

                try {
                    JSONObject object = response;
                    Fatura ultimaFatura = new Fatura(object.getString("ValorUltimaFatura"),object.getString("ConsumoUltimaFatura"));

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

    public String getConsumoUltimaFatura() {
        return consumoUltimaFatura;
    }

}
