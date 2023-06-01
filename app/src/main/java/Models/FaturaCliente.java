package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import Interfaces.IFatura;

public class FaturaCliente {
    private String valorUltimaFatura;
    private String consumoUltimaFatura;

    public FaturaCliente(String valorUltimaFatura, String consumoUltimaFatura) {
        this.valorUltimaFatura = valorUltimaFatura;
        this.consumoUltimaFatura = consumoUltimaFatura;
    }

    public static void BuscarValorConsumoUltimaFatura(int idResidencia, RequestQueue solicitacao, IFatura listener){
        //String url = "http://10.0.2.2:5000/api/Fatura/UltimaFatura/" + idResidencia;
        String url = "http://localhost:5000/api/Fatura/UltimaFatura/" + idResidencia;

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());

                try {
                    JSONObject object = response;
                    FaturaCliente ultimaFaturaCliente = new FaturaCliente(object.getString("ValorUltimaFatura"),object.getString("ConsumoUltimaFatura"));

                    listener.onResultado(ultimaFaturaCliente);

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
