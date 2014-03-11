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
    
    public static void getSwitches(JSONArray Switches) 
            throws JSONException, IOException, SQLException {
        /* 
         * LOGICA PARA SACAR LOS OBJETOS JSON DE CADA SWITCH CONECTADO 
         * A FLOODLIGHT DE ACUERDO A LA LONGITUD DEL ARREGLO DE SWITCHES 
         * SE SACAN EL NUMERO DE SWITCHES
         */
        DBManager dbm = new DBManager();
        
        for(int i = 0; i < Switches.length(); i++) {
            String dpid = null,inetAddress = null, software = null,
                    hardware = null, manufacturer = null, serialNum = null,
                    datapath = null;
            int connectedSince;
            JSONObject sw = Switches.getJSONObject(i);
            JSONObject description = sw.getJSONObject("description");
            JSONArray ports = sw.getJSONArray("ports");
            
            dpid = sw.getString("dpid");
            inetAddress = sw.getString("inetAddress");
            connectedSince = sw.getInt("connectedSince");
            software = description.getString("software");
            hardware = description.getString("hardware");
            manufacturer = description.getString("manufacturer");
            serialNum = description.getString("serialNum");
            datapath = description.getString("datapath");
            
            dbm.InsertSwitch(i,dpid,inetAddress,software,hardware,manufacturer,
                    serialNum,datapath,connectedSince);
            
            for(int j = 0; j < ports.length(); j++) {
                int portNumber,state;
                String name = null, hardwareAddress = null;
                JSONObject port = ports.getJSONObject(j);
                
                name = port.getString("name");
                hardwareAddress = port.getString("hardwareAddress");
                portNumber = port.getInt("portNumber");
                state = port.getInt("state");
          
                dbm.InsertPort(j,i,portNumber,name,hardwareAddress,state);
            }
        }
    }
    
    public static void getFlows(JSONObject Summary) 
            throws JSONException, SQLException, IOException {
        /* 
         * LOGICA PARA SACAR LA LISTA DE SWITCHES QUE CONTIENEN FLUJOS Y 
         * LOS FLUJOS INSTALADOS EN CADA SWITCH Y SUS VARIABLES PARA 
         * LA BASE DE DATOS
         */
        
        JSONArray AS = Summary.names();                                         // Arreglo de Switches conectados                                 
        
        for(int i = 0; i < Summary.length(); i++) {
            JSONObject s = Summary.getJSONObject(AS.getString(i));              // Se obtiene el JSONObject de los flujos que pertenece a cada switch
            JSONArray arr = s.names();                                          // Arreglo con los nombres de los flujos para ser recorrido
            
            for(int j = 0; j < s.length(); j++) {               
                JSONObject t = s.getJSONObject(arr.getString(j));               // Se obtiene el JSONObject con los datos del flujo actual
                JSONObject match = t.getJSONObject("match");                    // Se obtiene el JSONObject con los matchs del flujo actual
                int priority = t.getInt("priority");                            // Se obtiene la prioridad del flujo actual
                int InputPort = match.getInt("inputPort");                      // Se obtiene el inputPort del flujo actual
                JSONArray action = t.getJSONArray("actions");                   // Se obtiene el JSONArray con los actions del flujo actual
                JSONObject actions = action.getJSONObject(0);                   // Se obtiene el JSONObject con el action del flujo actual
                int OutputPort = actions.getInt("port");                        // Se obtiene el outputPort del flujo actual
                String accion = actions.getString("type");                      // Se obtiene el tipo de la accion del flujo actual
             
                DBManager dbm = new DBManager();
                String sname = arr.getString(j);              
                int switchh = dbm.getIndex(AS.getString(i));
                dbm.InsertFlow(j,i,sname,priority,InputPort,accion,OutputPort);
            }
        }
    }

    
    // PENDIENTE POR IDENTAR Y COMENTAR
    public static void getPortStatistics(JSONObject PortSummary) 
            throws JSONException {
        JSONArray PortNames;
        PortNames = PortSummary.names();
        
        for(int i = 0; i < PortSummary.length(); i++){
            //se obtiene el arreglo que contiene las caracteristicas 
            //del swich actual
            JSONArray pri = PortSummary.getJSONArray(PortNames.getString(i));
            
            for(int j = 0; j < pri.length(); j++){
                System.out.println("\n estadisticas generales del swich " + 
                        PortNames.getString(i));
                
                //se obtiene el objeto JSON con las estadisticas de cada puerto
                JSONObject pre = pri.getJSONObject(j);
                int portNumber,receiveBytes,collisions,transmitBytes;
                int transmitPackets,receivePackets;
                
                portNumber = pre.getInt("portNumber");
                receiveBytes = pre.getInt("receiveBytes");
                collisions = pre.getInt("collisions");
                transmitBytes = pre.getInt("transmitBytes");
                transmitPackets = pre.getInt("transmitPackets");
                receivePackets = pre.getInt("receivePackets");
                
                
                // SE DEBE VERIFICAR UNA VARIABLE 'DATE' PARA ENVIAR A LA BASE DE DATOS Y QUE SEA EL EJE X DE UNA POSIBLE GRAFICA
                //DBManager dbm = new DBManager();
                //String sname = arr.getString(j);              
                //int switchh = dbm.getIndex(AS.getString(i));
                //dbm.InsertPortStatistics(j,i,portNumber,receiveBytes,collisions,transmitBytes,transmitPackets,receivePackets);
                
                //System.out.println("portNumber: " + portNumber);
                //System.out.println("receiveBytes: " + receiveBytes);
                //System.out.println("collisions: " + collisions);
                //System.out.println("transmitBytes: " + transmitBytes);
                //System.out.println("transmitPackets: " + transmitPackets);
                //System.out.println("receivePackets: " + receivePackets);
            }
        }
    }
    
    public static void getFlowStatistics(JSONObject FlowSummary) 
            throws JSONException {
        JSONArray SFlowNames = FlowSummary.names();
        //se obtiene el arreglo de equipos que tienen flujos activos

        for(int i = 0; i < SFlowNames.length(); i++) {
            //se obtienen las estadisticas de los flujos del switch actual
            JSONArray FlowStatistics = 
                    FlowSummary.getJSONArray(SFlowNames.getString(i));
            
            for(int j = 0; j < FlowStatistics.length(); j++) {
                JSONObject FlowMatch;
                int packetCount, byteCount, FlowMatchPort,durationSeconds;
                
                //se obtiene las estadisticas del flujos actual
                JSONObject flow = FlowStatistics.getJSONObject(j);
                packetCount = flow.getInt("packetCount");
                byteCount = flow.getInt("byteCount");
                durationSeconds =flow.getInt("durationSeconds");
                
                //las estadisticas floodlight las organiza de acuerdo a como 
                //se instalaron
                //los flujos,asi mismo actualmente se determina a cual 
                //flujo pertenece
                //usando los if.
                
                //CUANDO SE INSERTE TODO EN LA DB, ESTA PARTE NO SERA NECESARIA
                //PORQUE PARA SABER EL FLUJO AL QUE PERTENECE, SOLO SE HARIAN
                //COMPARACION DE LOS SELECTS DE LA TABLAS FLUJOS Y ESTADISTICAS 
                //POR FLUJOS
                //COMPARANDO INPUTPORT,OUTPUTPORT Y LA ACCION A EJECUTAR
                //if(j==0) {
                //    System.out.println("\n estadisticas especificas del "
                //            + "flujo 1");
                //} else {
                //    System.out.println("\n estadisticas especificas del "
                //            + "flujo 2");
                //}
                
                System.out.println("Estadisticas especificas del flujo: " + j + "\n");
                System.out.println("packetCount: " + packetCount);
                System.out.println("byteCount: " + byteCount);
                System.out.println("durationSeconds: " + durationSeconds);
                
                FlowMatch = flow.getJSONObject("match");
                
                FlowMatchPort = FlowMatch.getInt("inputPort");
                System.out.println("puerto de entrada: " + FlowMatchPort);
                
                JSONArray FlowActions;
                
                //se obtiene el arreglos de acciones que tiene el flujo actual
                FlowActions = flow.getJSONArray("actions");
                int FlowOutport;
                String FlowType;
                JSONObject FActions;
                    
                //se obtiene la accion correspondiente al flujo
                FActions = FlowActions.getJSONObject(0);
                FlowOutport = FActions.getInt("port");
                FlowType = FActions.getString("type");
                System.out.println("accion a ejecutar: " + FlowType);
                System.out.println("puerto de salida: " + FlowOutport + "\n");
            }
        }
    }
}