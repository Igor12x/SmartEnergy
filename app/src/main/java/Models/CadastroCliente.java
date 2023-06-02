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

        public static void ValidarCadastroCliente(Cliente cadastro, RequestQueue solicitacao, ICadastroCliente listener) {
            //String url = "http://10.0.2.2:5000/api/Cadastro";
            String url = "http://localhost:5000/api/Cadastro";

            JSONObject enviarCliente = new JSONObject();

            try {
                enviarCliente.put("nome", cadastro.getNome());
                enviarCliente.put("sobrenome", cadastro.getSobrenome());
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
                    listener.onResultado(response.getString("nome").toString());
                } catch (Exception e){
                    e.printStackTrace();
                    listener.onErro("Ocorreu um erro ao processar a resposta do servidor. Tente novamente.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponse", error.toString());
                String errorMessage = ErroApi.mensagemErroCadastro(error);
                listener.onErro(errorMessage);
            }
        });
        solicitacao.add(envio);
    }
}
