package Interfaces;

public interface IRecuperarSenhaCliente {
    void onResultado(String codigoVerificacao);
    void onError(String mensagemErro);
}
