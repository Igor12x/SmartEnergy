package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Fatura {
    private String valorUltimaFatura;
    private String consumoUltimaFatura;

    public Fatura(String valorUltimaFatura, String consumoUltimaFatura) {
        this.valorUltimaFatura = valorUltimaFatura;
        this.consumoUltimaFatura = consumoUltimaFatura;
    }

    public interface BuscarValorConsumoUltimaFaturaListener {
        void onResultado(Fatura fatura);
    }

    public static void BuscarValorConsumoUltimaFatura(int id, RequestQueue solicitacao, Fatura.BuscarValorConsumoUltimaFaturaListener listener){
        String url = "http://10.0.2.2:5000/api/Fatura/UltimaFatura/1";

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

    public static double calcularValorFaturaAtual(double tarifaTusd, double tarifaTe, double kmw) {
        return ((kmw * tarifaTusd)+(kmw * tarifaTe));
    };

    public static double calcularProjecaoFatura(double totalContaAtual, int contatorDias, int diasRestantes){
        return ((totalContaAtual/contatorDias)*diasRestantes) + totalContaAtual;
    };

    public static int calculardDiasRestantes(Date hoje, int diaFechamentoFatura) {

        Calendar fechamento = Calendar.getInstance();
        fechamento.set(Calendar.DAY_OF_MONTH, diaFechamentoFatura);
        fechamento.add(Calendar.MONTH, 1);
        Date dataFechamento = fechamento.getTime();

        int diffInMs = Math.round(dataFechamento.getTime() - hoje.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);

        return (int) diffInDays;
    };

    public static int contadorDias(Date hoje, int diaFechamentoFatura) {

        Calendar fechamento = Calendar.getInstance();
        fechamento.set(Calendar.DAY_OF_MONTH, diaFechamentoFatura);
        Date dataFechamento = fechamento.getTime();

        int diferencaMs = Math.round(hoje.getTime() - dataFechamento.getTime());
        long diferencaDias = TimeUnit.DAYS.convert(diferencaMs, TimeUnit.MILLISECONDS);

        return (int) diferencaDias + 1; // +1 por conta que na subtração  em Ms não leva em conta que deve somar o ultimo dia também
    };

    public String getConsumoUltimaFatura() { return consumoUltimaFatura;
    }
    public String getValorUltimaFatura() {
        return valorUltimaFatura;
    }

}
