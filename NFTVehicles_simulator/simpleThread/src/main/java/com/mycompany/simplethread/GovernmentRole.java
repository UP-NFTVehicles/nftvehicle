/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author clopezp
 */
public class GovernmentRole implements Runnable{

        public String addressKey;
        ArrayList<Vehicle> carsSetCostTransaction;
        ArrayList<Vehicle> cars;
        ArrayList<Vehicle> carsInvalidated;
        //int nextCar=0; //this variable pinpoints if a car must be invalidated
        public boolean sigue;
        //private Utilerias util;
        
        GovernmentRole(String add){
            this.addressKey = add;
            cars = new ArrayList<>();
            carsSetCostTransaction  = new ArrayList<>();
            carsInvalidated = new ArrayList<>();
            sigue = true;
            //util = new Utilerias();
        }

    public synchronized void ownerRightRequested(Vehicle car) {
        //this method must be protected for accesing at the same time
            //this.wait();
            cars.add(car);
    }
    
    private boolean setCostForTheTransaction(Vehicle car, String tc, String tt){     
        Utilerias util = new Utilerias();
            String keys[] = car.getKeys4Cost4Trans();
            String values[] = car.getValues4Cost4Trans(tc, tt);
            String service = "setCost4Trans";
            if(values[5].equals("")){
                System.out.println("Error: government key is empty");
            }else{
                String jsonString = util.connect(keys,values,service);
                //System.out.println("Output from Server: \n" + jsonString);
                if(jsonString.equals("Error")){
                    System.out.println("Error: connection was lost");
                    return false;
                }else{
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(jsonString);
                        boolean exists = jsonObject.has("Result");
                        if(exists){
                            if(jsonObject.getString("Result").equals("Error")){
                                //System.out.println(jsonObject.toString());
                                return false;
                            }
                        }else{
                            //System.out.println("Set the transaction cost successfully");
                            return true;
                        }
                    } catch (JSONException ex) {
                        Logger.getLogger(GovernmentRole.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            
            }            
            return false;
    }

    @Override
    public void run() {
        //boolean sigue = true;
        //int size=0;
        Utilerias util = new Utilerias();
        while(sigue || (cars.size()>0)){
            //size = cars.size();
            if(cars.size()>0){
                Vehicle car = cars.get(0); // obtaining a car in the list
                String tt = car.getCharacteristicValue("inTransProcess");
                String tc = Utilerias.getCostTranType(tt);
                if(tt.equals("Error")){
                    System.out.println("Error: Government: it was a connection problem");
                    sigue = false; 
                }else{
                    if(tc.equals("") || tc.equals("0") || tt.equals("0")){
                        //System.out.println("Error:  Government: Transaction process was not putted\n" + car.getContractAddress() + "\n" + tc + "\n" + tt);
                        carsInvalidated.add(car);
                        cars.remove(0);                        
                    }else{
                        if(this.setCostForTheTransaction(car, tc, tt)){
                            //System.out.println("Car " + car.getName() + " has been included the cost of the transaction");
                            carsSetCostTransaction.add(car);
                            cars.remove(0);
                            //System.out.println("Quedan " + cars.size() + " pending cars");
                        }else{
                            //System.out.println("Error:  Government: Car was not possible to include the cost of the transaction\n" + car.getContractAddress());
                            carsInvalidated.add(car);
                            cars.remove(0);                        
                        }
                        util.espera(300);                                     
                    }
                }
            }else{
                util.espera(3000); //waiting a considerable time
            }
        }
        System.out.println("Government stadistics:");
        System.out.println("Cars set cost of the transaction: " + carsSetCostTransaction.size());
        System.out.println("Cars invalidated: " + carsInvalidated.size());
    }
    
}
