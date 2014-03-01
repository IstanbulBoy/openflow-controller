/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author cecomsaguraboast
 */
public class DBManager {
    Database db = new Database();
    
    public void InsertSwitch(String dpid,String inetaddress,String software,String hardware,String manufacturer,String serialnum,String datapath, int connectedsince) throws IOException {
        String sql = "INSERT INTO Switches(dpid,inetaddress,connectedsince,software,hardware,manufacturer,serialnum,datapath) "
                + "VALUES ('" + dpid + "','" + inetaddress + "','" + connectedsince + "','" + software + "','" + hardware + "','" + manufacturer + "','" + serialnum + "','" + datapath + "')";
        db.Connect();
        db.exec_sql(sql);
        System.out.println("Insert sucessfull\n");
        db.CerrarConexion();
    }
    
    public void SelectSwitches() throws SQLException {
        String sql = "SELECT sw_id,dpid,inetaddress,connectedsince,software,hardware,manufacturer,serialnum,datapath FROM Switches";
        db.Connect();
        ResultSet resultado = db.exec_sel(sql);
        db.CerrarConexion();
        System.out.println("Consulting data...\n");
        System.out.println("ID      DPID        inetAddress     connectedSince      Software        Hardware        Manufacturer        SerialNum       DataPath");
        do {
            System.out.println(resultado.getString(1) + "       " + resultado.getString(2) + "       " + resultado.getString(3) + "       " + resultado.getString(4) + "       " + resultado.getString(5) + "       " + resultado.getString(6) + "       " + resultado.getString(7) +  "       " + resultado.getString(8) + "       " + resultado.getString(9));
        } while(resultado.next());
    }
    
    public void InsertPort(int index,int portNumber,String name,String hardwareAddress,int state) throws IOException {
        String sql = "INSERT INTO ports(sw_id,portnumber,sname,hardwareaddress,state) "
                + "VALUES ('" + index + "','" + portNumber + "','" + name + "','" + hardwareAddress + "','" + state + "')";
        db.Connect();
        db.exec_sql(sql);
        System.out.println("Insert sucessfull\n");
        db.CerrarConexion();
    }
    
    
    
    
    public int getIndex(String dpid) throws SQLException {
        String sql = "SELECT sw_id FROM Switches WHERE dpid='" + dpid + "'";
        db.Connect();
        ResultSet resultado = db.exec_sel(sql);
        db.CerrarConexion();
        return resultado.getInt(1);
    }
}
