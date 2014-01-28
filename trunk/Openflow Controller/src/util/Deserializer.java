package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
    Deserializer.java
    Esta clase cuenta con 2 metodos (readJsonArrayFromURL y readJsonObjectFromURL)
    que reciben una URL en forma de String.
    Se utilizan para establecer la conexion con Floodlight y obtener
    los objetos JSON que Floodlight envia de acuerdo al URL solicitado.

    Los JSON se leen en forma de Object o Array porque Floodlight utiliza
    ambas formas para enviar los objetos, de acuerdo al URL solicitado.
*/

/**
 *
 * @author fausto
 */
public class Deserializer {
    //Posible codigo para iniciar a trabajar con hilos, pendiente de revision.
    //private final static int THREADS = Runtime.getRuntime().availableProcessors();
    //public static ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1 ) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    public static JSONArray readJsonArrayFromURL(final String surl) {
        try {
            URL url = new URL(surl);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            is.close();
            return new JSONArray(jsonText);
        } catch (IOException e) {
            System.out.println("Failed to deserialize data" + " : readJsonObjectFromURL");
            e.printStackTrace();
        } catch (JSONException ex) {
            Logger.getLogger(Deserializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static JSONObject readJsonObjectFromURL(final String surl) {
        try {
            URL url = new URL(surl);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
                    
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            is.close();
            return new JSONObject(jsonText);
        } catch (IOException e) {
            System.out.println("Failed to deserialize data" + " : readJsonObjectFromURL");
            e.printStackTrace();
        } catch (JSONException ex) {
            Logger.getLogger(Deserializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}