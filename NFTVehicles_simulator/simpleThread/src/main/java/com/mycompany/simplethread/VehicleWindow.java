/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author clopezp
 */
public class VehicleWindow extends JFrame{
    private Vehicle car;
    VehicleWindow(Vehicle car){
        this.car = car;
        this.setTitle(car.getName());
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,1));
        //-------------------------------------------------
        JPanel northPanel = new JPanel(new GridLayout(1,2));
        
        JPanel nortLeft = new JPanel(new GridLayout(6,2));

        JLabel lblName = new JLabel("Name:");
        JTextField txtnName = new JTextField(car.getName());

        JLabel lblState = new JLabel("Contract Address:");
        JTextField txtState = new JTextField(car.getContractAddress());

        JLabel lbltotalBuilders = new JLabel("Manufacturer:");        
        JTextField txtTotalBuilders = new JTextField(car.getManufacturerKey());

        JLabel lblTotalTokenized = new JLabel("Minted:");
        JTextField txtTotalTokenized = new JTextField(String.valueOf(car.isMinted()));

        //JLabel lblTotalSold = new JLabel("Characteristic:");
         // Create a new JComboBox
        JComboBox<String> characteristicDropdown = new JComboBox<>();

        // Add items to the dropdown
        characteristicDropdown.addItem("Select a chracteristic:");
        characteristicDropdown.addItem("name");
        characteristicDropdown.addItem("legal");
        characteristicDropdown.addItem("active");
        characteristicDropdown.addItem("currentDebt");
        characteristicDropdown.addItem("currentGovDebt");
        characteristicDropdown.addItem("government");
        characteristicDropdown.addItem("manufacturer");
        characteristicDropdown.addItem("inTransProcess");
        characteristicDropdown.addItem("descriptionEndLife");
        characteristicDropdown.addItem("legalOwner");        
        characteristicDropdown.addItem("possiblelegalOwner");
        characteristicDropdown.addItem("stolen");            
        characteristicDropdown.addItem("helper");            
        characteristicDropdown.addItem("insured");           
        characteristicDropdown.addItem("MILEAGE_RECORD");    
        characteristicDropdown.addItem("MILEAGE_RECORD_DESCRIPTION");
        JTextField txtCharacteristic = new JTextField("NOT ASSIGNED");

         characteristicDropdown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform action when the selected item is changed
                String selected = (String)characteristicDropdown.getSelectedItem();
                txtCharacteristic.setText(car.getCharacteristicValue(selected));
            }
        });
        
        JComboBox<String> jcbHistoricTran = new JComboBox<>();
        // Add items to the dropdown
        jcbHistoricTran.addItem("Events:");
        jcbHistoricTran.addItem("historicTransactions");
        jcbHistoricTran.addItem("historicOwnerrights");
        jcbHistoricTran.addItem("stolenLife");
        jcbHistoricTran.addItem("historicHelperUsers");
        jcbHistoricTran.addItem("historicMileageRecord");
        jcbHistoricTran.addItem("historicServices");
        jcbHistoricTran.addItem("historicInsuranceServices");
        jcbHistoricTran.addItem("OwnershipTransferred");                
        JButton btnHistoricTran = new JButton("GET");
         
        
        nortLeft.add(lblName);
        nortLeft.add(txtnName);
        nortLeft.add(lblState);
        nortLeft.add(txtState);
        nortLeft.add(lbltotalBuilders);
        nortLeft.add(txtTotalBuilders);
        nortLeft.add(lblTotalTokenized);
        nortLeft.add(txtTotalTokenized);
        nortLeft.add(characteristicDropdown);
        nortLeft.add(txtCharacteristic);
        nortLeft.add(jcbHistoricTran);
        nortLeft.add(btnHistoricTran);                
        String workingDir = System.getProperty("user.dir");
        
        // This block is to display the picture of the car        
        //BufferedImage carPicture=null;
        ImageIcon scaledIcon = null;

            //carPicture = ImageIO.read(new File(workingDir + "/images/blueConvertible.jpg"));            
            ImageIcon icon = new ImageIcon(workingDir + "/images/blueConvertible.jpg"); // Load the image
            Image image = icon.getImage(); // Get the image from the ImageIcon object
            Image scaledImage = image.getScaledInstance(360, 250,  java.awt.Image.SCALE_SMOOTH); // Scale the image to fit a 200x200 area
            scaledIcon = new ImageIcon(scaledImage); // Create a new ImageIcon object from the scaled image

        JLabel picCar = new JLabel(scaledIcon);
        
        
        
        
        //JLabel picCar = new JLabel(workingDir); //"ACA VA LA IMAGEN"
        
        // Add the JTextArea components to the northPanel
        northPanel.add(nortLeft);
        northPanel.add(picCar);
        //-------------------------------------------------
        
        //-------------------------------------------------        
        JPanel southPanel = new JPanel(new GridLayout(1,1));
        JTextArea jtaEvents = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(jtaEvents);
        //jtaEvents.setSize(this.getWidth(), this.getHeight()/2);
        southPanel.add(scrollPane);
        
        btnHistoricTran.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Perform action when the selected item is changed
                String selected = (String)jcbHistoricTran.getSelectedItem();
                jtaEvents.setText(selected);
                //txtCharacteristic.setText(car.getCharacteristicValue(selected));
                jtaEvents.setText(car.getEventsValue(selected));
            }
        });
        
        //-------------------------------------------------

        //adding north and south panels to the main panel
        //mainPanel.add(northPanel,BorderLayout.NORTH);        
        //mainPanel.add(southPanel,BorderLayout.SOUTH);
        mainPanel.add(northPanel);        
        mainPanel.add(southPanel);

        //adding main panel to the frame
        this.add(mainPanel);
        
    }
}
