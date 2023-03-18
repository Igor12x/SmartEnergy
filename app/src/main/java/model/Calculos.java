package model;



public class Calculos {
    public double calcularProjecao (double totalContaAtual/*valor da conta de energia até o momento*/,
                                    int contatorDias/*criada para contar quantos dias se passaram*/,
                                    int diasRestantes /*quantos dias faltam até a próxima medição/leitura*/){

        return ((totalContaAtual/contatorDias)*diasRestantes) + totalContaAtual;
    };
    public double calcularValorConta (double tarifaTUSDimpostos /*tarifa com impostos TUSD*/,
                                         double tarifaTEimpostos /*tarifa com impostos TE*/,
                                         double kWh /*energia consumida em kWh*/){

      return (kWh * tarifaTUSDimpostos) + (kWh * tarifaTEimpostos);
    };
    public double calcularTarifaImpostos (double tarifaAneelSemImpostos,
                                             double pis,
                                             double confins,
                                             double icms){

       double tarifaKWH = tarifaAneelSemImpostos/1000;
       double pisPercentual = pis/100;
       double confinsPercentual = confins/100;
       double icmsPercentual = icms/100;

        return (tarifaKWH)/(1-(pisPercentual+confinsPercentual+icmsPercentual));
    };
}
