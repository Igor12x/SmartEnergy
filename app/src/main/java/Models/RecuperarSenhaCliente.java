package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;
import Interfaces.IRecuperarSenhaCliente;

public class RecuperarSenhaCliente {

    private String codigoVerificacao;

    public RecuperarSenhaCliente(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public static void ReceberCodigoVerificacao(String email, RequestQueue solicitacao, IRecuperarSenhaCliente listener) {
        //String url = "http://localhost:5000/api/RecuperarSenha" + email;;
        String url = "http://10.0.2.2:5000/api/RecuperarSenha/" + email;

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    RecuperarSenhaCliente codigo = new RecuperarSenhaCliente(response.getString("codigoVericacao"));

                    listener.onResultado(codigo.getCodigoVerificacao());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponseLogin", error.toString());

            }
        });
        solicitacao.add(envio);
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }
}
