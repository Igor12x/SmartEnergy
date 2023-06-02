package Interfaces;

import Models.Cliente;

public interface ILoginCliente {
    void onResultado(Cliente clienteLogado);
    void onErro(String mensagemErro);

}
