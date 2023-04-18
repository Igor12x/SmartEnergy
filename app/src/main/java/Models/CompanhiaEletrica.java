package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CompanhiaEletrica {
    private double tarifaTUSD;
    private double tarifaTE;
    private double tarifaTUSDComImposto;
    private double tarifaTEComImposto;
    private double icms;
    private double pis;
    private double cofins;

    public CompanhiaEletrica(double tarifaTUSD, double tarifaTE, double icms, double pis, double cofins) {
        this.tarifaTUSD = tarifaTUSD;
        this.tarifaTE = tarifaTE;
        this.icms = icms;
        this.pis = pis;
        this.cofins = cofins;
    }

    public CompanhiaEletrica(double tarifaTUSDComImposto, double tarifaTEComImposto) {
        this.tarifaTUSDComImposto = tarifaTUSDComImposto;
        this.tarifaTEComImposto = tarifaTEComImposto;
    }

    public interface BuscarTarifasListener {
        void onResultado(CompanhiaEletrica tarifasComImpostoCompanhia);
    }

    public static void BuscarTarifas(int idCompanhia, RequestQueue solicitacao, CompanhiaEletrica.BuscarTarifasListener listener) {
        String url = "http://localhost:5000/api/CompanhiaEletrica/Tarifas/" + idCompanhia;

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());

                try {
                    CompanhiaEletrica tarifasCompanhia = validarJson(response);

                    double tarifaTUSDImposto = CalcularTarifaTUSDComImposto(tarifasCompanhia.getTarifaTUSD(), tarifasCompanhia.getIcms(), tarifasCompanhia.getPis(), tarifasCompanhia.getCofins());
                    double tarifaTEImposto = CalcularTarifaTEComImposto(tarifasCompanhia.getTarifaTE(), tarifasCompanhia.getIcms(), tarifasCompanhia.getPis(), tarifasCompanhia.getCofins());

                    CompanhiaEletrica tarifasComImpostoCompanhia = new CompanhiaEletrica(tarifaTUSDImposto, tarifaTEImposto);

                    listener.onResultado(tarifasComImpostoCompanhia);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onResultado(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponse", error.toString());
                listener.onResultado(null);
            }
        });
        solicitacao.add(envio);
    }

    private static CompanhiaEletrica validarJson(JSONObject response) throws JSONException {
        double tarifaTUSD = response.getDouble("TarifaTusd");
        double tarifaTE = response.getDouble("TarifaTe");
        double icms = response.getDouble("Icms");
        double pis = response.getDouble("Pis");
        double cofins = response.getDouble("Cofins");
        return new CompanhiaEletrica(tarifaTUSD, tarifaTE, icms, pis, cofins);
    }

    private static double CalcularTarifaTUSDComImposto (double tarifaTUSD, double icms, double pis, double cofins){
        double tarifaKWHTUSD = tarifaTUSD/1000;
        double icmsPercentual = icms/100;
        double pisPercentual = pis/100;
        double confinsPercentual = cofins/100;

        return (tarifaKWHTUSD)/(1-(pisPercentual+confinsPercentual+icmsPercentual));
    };

    private static double CalcularTarifaTEComImposto (double tarifaTE, double icms, double pis, double cofins){
        double tarifaKWHTE = tarifaTE/1000;
        double icmsPercentual = icms/100;
        double pisPercentual = pis/100;
        double confinsPercentual = cofins/100;

        return (tarifaKWHTE)/(1-(pisPercentual+confinsPercentual+icmsPercentual));
    };

    public double getTarifaTUSD() {
        return tarifaTUSD;
    }

    public double getTarifaTE() {
        return tarifaTE;
    }

    public double getIcms() {
        return icms;
    }

    public double getPis() {
        return pis;
    }

    public double getCofins() {
        return cofins;
    }

    public double getTarifaTUSDComImposto() {
        return tarifaTUSDComImposto;
    }

    public double getTarifaTEComImposto() {
        return tarifaTEComImposto;
    }


}
