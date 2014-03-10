/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.NullPointerException;
import java.beans.Statement;

/**
 *
 * @author cecomsaguraboast
 */
public class Database {
    
    public String driver,url,db,usr,pass,server,port;
    public String error_db="";
    public Connection conexion;

    public void Connect(String hport,String hdb, String husr, String hpass, 
            String hserver, String hdriver, String dbmanager) {
        port   = hport;
        db     = hdb;
        usr    = husr;
        pass   = hpass;
        server = hserver;
        driver = hdriver;
        url    = dbmanager;
		
		// "jdbc:"+dbmanager+"://"+server+":"+port+"/"+db;  
		// jdbc:oracle:thin:@test:1521:orcl
		// oracle.jdbc.driver.OracleDriver
		    
        try {
            Class.forName(driver).newInstance();
            conexion = DriverManager.getConnection(url,usr,pass);
            System.out.println("** Connected to the DataBase " + db + ":" + url + " **\n");
        } catch (Exception exc) {
            System.err.println("** Error connecting to the DataBase **\n" + url + "\n" + exc.getMessage());
            System.exit(1); // ESTA ESTO BIEN?? NO ME CIERRA MI APP?
        }
    }
     
    public void Connect() {
        // parametros para postgres
        port   = "5432";
        db     = "proyecto";
        usr    = "postgres";
        pass   = "fausto89"; // AJUSTAR PASSWORD 
        server = "localhost";
        driver = "org.postgresql.Driver";
        url    = "jdbc:postgresql://" + server + ":" + port + "/" + db;
		//jdbc:oracle:thin:@test:1521:orcl

      //loadDriver();

        try {
            Class.forName(driver).newInstance();
            conexion = DriverManager.getConnection(url,usr,pass);
            System.out.println("** Connected to the DataBase " + db + ":" + url + " **\n");
        } catch (Exception exc) {
            System.err.println("** Error connecting to the DataBase **\n" + url + "\n" + exc.getMessage());
            System.exit(1); // ESTA ESTO BIEN?? NO ME CIERRA MI APP?
        }
    }

    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException err) {
            System.err.println("** Could not load PostgreSQL driver **\n");
            System.exit(1);
        }
    }
	
    //metodo que retorna un resultset pasandole el query
    public ResultSet exec_sel(String query) {
        try {
            java.sql.Statement comando = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = comando.executeQuery(query);
            rs.beforeFirst();
            rs.next();
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("** Error de Base de datos, select **\n" + query + "\n" + ex.getMessage());
            return null;
        }
    }

    // metodo para ejecutar update, insert o delete
    public void exec_sql(String query) {
        try {
            java.sql.Statement stmt = conexion.createStatement();
            stmt.executeUpdate(query);		
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("** Error de Base de datos, insert, update, delete **\n"+query+"\n"+ex.getMessage());
        }
    }

    //metodo para obtener la cantidad de registros
    public int CantRegistros(ResultSet rs) {
        try {
            int int_cantreg=0;
            rs.beforeFirst();
            while(rs.next()) {
                int_cantreg++;
            }
            rs.beforeFirst();
            rs.next(); 
            return int_cantreg;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void CerrarConexion() {
        try {
            conexion.close();
            conexion = null;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
