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
    
    public void InsertSwitch(int index, String dpid, String inetaddress, 
            String software, String hardware, String manufacturer, 
            String serialnum, String datapath, int connectedsince) 
            throws IOException {
        String sql = "SELECT upsert('" + index + "','" + dpid + "','" + 
                inetaddress + "','" + connectedsince + "','" + software + 
                "','" + hardware + "','" + manufacturer + "','" + serialnum + 
                "','" + datapath + "')";
        db.Connect();
        ResultSet result = db.exec_sel(sql);
        System.out.println("DB: Sucessfully inserted switch with dpid: " + 
                dpid + "\n");
        db.CerrarConexion();
    }
    
    public void InsertPort(int port_id, int switchh, int portNumber, 
            String name, String hardwareAddress, int state) 
            throws IOException {
        String sql = "SELECT upsertport('" + port_id + "','" + switchh + "','" 
                + portNumber + "','" + name + "','" + hardwareAddress + 
                "','" + state + "')";
        db.Connect();
        ResultSet resultado = db.exec_sel(sql);
        System.out.println("DB: Inserted port: " + name + "\n");
        db.CerrarConexion();
    }

    public void InsertFlow(int index, int switchh, String name, int priority, 
            int inputport, String type, int outport) throws IOException {
        String sql = "SELECT upsertflow('" + index + "','" + switchh + "','" + 
                name + "','" + priority + "','" + inputport + "','" + 
                type + "','" + outport + "')";
        db.Connect();
        ResultSet resultado = db.exec_sel(sql);
        System.out.println("DB: Inserted flow: " + name + "\n");
        db.CerrarConexion();
    }

    public void InsertPortStatistics(String date, int j, int i, int portNumber, 
            int receiveBytes, int collisions, int transmitBytes, 
            int transmitPackets, int receivePackets) {
        // SE DEBE ABUNDAR ESTA FUNCION.
    }
    
    public void InsertFlowStatistics(String date) {
        
    }
    
    public int getIndex(String dpid) throws SQLException {
        System.out.println("DB: Attempting to get Switch index");
        String sql = "SELECT sw_id FROM Switches WHERE dpid='" + dpid + "'";
        db.Connect();
        ResultSet resultado = db.exec_sel(sql);
        System.out.println("Sucessfully got Switch index: " + resultado.getInt(1));
        db.CerrarConexion();
        return resultado.getInt(1);
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
}