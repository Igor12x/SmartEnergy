package Models;


import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        public int calculardDiasRestantes(Date hoje, int diaFechamentoFatura) {

            Calendar fechamento = Calendar.getInstance();
            fechamento.set(Calendar.DAY_OF_MONTH, diaFechamentoFatura);
            fechamento.add(Calendar.MONTH, 1);
            Date dataFechamento = fechamento.getTime();

            // Cálculo da diferença em dias
            int diffInMs = Math.round(dataFechamento.getTime() - hoje.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMs, TimeUnit.MILLISECONDS);

            return (int) diffInDays;
        };
    public int contadorDias(Date hoje, int diaFechamentoFatura) {

        Calendar fechamento = Calendar.getInstance();
        fechamento.set(Calendar.DAY_OF_MONTH, diaFechamentoFatura);
        Date dataFechamento = fechamento.getTime();

        // Cálculo da diferença em dias
        int diferencaMs = Math.round(hoje.getTime() - dataFechamento.getTime());
        long diferencaDias = TimeUnit.DAYS.convert(diferencaMs, TimeUnit.MILLISECONDS);

        return (int) diferencaDias + 1; // +1 por conta que na subtração  em Ms não leva em conta que deve somar o ultimo dia também
    };

    //pra que Parse???????? Segura esse método sem chatGPT
    public double formatarDouble(double valor) {
        DecimalFormat df = new DecimalFormat("0.00");
        String converter = df.format(valor);
        converter = converter.replace(",", ".");
        valor = Double.parseDouble(converter);
        return valor;
    };
}
