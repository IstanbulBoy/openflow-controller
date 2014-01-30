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
        String IP = "10.0.0.232";
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
        
        //Serializer.deleteFlow(flowname1);
        //Serializer.deleteFlow(flowname2);
        //System.out.println(Switches);
        System.out.println(Switches.length());
        //System.out.println(Switches.getJSONObject(0).toString());
        
        
        // LOGICA PARA SACAR LOS OBJETOS JSON DE CADA SWITCH CONECTADO A FLOODLIGHT
        // DE ACUERDO A LA LONGITUD DEL ARREGLO DE SWITCHES SE SACAN EL NUMERO DE SWITCHES
        // QUEDA PENDIENTE HACER LA LOGICA PARA SACAR LAS VARIABLES DE CADA SWITCH
        // SE ENTIENDE QUE ESA LOGICA SE DEBE APLICAR DENTRO DEL LOOP FOR
        String dpid = null;
        for(int i = 0; i < Switches.length(); i++) {
            JSONObject sw = Switches.getJSONObject(i);
            System.out.println(sw);
            dpid = sw.getString("dpid").toString();
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
