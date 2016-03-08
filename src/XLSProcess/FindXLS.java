/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XLSProcess;

import DBConnection.DBConnect;
import DBOperaciones.DBCrud;
import Log.LogEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Misael Recinos
 */

public class FindXLS {

    DBConnect db;
    Connection con;
    Statement q;
    ResultSet result;

    public void FileSearch(String file) {
        db = new DBConnect();
        con = db.Connect();
        try {
            q = con.createStatement();
            boolean get = new DBCrud().findFile(q, file);

            if (!get) {
                System.out.println("Busqueda de datos en: " + file);
                new LogEvent().LogFarino("Busqueda de datos en: "+file);
                new FileXLS_Process().ReadExcel(file);
            }else
            {
                
                new LogEvent().LogFarino("------------------------------------"
                        +'\n'+ " Sin cambios, Sin Datos a ingresar"+ '\n'+
                        "------------------------------------");
            }

        } catch (SQLException ex) {
            System.err.println("Busqueda de archivo: " + ex.getMessage());
            new LogEvent().LogFarino("Busqueda de archivo: " + ex.getMessage());
        }
        try {
            con.close();
            q.close();
        } catch (SQLException ex) {
            System.err.println("Error en buscar archivo");
            new LogEvent().LogFarino("Error en buscar archivo" + ex.getMessage());
        }
    }
}
