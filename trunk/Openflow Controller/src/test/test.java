package test;

import util.Deserializer;
import floodlightprovider.FloodlightProvider;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import util.DBManager;
import util.JSONManager;
import util.Overview;
import util.Serializer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
    test.java
    Archivo main de prueba para verificar el correcto funcionamiento de los 
    metodos hasta la fecha (Deserializer.java, FloodlightProvider.java y
    Serializer.java)
*/

/**
 *
 * @author fausto
 */
public class test {
    public static void main(String[] args) throws JSONException, IOException, SQLException {
        String IP = "192.168.56.101";
        String PORT = "8080";
        FloodlightProvider.setIP(IP);       // Set IP en FloodlightProvider
        FloodlightProvider.setPort(PORT);   // Set PORT en FloodlightProvider
        JSONArray Switches;                 // Crea arreglo JSON
        JSONObject Health,Memory,Modules;   // Crea objetos JSON
        
        // Se envian los URL a Deserializer y se almacena la respuesta en las
        // variables creadas anteriormente.
        //Health = Deserializer.readJsonObjectFromURL("http://" + IP
	//			+ ":" + PORT + "/wm/core/health/json");
        //Memory = Deserializer.readJsonObjectFromURL("http://" + IP
	//			+ ":" + PORT + "/wm/core/memory/json");
	//Modules = Deserializer.readJsonObjectFromURL("http://" + IP
	//			+ ":" + PORT + "/wm/core/module/loaded/json");
        Switches = Deserializer.readJsonArrayFromURL("http://" + IP
				+ ":" + PORT + "/wm/core/controller/switches/json");
        
        // CREACION DE FLUJOS E INSTALACION
        String flow1 = JSONManager.createFlow("00:00:00:00:00:00:00:01", 
                "flow-mod-1", "0", "32768", "1", "true", "2");
          
        String flow2 = JSONManager.createFlow("00:00:00:00:00:00:00:01", 
                "flow-mod-2", "0", "32768", "2", "true", "1");
        
        //Serializer.installFlow(flow1);
        //Serializer.installFlow(flow2);

        // CREACION DE OBJETO JSON PARA BORRAR FLUJO POR NOMBRE Y BORRADO
        String del1 = JSONManager.deleteRequest("flow-mod-1");
        String del2 = JSONManager.deleteRequest("flow-mod-2");
        
        //Serializer.deleteFlow(del1);
        //Serializer.deleteFlow(del2);
        
        // SACAR TODAS LAS VARIABLES DE LOS SWITCHES CONECTADOS
        //Overview.getSwitches(Switches);

       
        // test flows summary per switch
        
        JSONObject Summary;
        
        
        Summary = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/staticflowentrypusher/list/all/json ");
        
        
        //Overview.getFlows(Summary);
        
        // PRUEBAS CON LA BASE DE DATOS
        //Overview.getSwitches(Switches);
        //DBManager dbm = new DBManager();
        //dbm.InsertSwitches("00:00:00:01","/127.0.0.1:4545","456789","Mininet","VirtualBox","Nicira","none","none");
        //dbm.SelectSwitches();
        
        
        
        
        //aqui va el codigo para sacar las estadisticas por puertos de forma general
        
        JSONObject PortSummary;
        
        PortSummary = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/core/switch/all/port/json  ");
        JSONArray PortNames;
        PortNames = PortSummary.names();
        
        
        for(int i = 0; i < PortSummary.length(); i++){
            

            //se obtiene el arreglo que contiene las caracteristicas del swich actual
            JSONArray pri = PortSummary.getJSONArray(PortNames.getString(i));
            
            //System.out.println(pri.length());
            
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
        
        
        //aqui va el codigo para sacar las estadisticas por flujos
        JSONObject FlowSummary;
        FlowSummary = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/core/switch/all/flow/json  ");
        
        System.out.println(FlowSummary);
        
        
        JSONArray SFlowNames;
        //se obtiene el arreglo de equipos que tienen flujos activos
        SFlowNames = FlowSummary.names();
        
        
        
        for(int i = 0; i < SFlowNames.length(); i++){
            
            //se obtienen las estadisticas de los flujos del switch actual
            JSONArray FlowStatistics = FlowSummary.getJSONArray(SFlowNames.getString(i));
            
            
            for(int j = 0; j < FlowStatistics.length(); j++){
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
                if(j==0){
                    System.out.println("\n estadisticas especificas del flujo 1");
                }else{
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
