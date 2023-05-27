package Models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Medidor {
private static double consumoTotal = 0.0;
    public static double buscarConsumoMesAtual(FirebaseDatabase database, int idResidencia, Context context){

                            DatabaseReference referencia = database.getReference("Medidor");
                            referencia.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dadosPai) {
                                    for (DataSnapshot dadosFilho :dadosPai.getChildren()){

                                        Long idResidenciaResponse = dadosFilho.child("idResidencia").getValue(Long.class);
                                        double consumo = dadosFilho.child("consumo").getValue(Double.class) != null ? dadosFilho.child("consumo").getValue(Double.class) : 0.0;
                                        String data = dadosFilho.child("data").getValue(String.class);

                                        if (idResidenciaResponse != null && idResidenciaResponse == idResidencia && verificarMesAtual(data)) {
                                            consumoTotal += consumo;
                                        }
                                    }
                                    System.out.println("Consumo Total: " + consumoTotal);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(context, "Não foi possível ler o seu consumo desse mês", Toast.LENGTH_SHORT).show();
                                    Log.e("DatabaseError", "Erro ao acessar banco de dados do Firebase: " + error.getMessage());
                                }
                            });
                            return consumoTotal;
    }
    public static void buscarConsumoDiario(FirebaseDatabase database, FirebaseAuth firebaseAuth) {

        DatabaseReference referencia = database.getReference("Medidor");
        String currentDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double consumoTotalDiario = 0.0;

                for (DataSnapshot idSnapshot : snapshot.getChildren()) {
                    double consumoResponse = idSnapshot.child("consumo").getValue(Double.class) != null ? idSnapshot.child("consumo").getValue(Double.class) : 0.0;
                    String dataResponse = idSnapshot.child("data").getValue(String.class);
                    Long idResidenciaResponse = idSnapshot.child("idResidencia").getValue(Long.class);

                    if ((dataResponse != null && dataResponse.equals(currentDay) && idResidenciaResponse != null && idResidenciaResponse == 1)) {
                        consumoTotalDiario += consumoResponse;
                    }
                }
                System.out.println("consumoTotalDiario: " + consumoTotalDiario);
                System.out.println("currentDay: " + currentDay);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public static boolean verificarMesAtual(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        SimpleDateFormat formatar = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date dataFormata = formatar.parse(data);
            Calendar calendario = Calendar.getInstance();
            int mesAtual = calendario.get(Calendar.MONTH);
            calendario.setTime(dataFormata);
            int mesRecebido = calendario.get(Calendar.MONTH);
            return mesRecebido == mesAtual;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
