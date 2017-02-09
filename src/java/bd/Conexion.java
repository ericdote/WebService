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
import java.util.ArrayList;
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
            connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.180.10:1521:INSLAFERRERI", "edote", "1234");
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

    public boolean insertarCliente(Ubicaciones ubi) throws SQLException {
        String sql = "INSERT INTO Ubicacion (matricula, latitud, longitud, data) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, ubi.getMatricula());
        stmt.setDouble(2, ubi.getLatitud());
        stmt.setDouble(3, ubi.getLongitud());
        stmt.setString(4,ubi.getData());
        int res = stmt.executeUpdate();
        finalizarConexion();

        return (res == 1);
    }

    public List<Autobuses> obtenerAutobuses() throws SQLException {
        ResultSet rset;
        List<Autobuses> lista = new ArrayList();
        String sql = "SELECT matricula FROM Autobus";
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

        String sql = "SELECT matricula, password FROM Autobuses WHERE matricula = ?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString(1, matricula);
        rset = stmt.executeQuery();
        while (rset.next()) {            
            aut = new Autobuses(rset.getString("matricula"),  rset.getString("password"));

        }
        finalizarConexion();
        return aut;
    }

    public List<Ubicaciones> obtenerUbicacionBus(String matricula) throws SQLException{
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

}
