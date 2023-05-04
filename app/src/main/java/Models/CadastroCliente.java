package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import Interfaces.ICadastroCliente;

public class CadastroCliente {
    private String nome;
    private String senha;
    private String cpf;
    private String email;
    private String telefone;

    public CadastroCliente(String nome,String cpf,  String senha, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.email = email;
        this.telefone = telefone;
    }



    public static void ValidarCadastroCliente(CadastroCliente cadastro, RequestQueue solicitacao, ICadastroCliente listener) {
        String url = "http://10.0.2.2:5000/api/Cadastro";

        JSONObject enviarCliente = new JSONObject();

        try {
            enviarCliente.put("nome", cadastro.getNome());
            enviarCliente.put("cpf", cadastro.getCpf());
            enviarCliente.put("senha", cadastro.getSenha());
            enviarCliente.put("email", cadastro.getEmail());
            enviarCliente.put("telefone", cadastro.getTelefone());
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

    public String getNome() { return nome; }

    public String getSenha() { return senha; }

    public String getCpf() { return cpf; }

    public String getEmail() { return email; }

    public String getTelefone() { return telefone; }
}
