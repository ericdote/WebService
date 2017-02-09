/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Lluis_2
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

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



}
