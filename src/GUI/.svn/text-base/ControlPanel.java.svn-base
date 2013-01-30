package GUI;

import Data.Junctions.*;
import Data.Lane;
import Data.Road;
import Dijkstra.Dijkstra;
import TLStrategies.MarchingControl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;

public class ControlPanel extends javax.swing.JPanel {

    private static SimulationPanel simPanel;
    private Timer timer;
    private final String defaultMapName = "default.map";
    private File currentFile = null;
    private ArrayList<JCheckBox> carCheckboxes = new ArrayList<JCheckBox>();
    private static boolean updateTotalLC = false;
    public static double timeStandingStill = 0;
    private double averageSpeed = 0;
    private int noOfCars = 0;

    public ControlPanel(SimulationPanel panel) {
        initComponents();
        simPanel = panel;
        panel.setPaintMode(paintRadioButton.isSelected());

        stopButton.setEnabled(false);
        startButton.setEnabled(false);
        resetButton.setEnabled(false);

        // On start, load the default map
        File defaultMap = new File(defaultMapName);
        if (defaultMap.exists()) {
            loadMap(defaultMap);
        }
        setTextFields();
    }

    public boolean isPaintMode() {
        return paintRadioButton.isSelected();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        modeGroup = new javax.swing.ButtonGroup();
        paintRadioButton = new javax.swing.JRadioButton();
        paintOptionsPanel = new javax.swing.JPanel();
        saveMapButton = new javax.swing.JButton();
        loadMapButton = new javax.swing.JButton();
        mapFileLabel = new javax.swing.JLabel();
        saveDefaultMap = new javax.swing.JButton();
        newMapButton = new javax.swing.JButton();
        trafficLightJunctionButton = new javax.swing.JButton();
        createMapButton = new javax.swing.JButton();
        simulationRadio = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        simulationOptionsPanel = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        intervalTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        orangeTimeText = new javax.swing.JTextField();
        greenTimeText = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        SpeedLimitDistanceTextField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        densityText = new javax.swing.JTextField();
        mergeGapText = new javax.swing.JTextField();
        LCFactorText = new javax.swing.JTextField();
        totalLCText = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        timeStandingStillTextField = new javax.swing.JTextField();
        averageSpeedTextField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        numberOfCarsTextField = new javax.swing.JTextField();
        SpawnCarsLabel = new javax.swing.JLabel();
        CarsSpawnedTextField = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Control"));

        modeGroup.add(paintRadioButton);
        paintRadioButton.setSelected(true);
        paintRadioButton.setText("Paint mode");
        paintRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paintRadioButtonActionPerformed(evt);
            }
        });

        paintOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Paint options"));

        saveMapButton.setText("Save map");
        saveMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMapButtonActionPerformed(evt);
            }
        });

        loadMapButton.setText("Load map");
        loadMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMapButtonActionPerformed(evt);
            }
        });

        mapFileLabel.setText("Map file: ");

        saveDefaultMap.setText("Save as default");
        saveDefaultMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDefaultMapActionPerformed(evt);
            }
        });

        newMapButton.setText("New map");
        newMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMapButtonActionPerformed(evt);
            }
        });

        trafficLightJunctionButton.setText("TL Junction");
        trafficLightJunctionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trafficLightJunctionButtonActionPerformed(evt);
            }
        });

        createMapButton.setText("create Map");
        createMapButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createMapButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout paintOptionsPanelLayout = new org.jdesktop.layout.GroupLayout(paintOptionsPanel);
        paintOptionsPanel.setLayout(paintOptionsPanelLayout);
        paintOptionsPanelLayout.setHorizontalGroup(
            paintOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(paintOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(paintOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mapFileLabel)
                    .add(paintOptionsPanelLayout.createSequentialGroup()
                        .add(paintOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(saveMapButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(saveDefaultMap, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(paintOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(paintOptionsPanelLayout.createSequentialGroup()
                                .add(loadMapButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(trafficLightJunctionButton))
                            .add(paintOptionsPanelLayout.createSequentialGroup()
                                .add(newMapButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(createMapButton)))))
                .addContainerGap(309, Short.MAX_VALUE))
        );
        paintOptionsPanelLayout.setVerticalGroup(
            paintOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(paintOptionsPanelLayout.createSequentialGroup()
                .add(paintOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(saveMapButton)
                    .add(loadMapButton)
                    .add(trafficLightJunctionButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(paintOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(saveDefaultMap)
                    .add(newMapButton)
                    .add(createMapButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mapFileLabel))
        );

        modeGroup.add(simulationRadio);
        simulationRadio.setText("Simulation mode");
        simulationRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simulationRadioActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        simulationOptionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Simulation options"));

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        intervalTextField.setText("100");

        jLabel1.setText("Uptate interval:");

        jLabel2.setText("ms");

        jLabel3.setText("Time:");

        timeLabel.setText("0.00");

        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        jLabel4.setText("s");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Junctions"));

        jLabel7.setText("Orange light time:");

        jLabel8.setText("Green light time:");

        orangeTimeText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                orangeTimeTextFocusLost(evt);
            }
        });

        greenTimeText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                greenTimeTextFocusLost(evt);
            }
        });

        jLabel10.setText("ms");

        jLabel9.setText("ms");

        SpeedLimitDistanceTextField.setText("20");
        SpeedLimitDistanceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SpeedLimitDistanceTextFieldActionPerformed(evt);
            }
        });

        jLabel19.setText("m");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(orangeTimeText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                        .add(24, 24, 24))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(greenTimeText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel10)
                        .add(24, 24, 24))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(SpeedLimitDistanceTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel19, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                        .add(55, 55, 55))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(orangeTimeText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(greenTimeText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel10))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(SpeedLimitDistanceTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel19))
                .add(63, 63, 63))
        );

        jLabel20.setText("SpeedLimit Distance:");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel7)
                    .add(jLabel8)
                    .add(jLabel20))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(59, 59, 59))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel8)
                .add(18, 18, 18)
                .add(jLabel20))
            .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("LC Variables"));

        jLabel13.setText("LCFactor:");

        jLabel14.setText("Merge Gap:");

        jLabel15.setText("Avg. density:");

        jLabel16.setText("Total LC's:");

        densityText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                densityTextActionPerformed(evt);
            }
        });

        jLabel17.setText("cars / m");

        jLabel18.setText("meters");

        jButton1.setText("Apply");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Apply");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel16)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(totalLCText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel13)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(LCFactorText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel14)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(mergeGapText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel15)
                        .add(18, 18, 18)
                        .add(densityText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton1)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel17)
                            .add(jLabel18))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(jButton4))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(new java.awt.Component[] {LCFactorText, densityText, mergeGapText, totalLCText}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jButton2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton3))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel15)
                            .add(densityText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel17))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel3Layout.createSequentialGroup()
                                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel14)
                                    .add(mergeGapText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel13)
                                    .add(LCFactorText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jButton1))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                    .add(jLabel16)
                                    .add(totalLCText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jButton4)))
                            .add(jLabel18))))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(new java.awt.Component[] {LCFactorText, densityText, mergeGapText, totalLCText}, org.jdesktop.layout.GroupLayout.VERTICAL);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Cars"));

        jLabel21.setText("Time standing still:");

        jLabel22.setText("Average speed:");

        jLabel23.setText("Number of cars:");

        SpawnCarsLabel.setText("Cars Spawned:");

        CarsSpawnedTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CarsSpawnedTextFieldActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel21)
                            .add(jLabel22)
                            .add(jLabel23))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(numberOfCarsTextField)
                            .add(timeStandingStillTextField)
                            .add(averageSpeedTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(SpawnCarsLabel)
                        .add(18, 18, 18)
                        .add(CarsSpawnedTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)))
                .addContainerGap(152, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel21)
                    .add(timeStandingStillTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel22)
                    .add(averageSpeedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel23)
                    .add(numberOfCarsTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(8, 8, 8)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(SpawnCarsLabel)
                    .add(CarsSpawnedTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        org.jdesktop.layout.GroupLayout simulationOptionsPanelLayout = new org.jdesktop.layout.GroupLayout(simulationOptionsPanel);
        simulationOptionsPanel.setLayout(simulationOptionsPanelLayout);
        simulationOptionsPanelLayout.setHorizontalGroup(
            simulationOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, simulationOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(simulationOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, simulationOptionsPanelLayout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(intervalTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 132, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel2))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, simulationOptionsPanelLayout.createSequentialGroup()
                        .add(startButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(stopButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(resetButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(timeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel4))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(687, 687, 687))
        );
        simulationOptionsPanelLayout.setVerticalGroup(
            simulationOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(simulationOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(simulationOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(startButton)
                    .add(stopButton)
                    .add(resetButton)
                    .add(jLabel3)
                    .add(timeLabel)
                    .add(jLabel4))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(simulationOptionsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(intervalTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(199, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(simulationOptionsPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .add(paintOptionsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, paintRadioButton)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, simulationRadio))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(paintRadioButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(paintOptionsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(simulationRadio)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void paintRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paintRadioButtonActionPerformed
        simPanel.setPaintMode(true);
        saveMapButton.setEnabled(true);
        loadMapButton.setEnabled(true);
        saveDefaultMap.setEnabled(true);
        newMapButton.setEnabled(true);

        stopButton.setEnabled(false);
        startButton.setEnabled(false);
        resetButton.setEnabled(false);

        if (timer != null) {
            timer.stop();
        }
        intervalTextField.setEnabled(true);
        timeLabel.setText("0.00");
    }//GEN-LAST:event_paintRadioButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        System.out.println("speed: " + Road.speedWeight + " density: " + Road.densityWeight);
        final int interval = Integer.parseInt(intervalTextField.getText());
        intervalTextField.setEnabled(false);
        final double currentTime = Double.parseDouble(timeLabel.getText());
        timer = new Timer(interval, new ActionListener() {

            double time = currentTime;
            // Runs one phase of the simulation

            public void actionPerformed(ActionEvent e) {
                int spawns = 0;
                double avSpeed = 0;
                timeStandingStill = 0;
                noOfCars = 0;
                for (Junction j : simPanel.getJunctionList()) {
                    j.update(interval);
                    avSpeed += j.getAverageSpeed();
                    noOfCars += j.getNoOfCars();
                    timeStandingStill += j.getTimeStandingStill();
                    if(j.getType() == JunctionType.Spawn) {
                        Spawn s = (Spawn)j;
                        spawns+=s.CarsSpawned();
                    }
                }
                CarsSpawnedTextField.setText(spawns+"");
                simPanel.repaint();
                time += interval;
                timeLabel.setText(Double.toString(time / 1000.0));
                if((int)(time)%10000==0) {
                    System.out.println(time/1000.0+", "+(timeStandingStill / spawns)+", "+Road.getTotalLC()+", "+Lane.getActualDensity()+", "+noOfCars+", "+spawns);
                }
                timeStandingStillTextField.setText(roundTwoDecimals(timeStandingStill / spawns) + " seconds");
                // System.out.println("Average speed: " + roundTwoDecimals(avSpeed) + " number of junctions: " + noOfCars);
                averageSpeedTextField.setText("" + roundTwoDecimals(avSpeed / noOfCars) + " km/h");
                numberOfCarsTextField.setText(noOfCars + "");
                if (ControlPanel.updateTotalLC == true) {
                    totalLCText.setText("" + Road.getTotalLC());
                    ControlPanel.setTotalLC(false);
                }
                
            }
        });

        timer.start();
    }//GEN-LAST:event_startButtonActionPerformed

    private double roundTwoDecimals(double d) {
        double result = d * 100;
        result = Math.round(result);
        result = result / 100;
        return result;
    }

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        timer.stop();
        intervalTextField.setEnabled(true);
        timeLabel.setText("0.00");
        timeStandingStill = 0;
        averageSpeed = 0;

        // TODO Reset junctions to original setup here, without le cars
        if (currentFile != null) {
            loadMap(currentFile);
        }
    }//GEN-LAST:event_resetButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        timer.stop();
        intervalTextField.setEnabled(true);
    }//GEN-LAST:event_stopButtonActionPerformed

    private void setTextFields() {
        orangeTimeText.setText(Double.toString(TimeBasedTrafficLightJunction.getOrangeTime()));
        greenTimeText.setText(Double.toString(TimeBasedTrafficLightJunction.getLightInterval()));

        densityText.setText("Unknown");
        mergeGapText.setText(Double.toString(Road.getMergeGap()));
        LCFactorText.setText(Double.toString(Road.getLCFactor()));
        totalLCText.setText(Double.toString(Road.getTotalLC()));
        String unit = "seconds";
        timeStandingStillTextField.setText("" + timeStandingStill + " " + unit);
        unit = "km/h";
        averageSpeedTextField.setText("" + averageSpeed + " " + unit);
        numberOfCarsTextField.setText(noOfCars + "");
    }

private void simulationRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulationRadioActionPerformed
    simPanel.setPaintMode(false);
    Dijkstra.constructMatrices(simPanel.getJunctionList());
    saveMapButton.setEnabled(false);
    loadMapButton.setEnabled(false);
    saveDefaultMap.setEnabled(false);
    newMapButton.setEnabled(false);
    stopButton.setEnabled(true);
    startButton.setEnabled(true);
    resetButton.setEnabled(true);
}//GEN-LAST:event_simulationRadioActionPerformed

    private class mapFileFilter extends FileFilter {

        public boolean accept(File pathname) {
            if (pathname.getName().endsWith(".map") || pathname.isDirectory()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String getDescription() {
            return "Map File (*.map)";
        }
    }

    private void saveMap(File f) {
        File file = null;
        if (!f.getName().endsWith(".map")) {
            file = new File(f.getAbsolutePath() + ".map");
        } else {
            file = f;
        }
        mapFileLabel.setText("Map file: " + file.getName());
        ArrayList<Junction> junctionList = simPanel.getJunctionList();

        ObjectOutputStream out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsoluteFile());
            out = new ObjectOutputStream(fos);
            out.writeObject(junctionList);
            currentFile = f;
        } catch (FileNotFoundException ex) {
            System.err.println("File not found. " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Unable to write to file." + ex.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                System.err.println("Unable to close file." + ex.getMessage());
            }
        }
    }

    private void loadMap(File file) {

        ObjectInputStream out = null;
        FileInputStream fos = null;
        try {
            fos = new FileInputStream(file.getAbsoluteFile());
            out = new ObjectInputStream(fos);
            ArrayList<Junction> junctionList = (ArrayList<Junction>) out.readObject();

            simPanel.setJunctionList(junctionList);
            mapFileLabel.setText("Map file: " + file.getName());
            currentFile = file;
        } catch (ClassNotFoundException ex) {
            System.err.println("Class not found. " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            System.err.println("File not found. " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Unable to write to file." + ex.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                System.err.println("Unable to close file." + ex.getMessage());
            }
        }
    }

private void saveMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMapButtonActionPerformed
    JFileChooser fc = new JFileChooser();
    fc.setFileFilter(new mapFileFilter());

    int returnVal = fc.showSaveDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        saveMap(fc.getSelectedFile());
    }
}//GEN-LAST:event_saveMapButtonActionPerformed

private void loadMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMapButtonActionPerformed
    JFileChooser fc = new JFileChooser();
    fc.setFileFilter(new mapFileFilter());

    int returnVal = fc.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        loadMap(file);
    }
}//GEN-LAST:event_loadMapButtonActionPerformed

private void saveDefaultMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDefaultMapActionPerformed
    saveMap(new File(defaultMapName));
}//GEN-LAST:event_saveDefaultMapActionPerformed

private void newMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMapButtonActionPerformed
    ArrayList<Junction> junctions = new ArrayList<Junction>();
    simPanel.setJunctionList(junctions);
}//GEN-LAST:event_newMapButtonActionPerformed

private void densityTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_densityTextActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_densityTextActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    double totalDensity = 0;
    int elements = 0;
    for (Junction j : simPanel.getJunctionList()) {
        for (Road r : j.getIncomingRoads()) {
            if (r != null) {
                for (Lane l : r.getLanes()) {
                    totalDensity += (l.getCars().size() / l.getLength());
                    elements++;
                }
            }
        }
        for (Road r : j.getOutgoingRoads()) {
            if (r != null) {
                for (Lane l : r.getLanes()) {
                    totalDensity += (l.getCars().size() / l.getLength());
                    elements++;
                }
            }
        }
    }
    double returnValue = totalDensity / elements;
    DecimalFormat twoDForm = new DecimalFormat("#.##");
    this.densityText.setText("" + twoDForm.format(returnValue));

}//GEN-LAST:event_jButton2ActionPerformed

    public static void setTotalLC(boolean shouldUpdate) {
        ControlPanel.updateTotalLC = shouldUpdate;
    }

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    Road.setMergeGap(Double.parseDouble(mergeGapText.getText()));
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    Road.setLCFactor(Double.parseDouble(LCFactorText.getText()));
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    Road.setTotalLC(0);
}//GEN-LAST:event_jButton4ActionPerformed

private void SpeedLimitDistanceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SpeedLimitDistanceTextFieldActionPerformed
    TimeBasedTrafficLightJunction.setSpeedLimitDist(Integer.parseInt(SpeedLimitDistanceTextField.getText()));
    TrafficLights.setSpeedLimitDist(Integer.parseInt(SpeedLimitDistanceTextField.getText()));
}//GEN-LAST:event_SpeedLimitDistanceTextFieldActionPerformed

private void orangeTimeTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_orangeTimeTextFocusLost
    TimeBasedTrafficLightJunction.setOrangeTime(Integer.parseInt(orangeTimeText.getText()));
    MarchingControl.setOrangeTime(Integer.parseInt(orangeTimeText.getText()));
}//GEN-LAST:event_orangeTimeTextFocusLost

private void greenTimeTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_greenTimeTextFocusLost
    TimeBasedTrafficLightJunction.setLightInterval(Integer.parseInt(greenTimeText.getText()));
    MarchingControl.setLightInterval(Integer.parseInt(greenTimeText.getText()));
}//GEN-LAST:event_greenTimeTextFocusLost

private void trafficLightJunctionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trafficLightJunctionButtonActionPerformed
// TODO add your handling code here:
    int noOfLanes = Integer.parseInt(JOptionPane.showInputDialog("Number of lanes", 4));
    simPanel.makeTrafficLightJunction(noOfLanes);
}//GEN-LAST:event_trafficLightJunctionButtonActionPerformed

private void createMapButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createMapButtonActionPerformed
    // TODO add your handling code here:
    simPanel.makeSmallMap();
}//GEN-LAST:event_createMapButtonActionPerformed
private void CarsSpawnedTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CarsSpawnedTextFieldActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_CarsSpawnedTextFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CarsSpawnedTextField;
    private javax.swing.JTextField LCFactorText;
    private javax.swing.JLabel SpawnCarsLabel;
    private javax.swing.JTextField SpeedLimitDistanceTextField;
    private javax.swing.JTextField averageSpeedTextField;
    private javax.swing.JButton createMapButton;
    private javax.swing.JTextField densityText;
    private javax.swing.JTextField greenTimeText;
    private javax.swing.JTextField intervalTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadMapButton;
    private javax.swing.JLabel mapFileLabel;
    private javax.swing.JTextField mergeGapText;
    private javax.swing.ButtonGroup modeGroup;
    private javax.swing.JButton newMapButton;
    private javax.swing.JTextField numberOfCarsTextField;
    private javax.swing.JTextField orangeTimeText;
    private javax.swing.JPanel paintOptionsPanel;
    private javax.swing.JRadioButton paintRadioButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveDefaultMap;
    private javax.swing.JButton saveMapButton;
    private javax.swing.JPanel simulationOptionsPanel;
    private javax.swing.JRadioButton simulationRadio;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JTextField timeStandingStillTextField;
    private javax.swing.JTextField totalLCText;
    private javax.swing.JButton trafficLightJunctionButton;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
