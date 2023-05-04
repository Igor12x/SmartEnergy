package Models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Medidor {

    public int codigo;
    public double consumo;
    public String registro_dia;
    public String registro_horario;

    public Medidor(int codigo, double consumo, String registro_dia, String registro_horario) {
        this.codigo = codigo;
        this.consumo = consumo;
        this.registro_dia = registro_dia;
        this.registro_horario = registro_horario;
    }

    //interface para salvar o resultado da API
    public interface BuscaConsumoListener {
        void onResultado(double resultado);

    }

    public interface BuscaConsumoDiarioListener {
        void onResultado(String resultado);

    }

    public static void buscarConsumoAtual(int id, RequestQueue solicitacao, BuscaConsumoListener listener) {
        String url = "http://10.0.2.2:5000/api/Medidor/BuscarConsumo";

        //Criar um objeto da classe Volley para configurar as requisições ao webservice
        //Configurando a requisição a ser enviada

        JsonArrayRequest envio = new JsonArrayRequest(Request.Method.GET, url + "/" + id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("onResponse", response.toString());

                try {
                    //Pegando do JSON da API o objeto de index 0
                    JSONObject object = response.getJSONObject(0);
                    //criando o objeto medidor passando os atributos do objeto JSON no construtor
                    Medidor medidor = new Medidor(object.getInt("codigo"),
                            object.getDouble("consumo"), object.getString("registro_dia"), object.getString("registro_horario"));
                    //chamando método onResultado como callback
                    listener.onResultado(medidor.consumo);
                    Log.i("medor", medidor.toString());
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


    public static void buscarConsumoDiario(int id, RequestQueue solicitacao, BuscaConsumoDiarioListener listener) {
        String url = "http://10.0.2.2:5000/api/Medidor/BuscarConsumoDiario";

        //Criar um objeto da classe Volley para configurar as requisições ao webservice
        //Configurando a requisição a ser enviada

        JsonObjectRequest envio = new JsonObjectRequest(Request.Method.GET, url + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());

                try {
                    //Pegando do JSON da API o objeto de index 0
                    JSONObject object = response;

                    //chamando método onResultado como callback
                    listener.onResultado(object.getString("consumoDiario"));
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
}