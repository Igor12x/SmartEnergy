package Interfaces;

import Models.Cliente;

public interface IRecuperarSenhaRedefinir {
    void onResultado(String alterado);
    void onError(String mensagemErro);
}
