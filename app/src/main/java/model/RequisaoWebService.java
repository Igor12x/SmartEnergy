package model;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.example.projetotcc.Progressbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RequisaoWebService extends Progressbar {
    private List<Consumo> consumoList = new ArrayList<>();
    public List consumoHoje(int id) {

        //Configuração do endpoint (url) da requisição
        String url = "";
        //Criar um objeto da classe Volley para configurar as requisições ao webservice
        RequestQueue solicitacao = Volley.newRequestQueue(this);
        //Configurando a requisição a ser enviada
        JsonArrayRequest envio = new JsonArrayRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    //Recuperar cada objeto do webservice
                    JSONObject object = response.getJSONObject(id);
                    Consumo consumo = new Consumo(object.getInt("id"),
                            object.getDouble("kWh"));
                    consumoList.add(consumo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        solicitacao.add(envio);
        return consumoList;

    }
}

