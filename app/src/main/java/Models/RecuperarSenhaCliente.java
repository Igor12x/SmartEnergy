package Models;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Interfaces.ICadastroCliente;
import Interfaces.IRecuperarSenhaCodigoVerificacao;
import Interfaces.IRecuperarSenhaRedefinir;

public class RecuperarSenhaCliente {

    private String codigoVerificacao;

    public RecuperarSenhaCliente(String codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public static void ReceberCodigoVerificacao(String email, RequestQueue solicitacao, Context contexto, IRecuperarSenhaCodigoVerificacao listener) {
        String url = "http://localhost:5000/api/RecuperarSenha/CodigoVerificacao/" + email;

        StringRequest envio = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                RecuperarSenhaCliente codigo = new RecuperarSenhaCliente(response);
                listener.onResultado(codigo.getCodigoVerificacao());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 404) {
                    listener.onError("O recurso solicitado não foi encontrado.");
                } else if (networkResponse != null && networkResponse.statusCode == 500) {
                    listener.onError("O email fornecido não possui cadastro.");
                } else {
                    listener.onError("Ocorreu um erro ao recuperar o código de verificação. Por favor, tente novamente mais tarde.");
                }
            }
        });
        solicitacao.add(envio);
    }

    public static void ValidarCadastroCliente(Cliente redefinirSenhacliente, RequestQueue solicitacao, IRecuperarSenhaRedefinir listener) {
        String url = "http://localhost:5000/api/RecuperarSenha/RedefinirSenha";

        JSONObject enviarCliente = new JSONObject();

        try {
            enviarCliente.put("email", redefinirSenhacliente.getEmail());
            enviarCliente.put("senha", redefinirSenhacliente.getSenha());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.POST, url, enviarCliente, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                listener.onResultado(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onResultado(false);
                Log.i("onErrorResponse", error.toString());

            }
        });
        solicitacao.add(envio);
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }
}
