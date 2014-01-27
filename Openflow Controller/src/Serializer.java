import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
    Serializer.java
    Esta clase actualmente solo tiene un metodo (installFlow) que recibe
    una URL enf orma de String y un String que se genera a partir de un
    ojeto JSON que posee los parametros para configurar el flujo.
*/

/**
 *
 * @author fausto
 */
public class Serializer {
  
    public static void installFlow(final String surl, final String flow) {
        try {
            URL url = new URL(surl);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
           
            OutputStream stream = connection.getOutputStream();
            stream.write(flow.getBytes());
            stream.close();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str = br.readLine();
            
            while(str!=null) {
                sb.append(str);
                str = br.readLine();
            }
            
            System.out.println("Response" + sb);
        } catch (IOException e) {
            System.out.println("Failed to deserialize data" + " : installFlow");
            e.printStackTrace();
        }
    }
    
    public static void deleteFlow(final String surl, final String flowname) {
        try {
            URL url = new URL(surl);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
           
            OutputStream stream = connection.getOutputStream();
            stream.write(flowname.getBytes());
            stream.close();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str = br.readLine();
            
            while(str!=null) {
                sb.append(str);
                str = br.readLine();
            }
            
            System.out.println("Response" + sb);
        } catch (IOException e) {
            System.out.println("Failed to deserialize data" + " : deleteFlow");
            e.printStackTrace();
        }
    }
}
