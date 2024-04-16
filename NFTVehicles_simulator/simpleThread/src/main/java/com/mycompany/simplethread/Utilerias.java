/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author clopezp
 */
public class Utilerias {
    public enum MState{ //States of the manufacturer
        NOTHING, TOKENIZING, MINTING, REQOWNERRIGHT; 
    }
    
    public static String getState(MState s){
        String state;
        state = switch (s) {
            case NOTHING -> "NOTHING";            
            case TOKENIZING -> "TOKENIZING";
            case MINTING -> "MINTING";                
            case REQOWNERRIGHT -> "REQOWNERRIGHT";
            default -> "NOT DEFINED";
        };
        return state;
    }

    public enum TranType{//type of transactions
        CHANGE_OWNERRIGHTS, ENDLIFECYCLE, CHANGE_STOLEN_STATUS;
        //CHANGE_OWNERRIGHTS = 100; ENDLIFECYCLE = 300; CHANGE_STOLEN_STATUS = 101;
    }
    
    public static String getTranType(TranType s){
        String t;
        t = switch (s) {
            case CHANGE_OWNERRIGHTS -> "100";            
            case ENDLIFECYCLE -> "300";
            case CHANGE_STOLEN_STATUS -> "101";                
            default -> "-1"; //not defined
        };
        return t;
    }

    public static String getCostTranType(TranType s){
        String t;
        t = switch (s) {
            case CHANGE_OWNERRIGHTS -> "450";            
            case ENDLIFECYCLE -> "150";
            case CHANGE_STOLEN_STATUS -> "400";                
            default -> "0"; //not defined
        };
        return t;
    }
    public static String getCostTranType(String s){
        String t;
        t = switch (s) {
            case "100" -> "450";            
            case "300" -> "150";
            case "101" -> "400";                
            default -> "0"; //not defined
        };
        return t;
    }
    
    
    public void espera(int m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException ex) {
            Logger.getLogger(Utilerias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String connect(String[] keys, String[] values, String service){
            URL url;
            HttpURLConnection conn= null;
            String returnMsg=""; 
            try {
                url = new URL("http://localhost:6000/"+service);                
                try {                    
                    conn = (HttpURLConnection) url.openConnection();                    
                    conn.setRequestMethod("POST"); 
                    String nonce = Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                    conn.setRequestProperty("X-Nonce", nonce);                    
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);        
                    String jsonMsg="",inicio = "\"";        
                    for(int i=0; i<keys.length;i++){
                        jsonMsg = jsonMsg + inicio + keys[i] + "\":\"" + values[i] + "\"";
                        inicio = ",\"";
                    }
                    jsonMsg = "{" + jsonMsg + "}";
                    String requestBody = jsonMsg;
                    OutputStream os = conn.getOutputStream();
                    os.write(requestBody.getBytes());
                    os.flush();                    
                    int responseCode = conn.getResponseCode();
                    String threadName = Thread.currentThread().getName();
                    //System.out.println(threadName + " Response code: " + responseCode + "\n Manuf: " + values[5]);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                        String output;

                        while ((output = br.readLine()) != null) {             
                             returnMsg = returnMsg + output;
                        }
                        br.close();
                        conn.disconnect();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ManufacturerRole.class.getName()).log(Level.SEVERE, null, ex);
                    returnMsg="Error"; // if not error, this variable must change                    
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(ManufacturerRole.class.getName()).log(Level.SEVERE, null, ex);
                returnMsg="Error"; // if not error, this variable must change
            }
            //conn.disconnect();
        return returnMsg;
    }
    
}
