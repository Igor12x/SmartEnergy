package Interfaces;

import Models.Cliente;

public interface IRecuperarSenhaRedefinir {
    void onResultado(boolean alterado);
    void onError(String mensagemErro);
}