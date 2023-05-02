package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

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

    public interface ValidarCadastroListener {
        void onResultado(Cliente clienteCadastrado);
    }

    public static void ValidarCadastroCliente(CadastroCliente cadastro, RequestQueue solicitacao, CadastroCliente.ValidarCadastroListener listener) {
        String url = "http://localhost:5000/api/Cadastro";

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
                try {

                    Cliente clienteCadastrado = new Cliente(response.getString("Nome"), response.getString("Cpf"), response.getString("Senha"), response.getString("Email"), response.getString("Telefone"));

                    listener.onResultado(clienteCadastrado);

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

    public String getNome() { return nome; }

    public String getSenha() { return senha; }

    public String getCpf() { return cpf; }

    public String getEmail() { return email; }

    public String getTelefone() { return telefone; }
}
