/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author clopezp
 */
public class Cars extends JPanel {

    private int numTokenized;
    private int numMinted;
    private int numOR;
    private ManufacturerRole role;
    
    Cars(ManufacturerRole role){
        this.role = role;
        /*this.numTokenized = role.getTokenized();
        this.numMinted = role.getMinted();
        this.numOR = role.getRequestedOR();*/
        assignPos(50,50);
    }

    private void assignPos(int x, int y){        
        ArrayList <Vehicle> Vehicles = role.getVehicles();
        int i=0;
        for(Vehicle v: Vehicles){
            v.posX = x;
            v.posY = y + (i++*50);
        }
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Set background color
        setBackground(Color.WHITE);
        drawCars(g); //drawing cars
        //drawCars(250,50,g,Color.RED,numMinted); //drawing minted
        //drawCars(450,50,g,Color.GREEN,numOR); //drawing RequestedOR
    }

    
    private void drawCars(Graphics g){
        ArrayList <Vehicle> Vehicles = role.getVehicles();
        for(Vehicle v: Vehicles){
            drawACar(v.posX, v.posY,50,20,15,g,v.color);
        }
    }
    
    private void drawACar(int x, int y, int sizeX, int sizeY, int llanta, Graphics g, Color c){
        g.setColor(c);
        g.fillRect(x, y, sizeX, sizeY);
        
        // Draw car roof
        g.setColor(c);
        g.fillRect(x+(sizeX/4), y-(sizeY/2), sizeX/2, sizeY);
        
        // Draw wheels
        g.setColor(Color.BLACK);
        g.fillOval(x + (sizeX/4)-(llanta/2), y + sizeY, llanta, llanta);
        g.fillOval(x + ((3*sizeX)/4)-(llanta/2), y + sizeY, llanta, llanta);    
    }

}