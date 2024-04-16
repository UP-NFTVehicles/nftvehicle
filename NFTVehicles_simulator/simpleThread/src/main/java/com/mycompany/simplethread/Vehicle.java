/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;
import java.awt.Color;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author clopezp
 */
public class Vehicle {
    public int posX, posY;
    public Color color;
    private final Random rand= new Random();
    private String[] initializedKeys;
    private String[] initializedValues;
    private String tokenId;
    private JSONObject jsonResult;
    private JSONObject jresultMinted;
    private JSONObject jresultReqOwnerRights;    
    
    private boolean tokenized;
    private boolean minted;
    private boolean requestedOwnerRights = false;    
    
    private String name;    
    private String manufacturerKey="";
    private String governmentKey="";
    //private Utilerias util;
    private String error;
    
    
    
    

    Vehicle(String Manufacturerkey){ //When used this Constructor, the car is not tokenized yet
        tokenized = false;
        //rand = new Random();         
        manufacturerKey = Manufacturerkey;
        initializeKeysValues();
        //util = new Utilerias();
    }

    Vehicle(JSONObject jsonR){//When used this Constructor, the car is already tokenized
        tokenized = true;
        jsonResult = jsonR;
        manufacturerKey = getCharacteristicValue("manufacturer");        
        minted = (getCharacteristicValue("government").equals("0x0000000000000000000000000000000000000000")?false:true);
        name = getCharacteristicValue("name");
        //util = new Utilerias();
    }

    private void initializeKeysValues(){
        initializedKeys   = new String[6];
        initializedValues = new String[6];
        initializedKeys[0] = "gas";
        initializedValues[0] = "3600000";        
        initializedKeys[1] = "name";
        name = "CX" + rand.nextInt(10) + " Edition" + rand.nextInt(1000);
        initializedValues[1] = name;
        initializedKeys[2] = "symbol";
        initializedValues[2] = "GMCX" + rand.nextInt(10);
        initializedKeys[3] = "token";
        initializedValues[3] = "1FMADSFJL2432SDKFJA"; //al parecer este token debe ser fijo por lo pronto
        initializedKeys[4] = "data";
        initializedValues[4] = "{owrnername':'UPGDL'; 'colour':'white'}";
        initializedKeys[5] = "from";    
        initializedValues[5] = manufacturerKey;
    }
    
    public String[] getInitializedKeys(){
        return initializedKeys;
    }
    
    public String getManufacturerKey(){
        return manufacturerKey;
    }
    
    public String[] getInitializedValues(){
        return initializedValues;
    }
    
    public void setTokenized(JSONObject jresult){
        if(!tokenized){
            jsonResult = jresult;            
            String manuf = getManufacturerKey().toUpperCase();//getCharacteristicValue("manufacturer").toUpperCase();            
            if(!manuf.equals(manufacturerKey.toUpperCase())){
                System.out.println("Hay algo extraño acá: manufacturers no coinciden");
                System.out.println("Estaba:" + manufacturerKey);
                System.out.println("Nuevo:" + manuf);
                manufacturerKey = manuf;
            }
            color = Color.BLUE;
            tokenized = true;
        }
    }
    
    public boolean isTokenized(){
        return tokenized;
    }

    public JSONObject getJSONResultTokenized(){
        return jsonResult;
    }
    
    private String getStringFromJSONObject(String key){
        String result = "";
        try {
            result = jsonResult.getString(key);
        } catch (JSONException ex) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;    
    }
 
    public String getTransactionHash(){
        return getStringFromJSONObject("transactionHash");
    }

    public String getContractAddress(){
        return getStringFromJSONObject("contractAddress");
    }
    public String getName(){
        return name;
    }

    public String getGasUsed(){
        return getStringFromJSONObject("gasUsed");
    }

    public String getBlockNumber(){
        return getStringFromJSONObject("blockNumber");
    }

    public String getBlockHash(){
        return getStringFromJSONObject("blockHash");
    }
    
    public String[] getKeysForMinting(){
        String[] keysForMinting = new String[5];
        keysForMinting[0] = "gas";
        keysForMinting[1] = "contractAdd";
        keysForMinting[2] = "manufacturerAdd";
        keysForMinting[3] = "governmentAdd";
        keysForMinting[4] = "tokenId";
        return keysForMinting;
    }
    
    public String[] getValuesForMinting(String goverKey){        
        String[] valuesForMinting = new String[5];
        if(tokenized){
            valuesForMinting[0] = "6700000";        
            valuesForMinting[1] = getStringFromJSONObject("contractAddress"); 
            valuesForMinting[2] = manufacturerKey;            
            valuesForMinting[3] = goverKey;
            tokenId = Integer.toString(rand.nextInt(10000));
            valuesForMinting[4] = tokenId;//   "12345" 
        }else{
            valuesForMinting[0] = "";        
            valuesForMinting[1] = "";
            valuesForMinting[2] = "";
            valuesForMinting[3] = "";
            valuesForMinting[4] = "";
        }
        return valuesForMinting;
    }

    public void setMinted(JSONObject jresultTokenized){
        try {
            Utilerias util = new Utilerias();
            String governmentString = this.getCharacteristicValue("government");
            JSONObject jsonObject = new JSONObject(governmentString);
            String result = jsonObject.getString("Result");
            if(result.equals("Success")){
                governmentKey = jsonObject.getString("Result");
                color = Color.RED;
                minted = true;            
                this.jresultMinted = jresultTokenized;
            }            
        } catch (JSONException ex) {
            Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isMinted(){
        return minted;
    }

    private String[] getKeysForConsult(String key){
        String[] keysForConsult = new String[5];
        keysForConsult[0] = "contractAdd";
        keysForConsult[1] = key;
        return keysForConsult;
    }        

    private String[] getValuesForConsult(String ca, String pm){
        String[] valuesForConsult = new String[5];
        valuesForConsult[0] = ca;
        valuesForConsult[1] = pm;
        return valuesForConsult;
    }       
    
    /**
     * It obtains the characteristics value that are stored in the blockchain and this method consults it.
     * @param characteristic it is the characteristic name
     * @return it returns the value in String form
     */
    
    public String getCharacteristicValue(String characteristic){            
        Utilerias util = new Utilerias();
        String keys[] = this.getKeysForConsult("publicMethod");
        String values[] = this.getValuesForConsult(this.getContractAddress(), characteristic);
        String service = "consultInfo";
        String answer = util.connect(keys,values,service);
        return answer;
    }

    public String getEventsValue(String event){
        Utilerias util = new Utilerias();
            String keys[] = this.getKeysForConsult("eventSC");
            String values[] = this.getValuesForConsult(this.getContractAddress(), event);
            String service = "consultEvents";
            String answer = util.connect(keys,values,service);
            return answer;
    }

    public String[] getKeysForReqOwnerRight(){
        String[] keysForReqOwnerRight = new String[4];
        keysForReqOwnerRight[0] = "gas";
        keysForReqOwnerRight[1] = "contractAdd";
        keysForReqOwnerRight[2] = "owner";        
        keysForReqOwnerRight[3] = "tokenId";
        return keysForReqOwnerRight;
    }
    
    public String[] getValuesForReqOwnerRight(String ownerKey){        
        String[] valuesForReqOwnerRight = new String[4];
        if(minted){
            valuesForReqOwnerRight[0] = "3600000";        
            valuesForReqOwnerRight[1] = getContractAddress(); 
            valuesForReqOwnerRight[2] = ownerKey;            
            valuesForReqOwnerRight[3] = tokenId;
        }else{
            valuesForReqOwnerRight[0] = "";        
            valuesForReqOwnerRight[1] = "";
            valuesForReqOwnerRight[2] = "";
            valuesForReqOwnerRight[3] = "";
        }
        return valuesForReqOwnerRight;
    }
    
    public void setRequestedOwnerRights(JSONObject jresult){
        color = Color.GREEN;
        requestedOwnerRights = true;
        this.jresultReqOwnerRights = jresult;
    }

    public boolean hasBeenReqOwnerRights(){
        return requestedOwnerRights;
    }
    
    
    public String[] getKeys4Cost4Trans(){
        String[] keys4Cost4Trans = new String[6];
        keys4Cost4Trans[0] = "gas";
        keys4Cost4Trans[1] = "contractAdd";
        keys4Cost4Trans[2] = "transactionCost";
        keys4Cost4Trans[3] = "transactionType";        
        keys4Cost4Trans[4] = "tokenId";
        keys4Cost4Trans[5] = "government";
        return keys4Cost4Trans;
    }
    
    public String[] getValues4Cost4Trans(String tc, String tt){        
        String[] values4Cost4Trans = new String[6];
        if(minted){
            values4Cost4Trans[0] = "3600000";        
            values4Cost4Trans[1] = getContractAddress(); 
            values4Cost4Trans[2] = tc;            
            values4Cost4Trans[3] = tt;
            values4Cost4Trans[4] = tokenId;
            values4Cost4Trans[5] = governmentKey;
        }else{
            values4Cost4Trans[0] = "";        
            values4Cost4Trans[1] = "";
            values4Cost4Trans[2] = "";
            values4Cost4Trans[3] = "";
            values4Cost4Trans[4] = "";
            values4Cost4Trans[5] = "";
        }
        return values4Cost4Trans;
    }
    
    public String getTokenId(){
        return tokenId;
    }
    
    public String getGovernmentKey(){
        return governmentKey;
    }
    
    public void setError(String err){
        error = err;
    }

    public String getError(){
        return error;
    }
    
    
}
