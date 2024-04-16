/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author clopezp
 */
public class InitAndMonitor implements Runnable{

    private ManufacturersWindow mw;
    private ManufacturerRole[] agentsM;
    private Thread[] threadM;
    private int nmanufacturers;
    private DefaultTableModel model;
    private GovernmentRole government;
    private Utilerias util;
    
    InitAndMonitor(ManufacturersWindow mw, GovernmentRole gover){
        this.government =   gover;
        this.mw = mw;
        util = new Utilerias();
    }
    
    private void starting(){        
        //System.out.println(mw.jcbManufac.getSelectedIndex() + 1);
        nmanufacturers = mw.jcbManufac.getSelectedIndex() + 1;
        //System.out.println("N manufacturers:" + nmanufacturers);
        int timeFEP = Integer.parseInt(mw.txtTimeProduction.getText());
        int carsPFEM = Integer.parseInt(mw.txtTotalCars.getText());        
        String pname = mw.txtPrefixName.getText();
        agentsM = new ManufacturerRole[nmanufacturers];
        threadM = new Thread[nmanufacturers];
        model.setRowCount(0);
        for(int i=0;i<nmanufacturers;i++){
            String name = pname + String.valueOf(i);
            agentsM[i] = new ManufacturerRole(timeFEP,carsPFEM,name,mw.keys[i],this.government);
            threadM[i] = new Thread(agentsM[i]);
            threadM[i].start();
            model.addRow(new Object[] {name,"","0","0","0"});
        }
        //-----------
        ListSelectionModel selectionModel = mw.table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
        @Override
            public void valueChanged(ListSelectionEvent e) {
                // code to handle selection change event
                //System.out.println(e.getValueIsAdjusting());
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                ManufacturerWindowDetails[] v = new ManufacturerWindowDetails[nmanufacturers];
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i) && e.getValueIsAdjusting()) {
                            v[i] = new ManufacturerWindowDetails(agentsM[i]);
                            v[i].setVisible(true);                        
                    }
                }
            }
        });
        //----------
        

    }
    
    @Override
    public void run() {
        model = (DefaultTableModel) mw.table.getModel();
        starting();
        boolean monitoring=true;
        while(monitoring){
            for(int i=0;i<nmanufacturers;i++){
                int built=agentsM[i].getTokenized();
                int minted=agentsM[i].getMinted();
                int sold=agentsM[i].getRequestedOR();
                int errorsTokenized=agentsM[i].getErrorsTokenized();                
                String name = agentsM[i].getName();
                String cstate = agentsM[i].getState();
                model.setValueAt(name, i, 0);
                model.setValueAt(cstate, i, 1);
                model.setValueAt(built, i, 2);
                model.setValueAt(minted, i, 3);
                model.setValueAt(sold, i, 4);
                model.setValueAt(errorsTokenized, i, 5);
            }
            int lives=0;
            for(int i=0;i<nmanufacturers;i++){
                if(threadM[i].isAlive()){
                    lives++;
                }
            }
            if(lives==0) monitoring = false;
            else util.espera(500);
        }
        mw.buttonPlay.setText("Play");
        mw.buttonPlay.setEnabled(true);
        this.government.sigue = false;
    }
    
}
