package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Interfaces.ICompanhiaEletrica;

public class CompanhiaEnergiaEletrica {
    private double tarifaTUSD;
    private double tarifaTE;
    private double tarifaTUSDComImposto;
    private double tarifaTEComImposto;
    private double icms;
    private double pis;
    private double cofins;

    public CompanhiaEnergiaEletrica(double tarifaTUSD, double tarifaTE, double icms, double pis, double cofins) {
        this.tarifaTUSD = tarifaTUSD;
        this.tarifaTE = tarifaTE;
        this.icms = icms;
        this.pis = pis;
        this.cofins = cofins;
    }

    public CompanhiaEnergiaEletrica(double tarifaTUSDComImposto, double tarifaTEComImposto) {
        this.tarifaTUSDComImposto = tarifaTUSDComImposto;
        this.tarifaTEComImposto = tarifaTEComImposto;
    }

    public static void BuscarTarifas(int idResidencia, RequestQueue solicitacao, ICompanhiaEletrica listener) {
        //String url = "http://10.0.2.2:5000/api/CompanhiaEletrica/buscarCompanhia/" + idResidencia;
        String url = "http://localhost:5000/api/CompanhiaEletrica/buscarCompanhia/" + 1;

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());

                try {
                    CompanhiaEnergiaEletrica tarifasCompanhia = validarJson(response);

                    double tarifaTUSDImposto = CalcularTarifaComImposto(tarifasCompanhia.getTarifaTUSD(), tarifasCompanhia.getIcms(), tarifasCompanhia.getPis(), tarifasCompanhia.getCofins());
                    double tarifaTEImposto = CalcularTarifaComImposto(tarifasCompanhia.getTarifaTE(), tarifasCompanhia.getIcms(), tarifasCompanhia.getPis(), tarifasCompanhia.getCofins());

                    CompanhiaEnergiaEletrica tarifasComImposto = new CompanhiaEnergiaEletrica(tarifaTUSDImposto, tarifaTEImposto);

                    listener.onResultado(tarifasComImposto);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponseCompanhia", error.toString());
                listener.onResultado(null);
            }
        });
        solicitacao.add(envio);
    }

    private static CompanhiaEnergiaEletrica validarJson(JSONObject response) throws JSONException {
        double tarifaTUSD = response.getDouble("TarifaTUSD");
        double tarifaTE = response.getDouble("TarifaTE");
        double icms = response.getDouble("Icms");
        double pis = response.getDouble("Pis");
        double cofins = response.getDouble("Cofins");
        return new CompanhiaEnergiaEletrica(tarifaTUSD, tarifaTE, icms, pis, cofins);
    }

    private static double CalcularTarifaComImposto (double tarifa, double icms, double pis, double cofins){


        return (tarifa)/(1-(pis+cofins+icms));
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
