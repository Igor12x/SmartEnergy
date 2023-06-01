package Models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Interfaces.IMedidorBuscarConsumoDiario;
import Interfaces.IMedidorBuscoConsumoAtual;

public class Medidor {
    public double consumo;

    public Medidor(double consumo) {
        this.consumo = consumo;
    }

    public static void buscarConsumoMesAtual(FirebaseDatabase database, int idResidencia, Context context, IMedidorBuscoConsumoAtual listener) {
        try {
            DatabaseReference referencia = database.getReference("Medidor");
            referencia.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dadosPai) {
                    Medidor medidor = new Medidor(0);
                    for (DataSnapshot dadosFilho : dadosPai.getChildren()) {
                        Long idResidenciaResponse = dadosFilho.child("idResidencia").getValue(Long.class);
                        Double consumoDouble = dadosFilho.child("consumo").getValue(Double.class);
                        double consumo = (consumoDouble != null) ? consumoDouble : 0.0;
                        String data = dadosFilho.child("data").getValue(String.class);

                        if (idResidenciaResponse != null && idResidenciaResponse == idResidencia && verificarMesAtual(data)) {
                            medidor.consumo += consumo;
                        }
                    }
                    listener.onResultado(medidor.consumo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Não foi possível ler o seu consumo desse mês", Toast.LENGTH_SHORT).show();
                    Log.e("DatabaseError", "Erro ao acessar banco de dados do Firebase: " + error.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Não foi possível ler o seu consumo desse mês", Toast.LENGTH_SHORT).show();
        }
    }
    public static void buscarConsumoDiario(FirebaseDatabase database, int idResidencia, Context context, IMedidorBuscarConsumoDiario listener) {
        try {
            DatabaseReference referencia = database.getReference("Medidor");
            String hoje = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            referencia.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dadosPai) {
                    Medidor medidor = new Medidor(0);
                    for (DataSnapshot dadosFilho : dadosPai.getChildren()) {
                        Double consumoDouble = dadosFilho.child("consumo").getValue(Double.class);
                        double consumo = (consumoDouble != null) ? consumoDouble : 0.0;
                        String data = dadosFilho.child("data").getValue(String.class);
                        Long idResidenciaResponse = dadosFilho.child("idResidencia").getValue(Long.class);

                        if (data != null && data.equals(hoje) && idResidenciaResponse != null && idResidenciaResponse == idResidencia) {
                            medidor.consumo += consumo;
                        }
                    }
                    listener.onResultado(medidor.consumo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Não foi possível ler o seu consumo do dia", Toast.LENGTH_SHORT).show();
                    Log.e("DatabaseError", "Erro ao acessar banco de dados do Firebase: " + error.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Não foi possível ler o seu consumo do dia", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean verificarMesAtual(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        SimpleDateFormat formatar = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dataFormatada = formatar.parse(data);
            Calendar calendario = Calendar.getInstance();
            int mesAtual = calendario.get(Calendar.MONTH);
            if (dataFormatada != null) {
                calendario.setTime(dataFormatada);
            }
            int mesRecebido = calendario.get(Calendar.MONTH);
            return mesRecebido == mesAtual;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
