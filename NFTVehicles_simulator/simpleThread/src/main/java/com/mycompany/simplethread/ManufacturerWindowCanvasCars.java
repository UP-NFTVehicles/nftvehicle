/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

//import java.awt.FlowLayout;
import java.awt.Canvas;
import java.awt.GridLayout;
import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
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
//import org.json.JSONException;

/**
 *
 * @author clopezp
 */
public class ManufacturerWindowCanvasCars  extends JFrame{

    private ManufacturerRole mRole;
    
    
    
    
    ManufacturerWindowCanvasCars(ManufacturerRole role){
        mRole = role;
        this.setTitle(mRole.getName());
        this.setSize(1280,768);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,1));
        //-------------------------------------------------
        JPanel northPanel = new JPanel(new GridLayout(1,2));
        
        JPanel nortLeft = new JPanel(new GridLayout(5,2));

        JLabel lblName = new JLabel("Name:");
        JTextField txtnName = new JTextField(mRole.getName());

        JLabel lblState = new JLabel("State");
        JTextField txtState = new JTextField(mRole.getState());

        JLabel lbltotalTokenized = new JLabel("Total Tokenized:");        
        JTextField txtTotalTokenized = new JTextField(Integer.toString(mRole.getTokenized())+ " blue");

        JLabel lblTotalMinted = new JLabel("Total Minted:");
        JTextField txtTotalMinted = new JTextField(Integer.toString(mRole.getMinted())+ " red");

        JLabel lblTotalSold = new JLabel("Total OR:");
        JTextField txtTotalReqOR = new JTextField(Integer.toString(mRole.getRequestedOR()) + " green");
        
        
        nortLeft.add(lblName);
        nortLeft.add(txtnName);
        nortLeft.add(lblState);
        nortLeft.add(txtState);
        nortLeft.add(lbltotalTokenized);
        nortLeft.add(txtTotalTokenized);
        nortLeft.add(lblTotalMinted);
        nortLeft.add(txtTotalMinted);
        nortLeft.add(lblTotalSold);
        nortLeft.add(txtTotalReqOR);
        JButton buttonPlay = new JButton("Start simulation");        

        // Add the JTextArea components to the northPanel
        northPanel.add(nortLeft);
        northPanel.add(buttonPlay);
        //-------------------------------------------------
        
        //-------------------------------------------------
        JPanel southPanel = new JPanel(new GridLayout(1,1));
        //Canvas lienzo = new Canvas();
        Cars areaCars = new Cars(role);
        JScrollPane scrollPane = new JScrollPane(areaCars);        
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
        //addListener2TableModel(table.getSelectionModel(),cars.size());
    }

/*    
    private void addListener2TableModel(ListSelectionModel selecModel, int nCars){
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
                ArrayList<Vehicle> vehicles = mRole.getVehicles();                
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i) && e.getValueIsAdjusting()) {
                        VehicleWindow vDetails = new VehicleWindow(vehicles.get(i));
                            vDetails.setVisible(true);                        
                    }
                }
            }
        });
        //----------

    }
  */
    
}
