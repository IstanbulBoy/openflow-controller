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
        String IP = "10.0.0.234";
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
        Overview.getSwitches(Switches);

       
        // test flows summary per switch
        
        JSONObject Summary;
        
        
        //Summary = Deserializer.readJsonObjectFromURL("http://" + IP
	//			+ ":" + PORT + "/wm/staticflowentrypusher/list/all/json ");
        
        
        //Overview.getFlows(Summary);
        
        // PRUEBAS CON LA BASE DE DATOS
        //Overview.getSwitches(Switches);
        DBManager dbm = new DBManager();
        //dbm.InsertSwitches("00:00:00:01","/127.0.0.1:4545","456789","Mininet","VirtualBox","Nicira","none","none");
        dbm.SelectSwitches();
    }
}
