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

    /**
     * Establecemos la conexion a la ip del cole junto con el usuario y
     * contraseña de ORACLE y si no puede conectar a esa, se intentará conectar
     * a la BD desde fuera del colegio con el usuario y contraseña de ORACLE
     * también.
     *
     */
    public Conexion() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try {
                connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.180.10:1521:INSLAFERRERI", "ericdote", "1234");
            } catch (SQLException ex) {
                try {
                    connection = DriverManager.getConnection("jdbc:oracle:thin:@ieslaferreria.xtec.cat:8081:INSLAFERRERI", "ericdote", "1234");
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        } catch (ClassNotFoundException cnf) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, cnf);
        }
    }

    /**
     * Devuelve la conexión
     *
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Finalizar conexión
     *
     * @throws SQLException
     */
    public void finalizarConexion() throws SQLException {
        connection.close();
    }
    /**
     * Metodo que le llega un objeto Ubicaciones y sirve para realizar un Insert Into de la ubicacion
     * Una vez hecha la sentencia sql pasamos todos los datos del objeto ubicacion al PreparedStatement
     * Convertimos la fecha para que no de problemas con la BBDD al ser diferente tipo de Date
     * I devolvemos un booleano;
     * @param ubi
     * @return
     * @throws SQLException
     * @throws ParseException 
     */
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

    /**
     * Método para obtener todas las matriculas junto a sus contraseñas de la
     * tabla de los autobuses.
     *
     * @return
     * @throws SQLException
     */
    public List<Autobuses> obtenerAutobuses() throws SQLException {
        ResultSet rset;
        List<Autobuses> lista = new ArrayList();
        String sql = "SELECT * FROM AUTOBUSES";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        rset = stmt.executeQuery();
        while (rset.next()) {
            lista.add(new Autobuses(rset.getString("MATRICULA"), rset.getString("PASSWORD")));
        }
        finalizarConexion();
        return lista;
    }

    public Autobuses obtenerAutobus(String matricula) throws SQLException {
        Autobuses aut = null;
        ResultSet rset;
        String sql = "SELECT * FROM AUTOBUSES WHERE MATRICULA LIKE ?";

        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString(1, matricula);
        rset = stmt.executeQuery();
        while (rset.next()) {
            aut = new Autobuses(rset.getString("MATRICULA"), rset.getString("PASSWORD"));
        }
        finalizarConexion();
        return aut;
    }

    /**
     * Método que realiza la consulta para obtener la ubicación del bus con la
     * matricula que se pasa por parametro y devuelve la ubicacion de ese bus
     *
     * @param matricula
     * @return
     * @throws SQLException
     */
    public List<Ubicaciones> obtenerUbicacionBus(String matricula) throws SQLException {
        List<Ubicaciones> ubi = new ArrayList<>();
        ResultSet rset;
        String sql = "SELECT * FROM (SELECT * FROM UBICACION WHERE matricula LIKE ? ORDER BY data DESC) WHERE ROWNUM <=5";

        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString(1, matricula);
        rset = stmt.executeQuery();
        while (rset.next()) {
            ubi.add(new Ubicaciones(rset.getString("MATRICULA"), rset.getDouble("LATITUD"), rset.getDouble("LONGITUD"), rset.getString("DATA")));
        }
        finalizarConexion();
        return ubi;
    }

    /**
     * Mètodo que realiza la consulta para obtener las últimas posiciones de
     * todos los buses.
     *
     * @return
     * @throws SQLException
     */
    public List<Ubicaciones> obtenerUltimaPosBuses() throws SQLException {
        ResultSet rset;
        List<Ubicaciones> lista = new ArrayList<>();
        String sql = "SELECT * FROM UBICACION WHERE (MATRICULA, DATA) IN (SELECT MATRICULA, MAX(DATA) FROM UBICACION GROUP BY MATRICULA)";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        rset = stmt.executeQuery();
        while (rset.next()) {
            lista.add(new Ubicaciones(rset.getString("MATRICULA"), rset.getDouble("LATITUD"), rset.getDouble("LONGITUD"), rset.getString("DATA")));
        }
        finalizarConexion();
        return lista;
    }
    /**
     * Metodo para convertir de util.Date a sql.Date
     * @param uDate
     * @return 
     */
    private java.sql.Date convertUtilToSql(java.util.Date uDate) {

        java.sql.Date sDate = new java.sql.Date(uDate.getTime());

        return sDate;
    }
}
