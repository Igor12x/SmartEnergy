package Interfaces;

import Models.Cliente;

public interface ICadastroCliente {
    void onResultado(String nome);
    void onErro(String mensagemErro);
}
