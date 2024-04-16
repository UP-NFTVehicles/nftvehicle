/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author clopezp
 */
public class ManufacturerWindowRecovery extends JFrame{

    private JTable table;
    private JSONObject[] jsonObject;
    
    ManufacturerWindowRecovery(String manufName){
        this.setTitle(manufName);
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,1));
        //-------------------------------------------------
        JPanel northPanel = new JPanel(new GridLayout(1,2));
        
        JPanel nortLeft = new JPanel(new GridLayout(1,2));

        JLabel lblName = new JLabel("Name file:");
        JTextField txtnName = new JTextField(manufName);

        nortLeft.add(lblName);
        nortLeft.add(txtnName);
        JButton buttonPlay = new JButton("Play");
        buttonPlay.setEnabled(false);

        // Add the JTextArea components to the northPanel
        northPanel.add(nortLeft);
        northPanel.add(buttonPlay);
        //-------------------------------------------------
        
        //-------------------------------------------------
        JPanel southPanel = new JPanel(new GridLayout(1,1));
        //JPanel southPanel = new JPanel(new FlowLayout());
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Index");
        model.addColumn("contractAddress");
        model.addColumn("transactionHash");
        model.addColumn("gasUsed");
        model.addColumn("blockNumber");
        model.addColumn("blockHash");
        table = new JTable(model);        
        JScrollPane scrollPane = new JScrollPane(table);        
        // Add the JTextArea components to the northPanel
        southPanel.add(scrollPane);
        
        //-------------------------------------------------

        //adding north and south panels to the main panel
        //mainPanel.add(northPanel,BorderLayout.NORTH);        
        //mainPanel.add(southPanel,BorderLayout.SOUTH);
        mainPanel.add(northPanel);        
        mainPanel.add(southPanel);

        //adding main panel to the frame
        this.add(mainPanel);
        recoveringCarsFromFile(manufName);
        addListener2TableModel(table.getSelectionModel());
    }
    
    private JSONObject[] redimensiona(JSONObject[] originalJSObject){
         // Copy elements from original array to redimensioned array
        int size = originalJSObject.length;
        JSONObject[] redimensionedjsonOb = new JSONObject[size*2];
        for (int i = 0; i < size; i++) {
            redimensionedjsonOb[i] = originalJSObject[i];
        }
        // Update reference to redimensioned array
        originalJSObject = null;
        originalJSObject = redimensionedjsonOb;
        System.out.println("New length: " + originalJSObject.length);
        return redimensionedjsonOb;
    }
    
    private void recoveringCarsFromFile(String fName){
        String workingDir = System.getProperty("user.dir");        
        String folderPath = workingDir + "/tokensCreated/";        
        String fileName = folderPath + fName;
        FileReader fileReader;
        try {
            fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader); 
            String line;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int numLine=0;
            int SIZE = 50;
            jsonObject = new JSONObject[SIZE]; 
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);                    
                    try {
                        if(numLine>= SIZE){
                            jsonObject = redimensiona(jsonObject);
                            SIZE *=2;
                            System.out.println("Size array: " + jsonObject.length);
                        } 
                        jsonObject[numLine] = new JSONObject(line);
                        String contractAdd = jsonObject[numLine].getString("contractAddress");
                        String transactAdd = jsonObject[numLine].getString("transactionHash");
                        String gasUsed = jsonObject[numLine].getString("gasUsed");
                        String blockNumber = jsonObject[numLine].getString("blockNumber");
                        String blockHash = jsonObject[numLine].getString("blockHash");
                        model.addRow(new Object[] {numLine,contractAdd,transactAdd,gasUsed,blockNumber,blockHash});
                        numLine++;
                    } catch (JSONException ex) {
                        Logger.getLogger(ManufacturerWindowRecovery.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ManufacturerWindowRecovery.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManufacturerWindowRecovery.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    private void addListener2TableModel(ListSelectionModel selecModel){
                //-----------
        ListSelectionModel selectionModel = selecModel;
        selectionModel.addListSelectionListener(new ListSelectionListener() {
        @Override
            public void valueChanged(ListSelectionEvent e) {
                // code to handle selection change event
                //System.out.println(e.getValueIsAdjusting());
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                //Vehicle[] vehicles = mRole.getVehicles();                
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i) && e.getValueIsAdjusting()) {
                        Vehicle car = new Vehicle(jsonObject[i]);
                        VehicleWindow vDetails = new VehicleWindow(car);
                            vDetails.setVisible(true);                        
                    }
                }
            }
        });
        //----------
    }
}
