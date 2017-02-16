/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lluis_2
 */
public class Conexion {

    Connection connection;

    public Conexion() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.180.10:1521:INSLAFERRERI", "ericdote", "1234");
            // connection = DriverManager.getConnection("jdbc:oracle:thin:@ieslaferreria.xtec.cat:8081:INSLAFERRERI", "PROFEA1","1234");
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void finalizarConexion() throws SQLException {
        connection.close();
    }

    public boolean insertarUbicacion(Ubicaciones ubi) throws SQLException, ParseException {
        int res;
        String sql = "INSERT INTO UBICACION VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        String date;
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        stmt.setString(1, ubi.getMatricula());
        stmt.setDouble(2, ubi.getLatitud());
        stmt.setDouble(3, ubi.getLongitud());
        date = ubi.getData();
        Date data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);        
        java.sql.Date sDate = convertUtilToSql(data);
        stmt.setDate(4, sDate);
        res = stmt.executeUpdate();
        return (res == 1);

    }

    public List<Autobuses> obtenerAutobuses() throws SQLException {
        ResultSet rset;
        List<Autobuses> lista = new ArrayList();
        String sql = "SELECT matricula FROM AUTOBUSES";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        rset = stmt.executeQuery();
        while (rset.next()) {
            lista.add(new Autobuses(rset.getString("matricula"), rset.getString("password")));

        }
        finalizarConexion();
        return lista;
    }

    public Autobuses obtenerAutobus(String matricula) throws SQLException {
        Autobuses aut = null;
        ResultSet rset;
        String sql = "SELECT MATRICULA, PASSWORD FROM AUTOBUSES WHERE MATRICULA LIKE ?";

        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString(1, matricula);
        rset = stmt.executeQuery();
        while (rset.next()) {
            aut = new Autobuses(rset.getString("MATRICULA"), rset.getString("PASSWORD"));
        }
        finalizarConexion();
        return aut;
    }

    public List<Ubicaciones> obtenerUbicacionBus(String matricula) throws SQLException {
        ResultSet rset;
        List<Ubicaciones> lista = new ArrayList();
        String sql = "SELECT longitud, latitud, data FROM Cliente WHERE matricula = ?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        rset = stmt.executeQuery();
        while (rset.next()) {
            lista.add(new Ubicaciones(rset.getString("matricula"), rset.getDouble("latitud"), rset.getDouble("longitud"), rset.getString("data")));

        }
        finalizarConexion();
        return lista;
    }
    
    private  java.sql.Date convertUtilToSql(java.util.Date uDate) {

        java.sql.Date sDate = new java.sql.Date(uDate.getTime());

        return sDate;
    }


}
