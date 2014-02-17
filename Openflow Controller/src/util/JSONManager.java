/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 *
 * @author cecomsaguraboast
 */
public class JSONManager {
    
    public static String createFlow(String dpid, String name, String cookie, 
            String priority, String ingressPort, String active, String outport) 
            throws JSONException {
        /*
                Primero se crean los objetos JSON para configurar los flujos.
                Luego se convierten en String.
        */
        String flow = new JSONStringer().object()
                .key("switch").value(dpid)
                .key("name").value(name)
                .key("cookie").value(cookie)
                .key("priority").value(priority)
                .key("ingress-port").value(ingressPort)
                .key("active").value(active)
                .key("actions").value("output=" + outport)
                .endObject().toString();
       
        return flow;
    }
    
    public static String deleteRequest(String name) throws JSONException {
        String flowname = new JSONStringer().object()
                .key("name").value(name)
                .endObject().toString();
        return flowname;
    }
}
