package XLSProcess;

import DBConnection.DBConnect;
import DBOperaciones.DBCrud;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Misael Recinos
 */
public class GetParameters {

    DBConnect db;
    Connection con;
    Statement q;
    ResultSet result;

    public String getPath(String parameters) {
        String value = "";
        db = new DBConnect();
        con = db.Connect();

        try {
            q = con.createStatement();
            value = new DBCrud().findPath(q, parameters, "SV");
        } catch (SQLException ex) {
            Logger.getLogger(GetParameters.class.getName()).log(Level.SEVERE, null, ex);
        }

        return value;
    }
}
