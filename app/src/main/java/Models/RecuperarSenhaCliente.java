package Models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
        //String url = "http://10.0.2.2:5000/api/RecuperarSenha/CodigoVerificacao/" + email;
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
                listener.onError(ErroApi.mensagemErroRecuperarSenha(error));
            }
        });
        envio.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        solicitacao.add(envio);
    }

    public static void RedefinirSenhaCliente(Cliente redefinirSenhacliente, RequestQueue solicitacao, Context contexto, IRecuperarSenhaRedefinir listener) {
        String url = "http://localhost:5000/api/RecuperarSenha/RedefinirSenha";
        //String url = "http://10.0.2.2:5000/api/RecuperarSenha/RedefinirSenha";

        JSONObject enviarCliente = new JSONObject();

        try {
            enviarCliente.put("email", redefinirSenhacliente.getEmail());
            enviarCliente.put("senha", redefinirSenhacliente.getSenha());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(contexto, "Error ao gerar formato JSON, verifique os campos preenchidos", Toast.LENGTH_SHORT).show();
        }

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.PUT, url, enviarCliente, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listener.onResultado(response.getString("retorno"));
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(ErroApi.mensagemErroRecuperarSenha(error));
            }
        });
        solicitacao.add(envio);
    }

    public String getCodigoVerificacao() {
        return codigoVerificacao;
    }
}
