package Models;

import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class ErroApi {
    public static String mensagemErro(VolleyError error) {
        if (error instanceof NetworkError) {
            return "Erro de conexão de rede. Verifique sua conexão e tente novamente.";
        } else if (error instanceof ServerError) {
            return "Erro no servidor. Tente novamente mais tarde.";
        } else if (error instanceof TimeoutError) {
            return "Tempo limite da solicitação excedido. Tente novamente.";
        } else {
            return "Ocorreu um erro. Tente novamente.";
        }
    }
}

