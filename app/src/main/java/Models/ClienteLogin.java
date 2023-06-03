package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Interfaces.ILoginCliente;

public class ClienteLogin {
    private String cpf;
    private String senha;

    public ClienteLogin(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    public static void ValidarLoginCliente(ClienteLogin login, RequestQueue solicitacao, ILoginCliente listener) {
        String url = "http://localhost:5000/api/Login";
        //String url = "http://10.0.2.2:5000/api/Login";

        JSONObject enviarCliente = new JSONObject();

        try {
            enviarCliente.put("cpf", login.getCpf());
            enviarCliente.put("senha", login.getSenha());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.POST, url, enviarCliente, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                  try {

                      Cliente clienteLogado = new Cliente(response.getString("Nome"), response.getString("Sobrenome"), response.getString("Cpf"), response.getString("Email"), response.getString("Telefone"), response.getString("Senha"), response.getInt("Codigo"));

                      listener.onResultado(clienteLogado);

                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            listener.onErro(ErroApi.mensagemErroLogin(error));
            }
        });
        solicitacao.add(envio);
    }
    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }
}
