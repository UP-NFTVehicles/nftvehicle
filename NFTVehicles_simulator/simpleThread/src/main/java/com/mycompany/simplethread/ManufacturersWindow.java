/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simplethread;

//import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
//import javax.swing.BoxLayout;
//import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
//import javax.swing.ListSelectionModel;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author clopezp
 */
public class ManufacturersWindow extends JFrame{    
    private JTextField txtManufac;
    public JComboBox<String> jcbManufac;
    public JTextField txtTimeProduction;
    public JTextField txtTotalCars;
    public JTextField txtPrefixName;
    public JTable table;
    public Boton buttonPlay;
    public String[] keys = {
        "0x2CFcBB9Cf2910fBa7E7E7a8092aa1a40BC5BA341",
        "0xa6ba79E509d7adb4594852E50D3e48BDcA15D07e",
        "0x32d680Aa90D45B677BBa0fFE9Af3d3578dcB4a83",
        "0x207Ee448397059BA705629674b2F8c9Df1CA594b"};
    
    private File[] files;
    
    ManufacturersWindow(String title){
        generalSimilarConditions(title);
        
        //JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel mainPanel = new JPanel();
        //mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        mainPanel.setLayout(new GridLayout(2,1));
        //-------------------------------------------------
        JPanel northPanel = new JPanel(new GridLayout(1,2));
        
        JPanel nortLeft = new JPanel(new GridLayout(4,2));

        JLabel nManufac = new JLabel("# of Manufacturers:");
        jcbManufac = new JComboBox<>();
        // Add items to the dropdown
        jcbManufac.addItem("1");
        jcbManufac.addItem("2");
        jcbManufac.addItem("3");
        jcbManufac.addItem("4");
        jcbManufac.setSelectedIndex(3);

        JLabel timeProduction = new JLabel("Time for each production:");
        txtTimeProduction = new JTextField("100");

        JLabel totalCars = new JLabel("# cars produced f/e manufacturer:");
        txtTotalCars = new JTextField("5");

        JLabel prefixName = new JLabel("Prefix name");
        txtPrefixName = new JTextField("Manufac");
        
        nortLeft.add(nManufac);
        nortLeft.add(jcbManufac);
        nortLeft.add(timeProduction);
        nortLeft.add(txtTimeProduction);
        nortLeft.add(totalCars);
        nortLeft.add(txtTotalCars);
        nortLeft.add(prefixName);
        nortLeft.add(txtPrefixName);        
        buttonPlay = new Boton("Play",this);
        buttonPlay.addActionListener(buttonPlay);

        // Add the JTextArea components to the northPanel
        northPanel.add(nortLeft);
        northPanel.add(buttonPlay);
        //-------------------------------------------------
        
        //-------------------------------------------------
        //JPanel southPanel = new JPanel(new FlowLayout());
        JPanel southPanel = new JPanel(new GridLayout(1,1));
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("State");
        model.addColumn("#Tokenized");
        model.addColumn("#Minted");
        model.addColumn("#ReqOwnerRight");
        model.addColumn("#ErrorsTokenized");
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
    }

    ManufacturersWindow(String title, String statusString){
        this.generalSimilarConditions(title);        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,1));
        JPanel northPanel = new JPanel(new GridLayout(1,2));
        JPanel nortLeft = new JPanel(new GridLayout(1,2));
        JLabel nManufac = new JLabel("# of Manufacturers:");
        txtManufac = new JTextField("");

        nortLeft.add(nManufac);
        nortLeft.add(txtManufac);
        buttonPlay = new Boton("Play",this);
        buttonPlay.setEnabled(false);

        // Add the JTextArea components to the northPanel
        northPanel.add(nortLeft);
        northPanel.add(buttonPlay);
        //-------------------------------------------------
        
        //-------------------------------------------------
        //JPanel southPanel = new JPanel(new FlowLayout());
        JPanel southPanel = new JPanel(new GridLayout(1,1));
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Index");
        model.addColumn("Namefile");
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
        recoveringNameFiles();
        addListener2TableModel(table.getSelectionModel());
    }
    
    private void generalSimilarConditions(String title){
        this.setTitle(title);
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);    
    }
    
    private void recoveringNameFiles(){
        int fileCount=0;
        String workingDir = System.getProperty("user.dir");        
        String folderPath = workingDir + "/tokensCreated/";        
        File folder = new File(folderPath);
        files = folder.listFiles();

        if (files != null) {
            fileCount = files.length;
            txtManufac.setText(String.valueOf(fileCount));
            System.out.println("Number of files in the folder: " + fileCount);
            for(int i=0;i<fileCount;i++){                                
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                String nameFile = files[i].getName();
                model.addRow(new Object[] {i,nameFile});                    
            }
        } else {
            System.out.println("Folder does not exist or is not a directory.");
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
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i) && e.getValueIsAdjusting()) {
                        ManufacturerWindowRecovery manuWR = new ManufacturerWindowRecovery(files[i].getName());
                            manuWR.setVisible(true);                        
                    }
                }
            }
        });
        //----------

    }    
    
    
}
