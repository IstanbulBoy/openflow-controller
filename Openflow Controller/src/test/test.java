package test;

import util.Deserializer;
import floodlightprovider.FloodlightProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
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
    public static void main(String[] args) throws JSONException {
        String IP = "10.0.0.235";
        String PORT = "8080";
        FloodlightProvider.setIP(IP);       // Set IP en FloodlightProvider
        FloodlightProvider.setPort(PORT);   // Set PORT en FloodlightProvider
        JSONArray Switches;                 // Crea arreglo JSON
        JSONObject Health,Memory,Modules;   // Crea objetos JSON
        
        // Se envian los URL a Deserializer y se almacena la respuesta en las
        // variables creadas anteriormente.
        Health = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/core/health/json");
        Memory = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/core/memory/json");
	Modules = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/core/module/loaded/json");
        Switches = Deserializer.readJsonArrayFromURL("http://" + IP
				+ ":" + PORT + "/wm/core/controller/switches/json");
        
        // FLOW SET UP. 
        // Primero se crean los objetos JSON para configurar los flujos.
        // Luego se convierten en String.
        // Luego se envian los flujos con su respectiva URL a Serializer
        String flow1 = new JSONStringer().object()
                .key("switch").value("00:00:00:00:00:00:00:01")
                .key("name").value("flow-mod-1")
                .key("cookie").value("0")
                .key("priority").value("32768")
                .key("ingress-port").value("1")
                .key("active").value("true")
                .key("actions").value("output=2")
                .endObject().toString();
        
        String flow2 = new JSONStringer().object()
                .key("switch").value("00:00:00:00:00:00:00:01")
                .key("name").value("flow-mod-2")
                .key("cookie").value("0")
                .key("priority").value("32768")
                .key("ingress-port").value("2")
                .key("active").value("true")
                .key("actions").value("output=1")
                .endObject().toString();
        
        //Serializer.installFlow(flow1);
        //Serializer.installFlow(flow2);
             
        // DELETE FLOW TEST
        String flowname1 = new JSONStringer().object()
                .key("name").value("flow-mod-1")
                .endObject().toString();
        String flowname2 = new JSONStringer().object()
                .key("name").value("flow-mod-2")
                .endObject().toString();
        
        Serializer.deleteFlow(flowname1);
        Serializer.deleteFlow(flowname2);
        
        //System.out.println(Switches);
        //System.out.println(Switches.length());
        //System.out.println(Switches.getJSONObject(0).toString());
        
        
        // LOGICA PARA SACAR LOS OBJETOS JSON DE CADA SWITCH CONECTADO A FLOODLIGHT
        // DE ACUERDO A LA LONGITUD DEL ARREGLO DE SWITCHES SE SACAN EL NUMERO DE SWITCHES
        // QUEDA PENDIENTE HACER LA LOGICA PARA SACAR LAS VARIABLES DE CADA SWITCH
        // SE ENTIENDE QUE ESA LOGICA SE DEBE APLICAR DENTRO DEL LOOP FOR
        
        
        for(int i = 0; i < Switches.length(); i++) {
            String dpid = null,harole = null,inetAddress = null;
            int connectedSince,buffers,capabilities,actions;
            
            JSONObject sw = Switches.getJSONObject(i);
            //System.out.println(sw);
            
            dpid = sw.getString("dpid");
            harole = sw.getString("harole");
            inetAddress = sw.getString("inetAddress");
            connectedSince = sw.getInt("connectedSince");
            buffers = sw.getInt("buffers");
            capabilities = sw.getInt("capabilities");
            actions = sw.getInt("actions");
            
            
            System.out.println("Information about switch with DPID: " + dpid);
            System.out.println("IP Address: " + inetAddress);
            
            
            JSONArray ports = sw.getJSONArray("ports");
            //System.out.println(ports.length());
            for(int j = 0; j < ports.length(); j++) {
                int currentFeatures,portNumber,supportedFeatures,state,config,
                        advertisedFeatures,peerFeatures;
                String name = null, hardwareAddress = null;
                JSONObject port = ports.getJSONObject(j);
                //System.out.println(port);
                
                name = port.getString("name");
                hardwareAddress = port.getString("hardwareAddress");
                currentFeatures = port.getInt("currentFeatures");
                portNumber = port.getInt("portNumber");
                supportedFeatures = port.getInt("supportedFeatures");
                state = port.getInt("state");
                config = port.getInt("config");
                advertisedFeatures = port.getInt("advertisedFeatures");
                peerFeatures = port.getInt("peerFeatures");
                
                System.out.println("");
                System.out.println("Name: " + name);
                System.out.println("MAC: " + hardwareAddress);
                System.out.println("Port Number: " + portNumber);
                System.out.println("State: " + state);
                //System.out.println("");
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

        // JSON EXTRACK OBJETS TEST
        //Map<String,String> map = new HashMap<String,String>();
       // Map<String,String> map = new HashMap<String,String>();
       // while(iter.hasNext()){
       //     String key = (String)iter.next();
        //    String value = Switches.getString(key);
        //    map.put(key,value);    
        
        
        
        
        // test flows summary per switch
        
        //JSONObject Summary;
        
        
        //Summary = Deserializer.readJsonObjectFromURL("http://" + IP
	//			+ ":" + PORT + "/wm/staticflowentrypusher/list/" + dpid + "/json ");
        //System.out.println(Summary);
        
    }
}
