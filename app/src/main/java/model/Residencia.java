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

import java.util.ArrayList;
import java.util.List;

public class Residencia {
    private int codigo;
    private String logradouro;
    private int numero;
    private String complemento;
    private String cep;
    private String municipio;
    private String uf;
    private String bairro;

    public Residencia(int codigo, String logradouro, int numero, String complemento, String cep, String municipio, String uf, String bairro) {
        this.codigo = codigo;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
        this.municipio = municipio;
        this.uf = uf;
        this.bairro = bairro;
    }

    public interface ListarResidenciaListener{
        void onResultado(List<Residencia> residencias);
    }

    public static void listarResidencias(int idCliente, RequestQueue solicitacao, ListarResidenciaListener listener) {
        String url = "http://10.0.2.2:5000/api/Residencia/buscar";
        List<Residencia> residencias = new ArrayList<Residencia>();
        JsonArrayRequest envio = new JsonArrayRequest(Request.Method.GET, url + "/" + idCliente, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject object = response.getJSONObject(i);

                        Residencia residencia = new Residencia(object.getInt("codigo"),
                                                                object.getString("logradouro"),
                                                                object.getInt("numero"),
                                                                object.getString("complemento"),
                                                                object.getString("cep"),
                                                                object.getString("municipio"),
                                                                object.getString("uf"),
                                                                object.getString("bairro")
                        );
                        residencias.add(residencia);
                    }
                    listener.onResultado(residencias);
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
