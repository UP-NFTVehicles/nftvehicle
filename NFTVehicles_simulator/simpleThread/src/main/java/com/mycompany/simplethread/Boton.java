/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author clopezp
 */
public class Boton extends JButton implements ActionListener  {
    private ManufacturersWindow mw;
    Boton(String name, ManufacturersWindow mw){
        this.setName(name);
        this.setText(name);
        this.mw = mw;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.setText("Disabled");
        this.setEnabled(false);
        GovernmentRole gov = new GovernmentRole("0x64D02158CbD8D1856440C14A1d5CFceBA80c804e");
        InitAndMonitor simulate = new InitAndMonitor(mw,gov);
        Thread simulateTh = new Thread(simulate);
        simulateTh.start();        
        Thread simulaGov = new Thread(gov);
        simulaGov.start();
    }
    
}
