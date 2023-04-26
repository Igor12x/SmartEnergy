package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginCliente {
    private String cpf;
    private String senha;

    public LoginCliente(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    public interface ValidarLoginListener {
        void onResultado(Cliente clienteLogado);
    }

    public static void ValidarLoginCliente(LoginCliente login, RequestQueue solicitacao, LoginCliente.ValidarLoginListener listener) {
        String url = "http://localhost:5000/api/Login";

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

                      Cliente clienteLogado = new Cliente(response.getString("Nome"), response.getString("Cpf"), response.getString("Email"), response.getString("Telefone"));

                      listener.onResultado(clienteLogado);

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

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }
}
