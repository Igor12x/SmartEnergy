package Models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Interfaces.IMedidorBuscarConsumoDiario;
import Interfaces.IMedidorBuscoConsumoAtual;

public class Medidor {

    public static void buscarConsumoAtual(FirebaseDatabase database, FirebaseAuth firebaseAuth){


        String userEmail = "igorbtenorioidi@gmail.com";
        String userPassword = "123456789";

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference referencia = database.getReference("Medidor");
                            referencia.addValueEventListener(new ValueEventListener() {
                                double consumoTotal = 0.0;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot idSnapshot :snapshot.getChildren()){
                                        String idResponse = idSnapshot.getKey();

                                        Long idResidenciaResponse = idSnapshot.child("idResidencia").getValue(Long.class);
                                        double consumoResponse = idSnapshot.child("consumo").getValue(Double.class) != null ? idSnapshot.child("consumo").getValue(Double.class) : 0.0;
                                        double medicaoAtualResponse = idSnapshot.child("medicaoAtual").getValue(Double.class) != null ? idSnapshot.child("medicaoAtual").getValue(Double.class) : 0.0;
                                        String dataResponse = idSnapshot.child("data").getValue(String.class);
                                        String horarioResponse = idSnapshot.child("horario").getValue(String.class);

                                        if (idResidenciaResponse != null && idResidenciaResponse == 1 && isCurrentMonth(dataResponse)) {
                                            consumoTotal += consumoResponse;
                                        }

                                        System.out.println("Consumo Total: " + consumoTotal);
                                        // Faça o que for necessário com os dados lidos
                                        // Por exemplo, exiba-os em um TextView ou armazene-os em uma lista
                                        // Aqui, estou apenas imprimindo os dados no console
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            // Autenticação falhou, trate o erro aqui
                            Exception exception = task.getException();
                            if (exception != null) {
                                Log.e("Authentication", "Erro de autenticação: " + exception.getMessage());
                            }
                        }
                    }
                });
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
                // Trate o cancelamento da consulta, se necessário
            }
        });


    }
    public static boolean isCurrentMonth(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = sdf.parse(data);
            Calendar calendar = Calendar.getInstance();
            int currentMonth = calendar.get(Calendar.MONTH);
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH);
            return month == currentMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
