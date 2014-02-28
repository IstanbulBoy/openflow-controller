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
    public static void main(String[] args) throws JSONException {
        String IP = "192.168.56.101";
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
        Overview.getSwitches(Switches);

       
        // test flows summary per switch
        
        JSONObject Summary;
        
        
        Summary = Deserializer.readJsonObjectFromURL("http://" + IP
				+ ":" + PORT + "/wm/staticflowentrypusher/list/all/json ");
        
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
