/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cecomsaguraboast
 */
public class Overview {
    
    public static void getSwitches(JSONArray Switches) throws JSONException {
        /* 
         LOGICA PARA SACAR LOS OBJETOS JSON DE CADA SWITCH CONECTADO A FLOODLIGHT
         DE ACUERDO A LA LONGITUD DEL ARREGLO DE SWITCHES SE SACAN EL NUMERO DE SWITCHES
         QUEDA PENDIENTE GUARDAR ESTOS DATOS EN LA BASE DE DATOS
        */
        for(int i = 0; i < Switches.length(); i++) {
            String dpid = null,inetAddress = null;
            
            JSONObject sw = Switches.getJSONObject(i);
            
            dpid = sw.getString("dpid");
            inetAddress = sw.getString("inetAddress");
            
            System.out.println("Information about switch with DPID: " + dpid);
            System.out.println("IP Address: " + inetAddress);
              
            JSONArray ports = sw.getJSONArray("ports");

            for(int j = 0; j < ports.length(); j++) {
                int portNumber,state;
                String name = null, hardwareAddress = null;
                JSONObject port = ports.getJSONObject(j);
                
                name = port.getString("name");
                hardwareAddress = port.getString("hardwareAddress");
                portNumber = port.getInt("portNumber");
                state = port.getInt("state");
                
                System.out.println("");
                System.out.println("Name: " + name);
                System.out.println("MAC: " + hardwareAddress);
                System.out.println("Port Number: " + portNumber);
                System.out.println("State: " + state);
            }
            
            JSONObject description = sw.getJSONObject("description");
            String software = null, hardware = null, manufacturer = null,
                    serialNum = null, datapath = null;
            
            software = description.getString("software");
            hardware = description.getString("hardware");
            manufacturer = description.getString("manufacturer");
            serialNum = description.getString("serialNum");
            datapath = description.getString("datapath");
            
            System.out.println("");
            System.out.println("Software: " + software);
            System.out.println("Hardware: " + hardware);
            System.out.println("Manufactuer: " + manufacturer);
            System.out.println("Serial: " + serialNum);
            System.out.println("Datapath: " + datapath);
            System.out.println("");     
        }
    }
    
    public static void getFlows() {
        
    }
}
