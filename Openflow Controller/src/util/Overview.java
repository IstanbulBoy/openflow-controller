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
            
            //dbm.InsertSwitch(i,dpid,inetAddress,software,hardware,manufacturer,serialNum,datapath,connectedSince);
            // LOS UPSERT SE HACEN COMO SI FUERAN UN SELECT. PORQUE SE LLAMA UNA FUNCION EN LA BASE DE DATOS. CODIGO DE PUREBA.
            dbm.InsertSwitch(0,dpid,inetAddress,software,hardware,manufacturer,"00004","0003",connectedSince);
            dbm.InsertSwitch(1, "00:00:00:03", inetAddress, software, hardware, manufacturer, serialNum, datapath, connectedSince);
            dbm.InsertSwitch(2, "00:00:00:04", inetAddress, software, hardware, manufacturer, serialNum, datapath, connectedSince);
      // SE DEBE CREAR LA FUNCION PARA EL UPSERT DE LOS PUERTOS Y PARA EL UPSERT DE LOS FLUJOS.
            //LA FUNCION ACTUAL SOLO ES PARA LOS SWITCHES.
  /*          
            for(int j = 0; j < ports.length(); j++) {
                int portNumber,state;
                String name = null, hardwareAddress = null;
                JSONObject port = ports.getJSONObject(j);
                
                name = port.getString("name");
                hardwareAddress = port.getString("hardwareAddress");
                portNumber = port.getInt("portNumber");
                state = port.getInt("state");
          
                dbm.InsertPort(j,i,portNumber,name,hardwareAddress,state);
            } */
        } 
    }
    
    public static void getFlows(JSONObject Summary) throws JSONException, SQLException, IOException {
        /* 
         LOGICA PARA SACAR LA LISTA DE SWITCHES QUE CONTIENEN FLUJOS Y LOS FLUJOS
         INSTALADOS EN CADA SWITCH Y SUS VARIABLES PARA LA BASE DE DATOS
        */
        
        //Se crea un arreglo con los switches conectados para ser recorrido
        JSONArray AS = Summary.names();
        
        for(int i=0; i < Summary.length(); i++) {
        //se obtiene el objeto json de los flujos que pertenece a cada switch
            JSONObject s = Summary.getJSONObject(AS.getString(i));
        
        //se crea un arreglo con los nombres de los flujos intalados, para 
        //ser recorridos en el ciclo.
            JSONArray arr = s.names();
            for(int j = 0; j < s.length(); j++) {
                int priority, InputPort;
                
        //se obtiene el objeto json que tiene las caracteristicas del flujo actual
                JSONObject t = s.getJSONObject(arr.getString(j)); 
                priority = t.getInt("priority");

                //se obtiene el objeto json con los match's de los flujos
                JSONObject match = t.getJSONObject("match");
                InputPort = match.getInt("inputPort");

                //se obtiene el arreglo json con los action's de los flujos
                JSONArray action = t.getJSONArray("actions");
                int OutputPort;
                String accion;
                //se obtiene el objeto json con los actions del flujo actual
                JSONObject actions = action.getJSONObject(0);
                OutputPort = actions.getInt("port");
                accion = actions.getString("type");

                //System.out.println(AS.getString(i));
                DBManager dbm = new DBManager();
                String sname = arr.getString(j);
                int switchh = dbm.getIndex(AS.getString(i));
                dbm.InsertFlow(j,i,sname,priority,InputPort,accion,OutputPort);
            }
        }
    }

    public static void getPortStatistics(JSONObject PortSummary) throws JSONException {
        JSONArray PortNames;
        PortNames = PortSummary.names();
        
        for(int i = 0; i < PortSummary.length(); i++){
            //se obtiene el arreglo que contiene las caracteristicas del swich actual
            JSONArray pri = PortSummary.getJSONArray(PortNames.getString(i));
            
            for(int j = 0; j < pri.length(); j++){
                System.out.println("\n estadisticas generales del swich " + PortNames.getString(i));
                
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
                
                System.out.println("portNumber: " + portNumber);
                System.out.println("receiveBytes: " + receiveBytes);
                System.out.println("collisions: " + collisions);
                System.out.println("transmitBytes: " + transmitBytes);
                System.out.println("transmitPackets: " + transmitPackets);
                System.out.println("receivePackets: " + receivePackets);
            }
        }
    }
    
    public static void getFlowStatistics(JSONObject FlowSummary) throws JSONException {
        JSONArray SFlowNames;
        //se obtiene el arreglo de equipos que tienen flujos activos
        SFlowNames = FlowSummary.names();

        for(int i = 0; i < SFlowNames.length(); i++) {
            //se obtienen las estadisticas de los flujos del switch actual
            JSONArray FlowStatistics = FlowSummary.getJSONArray(SFlowNames.getString(i));
            
            for(int j = 0; j < FlowStatistics.length(); j++) {
                JSONObject flow,FlowMatch;
                int packetCount, byteCount, FlowMatchPort,durationSeconds;
                
                //se obtiene las estadisticas del flujos actual
                flow = FlowStatistics.getJSONObject(j);
                packetCount = flow.getInt("packetCount");
                byteCount = flow.getInt("byteCount");
                durationSeconds =flow.getInt("durationSeconds");
                
                //las estadisticas floodlight las organiza de acuerdo a como se instalaron
                //los flujos,asi mismo actualmente se determina a cual flujo pertenece
                //usando los if.
                
                //CUANDO SE INSERTE TODO EN LA DB, ESTA PARTE NO SERA NECESARIA
                //PORQUE PARA SABER EL FLUJO AL QUE PERTENECE, SOLO SE HARIAN
                //COMPARACION DE LOS SELECTS DE LA TABLAS FLUJOS Y ESTADISTICAS POR FLUJOS
                //COMPARANDO INPUTPORT,OUTPUTPORT Y LA ACCION A EJECUTAR
                if(j==0) {
                    System.out.println("\n estadisticas especificas del flujo 1");
                } else {
                    System.out.println("\n estadisticas especificas del flujo 2");
                }
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
                System.out.println("puerto de salida: " + FlowOutport);
            }
        }
    }
}