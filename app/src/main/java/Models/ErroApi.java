package Models;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class ErroApi {
    public static String mensagemErroCadastro(VolleyError error) {
        if (error instanceof NetworkError) {
            return "Erro de conexão de rede. Verifique sua conexão e tente novamente.";
        } else if (error instanceof ServerError) {
            return "Erro no servidor. Tente novamente mais tarde.";
        } else if (error instanceof TimeoutError) {
            return "Tempo limite da solicitação excedido. Tente novamente.";
        } else {
            return "Ocorreu um erro. Tente novamente." + " Erro: " + error;
        }
    }
    public static String mensagemErroRecuperarSenha(VolleyError error){
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.statusCode == 404) {
            return "O recurso solicitado não foi encontrado.";
        } else if (networkResponse != null && networkResponse.statusCode == 500) {
            return "O email fornecido não possui cadastro.";
        } else {
            return "Um erro inesperado ocorreu. Tente novamente mais tarde." + " Erro: " + error;
        }
    }
    public static String mensagemErroLogin(VolleyError error){
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.statusCode == 404) {
            return "Não foi possível realizar solicitar o login no servidor.";
        } else if (networkResponse != null && networkResponse.statusCode == 500) {
            return "E-mail ou senha inválidos";
        } else {
            return "Um erro inesperado ocorreu. Tente novamente mais tarde." + " Erro: " + error;
        }
    }
}

