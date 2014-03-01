/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author cecomsaguraboast
 */
public class Overview {
    
    public static void getSwitches(JSONArray Switches) throws JSONException, IOException, SQLException {
        /* 
         LOGICA PARA SACAR LOS OBJETOS JSON DE CADA SWITCH CONECTADO A FLOODLIGHT
         DE ACUERDO A LA LONGITUD DEL ARREGLO DE SWITCHES SE SACAN EL NUMERO DE SWITCHES
         QUEDA PENDIENTE GUARDAR ESTOS DATOS EN LA BASE DE DATOS
        */
        DBManager dbm = new DBManager();
        
        
        for(int i = 0; i < Switches.length(); i++) {
            String dpid = null,inetAddress = null;
            int connectedSince;
            
            JSONObject sw = Switches.getJSONObject(i);
            
            dpid = sw.getString("dpid");
            inetAddress = sw.getString("inetAddress");
            connectedSince = sw.getInt("connectedSince"); //     REVISAR ESTO?
            
            //System.out.println("Information about switch with DPID: " + dpid);
            //System.out.println("IP Address: " + inetAddress);
            
            JSONObject description = sw.getJSONObject("description");
            String software = null, hardware = null, manufacturer = null,
                    serialNum = null, datapath = null;
            
            software = description.getString("software");
            hardware = description.getString("hardware");
            manufacturer = description.getString("manufacturer");
            serialNum = description.getString("serialNum");
            datapath = description.getString("datapath");
            
            //System.out.println("");
            //System.out.println("Software: " + software);
            //System.out.println("Hardware: " + hardware);
            //System.out.println("Manufactuer: " + manufacturer);
            //System.out.println("Serial: " + serialNum);
            //System.out.println("Datapath: " + datapath);
            //System.out.println("");     
            
            //dbm.Insert(dpid);
            dbm.InsertSwitch(dpid,inetAddress,software,hardware,manufacturer,serialNum,datapath,connectedSince);
            
            
            
            JSONArray ports = sw.getJSONArray("ports");

            for(int j = 0; j < ports.length(); j++) {
                int portNumber,state;
                String name = null, hardwareAddress = null;
                JSONObject port = ports.getJSONObject(j);
                
                name = port.getString("name");
                hardwareAddress = port.getString("hardwareAddress");
                portNumber = port.getInt("portNumber");
                state = port.getInt("state");
                
                //System.out.println("");
                //System.out.println("Name: " + name);
                //System.out.println("MAC: " + hardwareAddress);
                //System.out.println("Port Number: " + portNumber);
                //System.out.println("State: " + state);
                
                int index = dbm.getIndex(dpid);
                
                dbm.InsertPort(index,portNumber,name,hardwareAddress,state);
            }
            
            
            
        }
    }
    
    public static void getFlows(JSONObject Summary) throws JSONException {
        /* 
         LOGICA PARA SACAR LA LISTA DE SWITCHES QUE CONTIENEN FLUJOS Y LOS FLUJOS
         INSTALADOS EN CADA SWITCH Y SUS VARIABLES PARA LA BASE DE DATOS
         QUEDA PENDIENTE GUARDAR ESTOS DATOS EN LA BASE DE DATOS
        */
        
        //se crea un arreglo con los switches conectados para ser recorrido
        System.out.println("Lista de switches con flujos: " + Summary.names());
        JSONArray AS = Summary.names();
        
        for(int i=0; i < Summary.length(); i++){
        //se obtiene el objeto json de los flujos que pertenece a cada switch
            JSONObject s = Summary.getJSONObject(AS.getString(i));
        
        //se crea un arreglo con los nombres de los flujos intalados,para 
        //ser recorridos en el ciclo.
            JSONArray arr = s.names();
            for(int j = 0; j < s.length(); j++){
                int priority, InputPort, version;
                
        //se obtiene el objeto json que tiene las caracteristicas del flujo actual
                JSONObject t = s.getJSONObject(arr.getString(j));
                
                System.out.println("\nCaracteristicas del flujo: " + arr.getString(j));
                
                priority = t.getInt("priority");
                System.out.println("Prioridad: " + priority);
                
                //se obtiene el objeto json con los match's de los flujos
                JSONObject match = t.getJSONObject("match");
                
                InputPort = match.getInt("inputPort");
                System.out.println("Puerto de entrada: " + InputPort);
                
                //se obtiene el arreglo json con los action's de los flujos
                JSONArray action = t.getJSONArray("actions");
                for(int y = 0; y < action.length(); y++){
                    int OutputPort;
                    String accion;
                    //se obtiene el objeto json con los actions del flujo actual
                    JSONObject actions = action.getJSONObject(y);
                    
                    OutputPort = actions.getInt("port");
                    accion = actions.getString("type");
                    
                    System.out.println("Accion a ejecutar: " + accion);
                    System.out.println("Puerto a utilizar: " + OutputPort);
                }
                
                version = t.getInt("version");
                System.out.println("Version: " + version);
               
            }
        } 
    }
}
