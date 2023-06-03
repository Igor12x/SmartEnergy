package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Interfaces.IResidencia;
import Interfaces.IResidenciaVerificarCadastro;

public class Residencia {
    public int getCodigo() {
        return codigo;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getCep() {
        return cep;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getUf() {
        return uf;
    }

    public String getBairro() {
        return bairro;
    }

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


    public String toString() {
        return logradouro;
    }

    public static void listarResidencias(int idCliente, RequestQueue solicitacao, IResidencia listener) {
        //String url = "http://10.0.2.2:5000/api/Residencia/listarResidencias";
        String url = "http://localhost:5000/api/Residencia/listarResidencias";
        List<Residencia> residencias = new ArrayList<Residencia>();
        JsonArrayRequest envio = new JsonArrayRequest(Request.Method.GET, url + "/" + idCliente, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject object = response.getJSONObject(i);

                        Residencia residencia = new Residencia(object.getInt("Codigo"),
                                                                object.getString("Logradouro"),
                                                                object.getInt("Numero"),
                                                                object.getString("Complemento"),
                                                                object.getString("Cep"),
                                                                object.getString("Municipio"),
                                                                object.getString("Uf"),
                                                                object.getString("Bairro")
                        );
                        residencias.add(residencia);
                        Log.i("listarResdencia", residencia + " oi");
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
    public static void verificarResidenciaCadastrada(String cpf, RequestQueue solicitacao, IResidenciaVerificarCadastro listener){
        //String url = "http://10.0.2.2:5000/api/Residencia/listarResidencias";
        String url = "http://localhost:5000/api/Residencia/verificarResidenciaCadastrada/" + cpf;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse -verificarResidenciaCadastrada", ">>>>>>> " + response );
                boolean possuiResidencia = Boolean.parseBoolean(response);
                listener.onResultado(possuiResidencia);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse-verificarResidenciaCadastrada", ">>>>>>>>>>>>" + error);
            }
        });
        solicitacao.add(stringRequest);
    }
}
