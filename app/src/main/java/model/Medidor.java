package model;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.projetotcc.Progressbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Medidor extends Progressbar{

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

    public interface BuscaConsumoListener {
        void onResultado(double resultado);

    }

    public static void buscarConsumoAtual(int id, RequestQueue solicitacao, BuscaConsumoListener listener){
        String url = "http://10.0.2.2:5000/api/Medidor/1";

        //Criar um objeto da classe Volley para configurar as requisições ao webservice
               //Configurando a requisição a ser enviada

        JsonArrayRequest envio = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("onResponse", response.toString());
                for(int i=0 ; i<response.length() ; i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);

                        Medidor medidor = new Medidor(object.getInt("codigo"),
                                object.getDouble("consumo"), object.getString("registro_dia"), object.getString("registro_horario"));

                        listener.onResultado(medidor.consumo);
                        Log.i("medor", medidor.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
