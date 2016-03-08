package DBOperaciones;

import DBConnection.DBConnect;
import Log.LogEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misael Recinos
 */
public class DBCrud {

    DBConnect db; // Instancia de la clase DBConnect
    Connection con; // variable para realizar conexión
    Statement q; // variable para manejo de sentencias sql
    ResultSet result; // variable para resultados sql

    /* Método dedica a la inserción de valores de 
     capturas de los archivos de alveo.
     */
    public void insertValues(List values) {
        db = new DBConnect(); //Se asigna una instancia de conexión de base de datos
        con = db.Connect(); //apertura de conexión

        try {
            q = con.createStatement();
            q.execute("INSERT INTO "
                    + "SM_FARINO(IDPAIS,IDPLANTA,FFARINO,USUARIOF,IDMUESTRA,ABS,ESTA,MTI,DEC,QN)"
                    + "VALUES('SV','M201','" + values.get(0) + "','" + values.get(1) + "',"
                    + "'" + values.get(2) + "','" + values.get(3) + "','" + values.get(4) + "','" + values.get(5) + "',"
                    + "'" + values.get(6) + "','" + values.get(7) + "')");
            new LogEvent().LogFarino("Inserción de datos exitosa");
        } catch (SQLException ex) {
            System.out.println("Error en insertar Farino: " + ex.getMessage());
            new LogEvent().LogFarino("Error en insertar Farino: " + ex.getMessage());
        }

        try {
            con.close();
            q.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBCrud.class.getName()).log(Level.SEVERE, null, ex);
            new LogEvent().LogFarino("Error DBCrud: " + ex.getMessage());
        }
    }

    /*
     Busqueda de archivos de de alveo
     */
    public boolean findFile(Statement query, String file) {
        String sql = "SELECT * FROM SM_FARINO WHERE FFARINO='" + file + "'";
        boolean ok = false;
        try {
            result = query.executeQuery(sql);
            while (result.next()) {
                ok = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBCrud.class.getName()).log(Level.SEVERE, null, ex);
            new LogEvent().LogFarino("Error DBCrud, busqueda: " + ex.getMessage());
        }
        return ok;
    }

    public String findPath(Statement query, String param, String pais) {
        String sql = "SELECT * FROM DT_PARAMETERS WHERE PARAMETER='" + param + "' AND IDPAIS='" + pais + "'";
        String value = "";
        try {
            result = query.executeQuery(sql);
            while (result.next()) {
                value = result.getString("VALUE");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;
    }
}
