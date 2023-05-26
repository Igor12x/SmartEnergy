package Interfaces;

public interface IRecuperarSenhaCodigoVerificacao {
    void onResultado(String codigoVerificacao);
    void onError(String mensagemErro);
}
