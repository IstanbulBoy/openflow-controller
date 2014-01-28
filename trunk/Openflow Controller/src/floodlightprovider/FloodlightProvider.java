package floodlightprovider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
    FloodlightProvider.java
    Esta clase sirve de estructura para almacenar y setear
    la IP y el PORT.
*/

/**
 *
 * @author fausto
 */

public class FloodlightProvider {
    private static String IP, PORT = "8080", 
            STORELINK = "/wm/staticflowentrypusher/json/store",
            DELETELINK = "/wm/staticflowentrypusher/json/delete";
    
    public static String getIP(){
        return IP;
    }
    
    public static void setIP(String IPAddress){
    	IP = IPAddress; 
    }
    
    public static String getPort(){
    	return PORT;
    }
    
    public static void setPort(String ControllerPort){
    	PORT = ControllerPort;
    }
    
    public static void setStoreLink(String StoreURL) {
        STORELINK = StoreURL;
    }
    
    public static String getStoreLink() {
        return STORELINK;
    }
    
    public static void setDeleteLink(String StoreURL) {
        STORELINK = StoreURL;
    }
    
    public static String getDeleteLink() {
        return DELETELINK;
    }
}