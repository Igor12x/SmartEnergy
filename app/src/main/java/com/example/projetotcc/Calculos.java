package com.example.projetotcc;



public class Calculos {
    protected double Calcularprojecao (double totalContaAtual/*valor da conta de energia até o momento*/,
                                    int contatorDias/*criada para contar quantos dias se passaram*/,
                                    int diasRestantes /*quantos dias faltam até a próxima medição/leitura*/){

        return ((totalContaAtual/contatorDias)*diasRestantes) + totalContaAtual;
    };
}