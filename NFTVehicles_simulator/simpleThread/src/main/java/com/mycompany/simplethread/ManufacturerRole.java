/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

import static com.mycompany.simplethread.Utilerias.MState.TOKENIZING;
import static com.mycompany.simplethread.Utilerias.MState.NOTHING;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author clopezp
 */
public class ManufacturerRole implements Runnable{
    private boolean sigue = true;
    Utilerias.MState state;
    private final int timeEachProduction;
    private final int maxCars;
    private final String myName;
    private ArrayList<Vehicle> tokenized;
    //private int tokenized=0;    
    private ArrayList<Vehicle> minted;
    private ArrayList<Vehicle> toMint;
    private ArrayList<Vehicle> noMinted;
    //private int minted=0;
    private ArrayList<Vehicle> toReqOR;
    private ArrayList<Vehicle> numReqOR;
    private ArrayList<Vehicle> noReqOR;
    private ArrayList<Vehicle> numErrorsTokenized;    
    //private int numReqOR=0;
    private final Random rand;
    //private Vehicle cars[];
    private String manufacturerKey; // = "0x2CFcBB9Cf2910fBa7E7E7a8092aa1a40BC5BA341"; //it is required to know manufacturers key
    //private String governmentKey = "0x64D02158CbD8D1856440C14A1d5CFceBA80c804e"; 
    //private JSONObject jsonResults[];
    private String fileName;
    private GovernmentRole government;    

    
    ManufacturerRole(int time, int mCars, String name, String keyManuf, GovernmentRole gover){        
        this.government = gover;
        timeEachProduction = time;
        maxCars = mCars;
        myName = name;
        this.manufacturerKey = keyManuf;
        state = NOTHING;
        rand = new Random();
        //jsonResults = new JSONObject[maxCars];
        //cars = new Vehicle[maxCars];
        fileName = "tokenized" + name + ".txt";
        createFile();
        //Utilerias util = new Utilerias();
        tokenized = new ArrayList<>();
        toMint = new ArrayList<>();        
        minted = new ArrayList<>();
        noMinted = new ArrayList<>();
        toReqOR = new ArrayList<>();
        numReqOR = new ArrayList<>();    
        noReqOR = new ArrayList<>();    
        numErrorsTokenized = new ArrayList<>();    
    }


    private void createFile(){ //tokenized.txt        
        //System.out.println("Creating a file");
        String workingDir = System.getProperty("user.dir");        
        String filePath = workingDir + "/tokensCreated/" + fileName;
        File file = new File(filePath);
        
        // Check if the file already exists
        if (file.exists()) {
            //file.delete();
            //System.out.println("File already exists, removing!");
            return;
        }

        try {
            // Create a new file
            if (file.createNewFile()) {
                //System.out.println("File created successfully.");                
            } else {
                System.out.println("Failed to create the file.");
            }
        } catch (IOException ex) {
            Logger.getLogger(ManufacturerRole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void addData2File(String jsonString){        
        //
        //System.out.println("Adding data to the file:");
        String workingDir = System.getProperty("user.dir");        
        String filePath = workingDir + "/tokensCreated/" + fileName;
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(jsonString);
            bufferedWriter.newLine();
            bufferedWriter.close();            
        //System.out.println(filePath);
        //System.out.println(jsonString);
            
        } catch (IOException ex) {
            Logger.getLogger(ManufacturerRole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        
    private void tokenize(){
            Utilerias util = new Utilerias();            
            String threadName = Thread.currentThread().getName();
            Vehicle car = new Vehicle(manufacturerKey);
            String keys[] = car.getInitializedKeys();            
            String values[] = car.getInitializedValues();
            //System.out.println(keys[5] + ": " + values[5]);
            String service = "create";
            long timeStarting = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            String jsonString = util.connect(keys,values,service);
            long timeReturning = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                //System.out.println("Output from Server: \n" + jsonString);            
            try {
                    JSONObject jsonObject = new JSONObject(jsonString);                    
                    String result = jsonObject.getString("Result");
                    jsonObject.append("name", values[1]);
                    jsonObject.append("TimeStarting", timeStarting);
                    jsonObject.append("TimeReturning", timeReturning);
                    switch (result) {
                        case "Error" -> {
                            numErrorsTokenized.add(car);
                            if(jsonObject.getString("Num").equals("9")){
                                //System.out.println("Error: from: " + jsonObject.getString("from") + "\n" + jsonObject.getString("Num") + jsonObject.getString("Description"));
                                System.out.println(threadName + " Error: from: " + jsonObject.getString("from") + "\n " + jsonObject.getString("Description") + "\n Exiting");
                                this.sigue = false;
                            }else{
                                System.out.println(threadName + " Error: from: " + jsonObject.getString("from") + "\n " + jsonObject.getString("Description"));
                            }
                        }
                        case "Success" -> {
                            String from = jsonObject.getString("from").toUpperCase();
                            String fromOrigin = jsonObject.getString("fromorigin").toUpperCase();
                            if(from.equals(manufacturerKey.toUpperCase())){
                                car.setTokenized(jsonObject);
                                jsonString = jsonObject.toString();
                                addData2File(jsonString);                        
                                tokenized.add(car);
                                toMint.add(car);                        
                            }else{
                                numErrorsTokenized.add(car);
                                String threadName2 = Thread.currentThread().getName();
                                System.out.println(threadName + ":" + threadName2 + "Error: manufacturer: key manufacturers does not coincide 1 \n" +
                                        "Manuf: " + values[5] + "\n" +
                                        "Manufac key return:" + from + "\n" +
                                        "Manufac key origin:" + fromOrigin);
                            }
                        }
                        case "ErrRare" -> {
                            numErrorsTokenized.add(car);
                            String from = jsonObject.getString("from").toUpperCase();
                            String fromOrigin = jsonObject.getString("fromorigin").toUpperCase();                            
                            String threadName2 = Thread.currentThread().getName();
                            System.out.println(threadName + ":" + threadName2 + "Error: manufacturer: key manufacturers does not coincide 2 \n" +
                                    "Manuf: " + values[5] + "\n" +
                                        "Manufac key return:" + from + "\n" +
                                        "Manufac key origin:" + fromOrigin);
                        }
                    default -> {
                    }
                }
            } catch (JSONException ex) {
                    Logger.getLogger(ManufacturerRole.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
    
    private boolean mint(Vehicle car){
        Utilerias util = new Utilerias();
        String keys[] = car.getKeysForMinting();
        String values[] = car.getValuesForMinting(government.addressKey);
        String service = "mint";
        long timeStarting = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        String jsonString = util.connect(keys,values,service);
        long timeReturning = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        JSONObject jsonObject;                  
        try {
            jsonObject = new JSONObject(jsonString);
            String result = jsonObject.getString("Result");
            if(result.equals("Error") && jsonObject.getString("Num").equals("9")){
                sigue = false; // stopping this thread of tokenizing because a problem was identified            
                return false;
            }else{                
                try {                    
                    jsonObject.append("TimeStarting", timeStarting);
                    jsonObject.append("TimeReturning", timeReturning);
                    if(jsonObject.has("transactionHash")){
                        car.setMinted(jsonObject);
                        return true;
                    }else{
                        //System.out.println("Error: trying minting \n" + car.getContractAddress() + "\n" + values[3]); 
                        //System.out.println(jsonString);
                        car.setError(jsonString);
                        return false;
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(ManufacturerRole.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (JSONException ex) {
            Logger.getLogger(ManufacturerRole.class.getName()).log(Level.SEVERE, null, ex);
        }
        //}
        return false;
    }
    
    private void tryMint(){
        Utilerias util = new Utilerias();
        if(!toMint.isEmpty()){
            Vehicle car = toMint.get(0);
            if(mint(car)){
                util.espera(1000);
                minted.add(car);
                toMint.remove(0);
                toReqOR.add(car);                        
            }
        }
    }

    private boolean reqOwnerRight(){ //REQOWNERRIGHT
        Utilerias util = new Utilerias();
        //System.out.println(myName + ": Selling");
        if(!toReqOR.isEmpty()){
            Vehicle car = toReqOR.get(0);
            String keys[] = car.getKeysForReqOwnerRight();            
            String values[] = car.getValuesForReqOwnerRight(manufacturerKey);
            String service = "requestOwnerRight";
            
            long timeStarting = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            String jsonString = util.connect(keys,values,service);
            long timeReturning = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            JSONObject jsonObject;
            try {                        
                jsonObject = new JSONObject(jsonString);
                String result = jsonObject.getString("Result");
                if(result.equals("Error") && jsonObject.getString("Num").equals("9")){
                    sigue = false; // stopping this thread of tokenizing because a problem was identified            
                    return false;
                }else{
                    jsonObject.append("TimeStarting", timeStarting);
                    jsonObject.append("TimeReturning", timeReturning);
                    if(jsonObject.has("transactionHash")){
                        car.setRequestedOwnerRights(jsonObject); 
                        government.ownerRightRequested(car);                            
                        numReqOR.add(car);
                        toReqOR.remove(0);
                        return true;
                    }else{
                        noReqOR.add(car);
                        toReqOR.remove(0);
                        car.setError(jsonString);                            
                        return false;                            
                    }
                }            
            } catch (JSONException ex) {
                Logger.getLogger(ManufacturerRole.class.getName()).log(Level.SEVERE, null, ex);
            }                        
        }
        return false;
    }

    public int getTokenized(){
        return tokenized.size();
    }
    public int getMinted(){
        return minted.size();
    }
    public int getRequestedOR(){
        return numReqOR.size();
    }

    public int getErrorsTokenized(){
        return numErrorsTokenized.size();
    }
    
    public String getName(){
        return myName;
    }
    public String getState(){
        return (Utilerias.getState(state));
    }

    public ArrayList<Vehicle> getVehicles(){
        return tokenized;
    }
    
    private void setRoleState(){
        int randomNum = rand.nextInt(100);
        if(randomNum<40){
            state = Utilerias.MState.TOKENIZING;
        }else if (randomNum>=40 && randomNum<70){
            state = Utilerias.MState.MINTING;
        }else{
            state = Utilerias.MState.REQOWNERRIGHT;
        }
    }

    private void setRoleState2(){
        state = Utilerias.MState.TOKENIZING;
    }
    
    private void notMintedReport(){
        System.out.println("-----------------------------------");
        System.out.println("Not Minted: " + noMinted.size());
        for (Vehicle car: noMinted) {
            System.out.println("------------------------------");
            System.out.println("Name: " + car.getName());
            System.out.println("Contract: " + car.getContractAddress());
            System.out.println("Manufacturer key: " + car.getManufacturerKey());
            System.out.println("TokenId: " + car.getTokenId());
            System.out.println("Tokenized: " + car.isTokenized());
            System.out.println("Result: " + car.getError());
            System.out.println("JSON tokenized: " + car.getJSONResultTokenized().toString());            
        }
    }
    
    private void notRequestedORReport(){
        System.out.println("-----------------------------------");
        System.out.println("notRequestedORReport(): " + noReqOR.size());
        for (Vehicle car: noReqOR) {
            System.out.println("------------------------------");
            System.out.println("Name: " + car.getName());
            System.out.println("Contract: " + car.getContractAddress());
            System.out.println("Manufacturer key: " + car.getManufacturerKey());
            System.out.println("Government key: " + car.getGovernmentKey());
            System.out.println("TokenId: " + car.getTokenId());
            System.out.println("Minted: " + car.isMinted());            
        }
    }
    
    @Override
    public void run() {
        Utilerias util = new Utilerias();
        state = TOKENIZING;
        while(sigue && ((tokenized.size()<maxCars) || (toMint.size()>0))){            
            switch (state) {
                case TOKENIZING -> {
                    util.espera(timeEachProduction);
                    if(tokenized.size()<maxCars) tokenize();
                    //break;
                }
                case MINTING -> {
                    util.espera(timeEachProduction);
                    //break;
                    tryMint();
                }
                case REQOWNERRIGHT -> {
                    util.espera(timeEachProduction);
                    reqOwnerRight();
                    //break;
                }
                default -> {
                    sigue = false;
                }
            }
            setRoleState(); // randoming the state of the manufacturer
        }
        state = Utilerias.MState.NOTHING;
        System.out.println("Total of: ");
        System.out.println("Tokenized: " + tokenized.size());
        System.out.println("Minted: " + minted.size());
        System.out.println("No minted: " + noMinted.size());                
        System.out.println("RequestedOR: " + numReqOR.size());
        System.out.println("No RequestedOR: " + noReqOR.size());
        //Not minted report
        notMintedReport();
        //notRequestedORReport();
    }
    
    
}
