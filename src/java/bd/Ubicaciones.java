package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 
 * @author Eric
 */
public class Ubicaciones {

    private String matricula;
    private double latitud, longitud;
    private String data;

    public Ubicaciones(String matricula, double latitud, double longitud, String data) {
        this.matricula = matricula;
        this.latitud = latitud;
        this.longitud = longitud;
        this.data = data;
    }

    public String getMatricula() {
        return matricula;
    }


    public double getLatitud() {
        return latitud;
    }


    public double getLongitud() {
        return longitud;
    }


    public String getData() {
        return data;
    }




}
