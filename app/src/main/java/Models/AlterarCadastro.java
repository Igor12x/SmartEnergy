package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Interfaces.IAlterarCadastro;

public class AlterarCadastro {
    private String email;
    private String telefone;

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }
    public AlterarCadastro(String email, String telefone) {
        this.email = email;
        this.telefone = telefone;
    }

    public static void Alterar(AlterarCadastro dados, int codigoCliente, RequestQueue solicitacao, IAlterarCadastro listener){
        //String url = "http://10.0.2.2:5000/api/AlterarCadastro/" + codigoCliente;
        String url = "http://localhost:5000/api/AlterarCadastro/" + codigoCliente;

        JSONObject enviarAlteracao = new JSONObject();

        try {
            enviarAlteracao.put("email", dados.getEmail());
            enviarAlteracao.put("telefone", dados.getTelefone());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.PUT, url, enviarAlteracao, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                Cliente clienteAtualizado = new Cliente(response.getString("Nome"),
                        response.getString("Sobrenome"),
                        response.getString("Cpf"),
                        response.getString("Email"),
                        response.getString("Telefone"),
                        response.getString("Senha"),
                        response.getInt("Codigo"));
                    listener.onResultado(clienteAtualizado);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponseEditarPerfil", error.toString());
            }
        });
        solicitacao.add(envio);
    }
}
