package model;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Arduino {

    public int codigo;
    public double kWh;
    public String registro_dia;
    public String registro_horario;

    public Arduino(int codigo, double kWh, String registro_dia, String registro_horario) {
        this.codigo = codigo;
        this.kWh = kWh;
        this.registro_dia = registro_dia;
        this.registro_horario = registro_horario;
    }
}
