package GUI;

import java.awt.BorderLayout;

public class MainFrame extends javax.swing.JFrame {

    private SimulationPanel simulationPan;
    private ControlPanel controlPan;
    private double interval = 40; // Update interval in ms.

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
        simulationPanel.setLayout(new BorderLayout());
        simulationPan = new SimulationPanel(simulationPanel.getWidth(), simulationPanel.getHeight(), 10000, 1000);
        //simulationPan.setBounds(0, 0, simulationPanel.getWidth(), simulationPanel.getHeight());
        simulationPanel.add(simulationPan, BorderLayout.CENTER);
//        simulationPan.makeMap();
        controlPanel.setLayout(new BorderLayout());
        controlPan = new ControlPanel(simulationPan);
        controlPanel.add(controlPan, BorderLayout.CENTER);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        simulationPanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        simulationPanel.setMaximumSize(new java.awt.Dimension(500, 500));
        simulationPanel.setMinimumSize(new java.awt.Dimension(500, 500));
        simulationPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        simulationPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                simulationPanelComponentResized(evt);
            }
        });

        org.jdesktop.layout.GroupLayout simulationPanelLayout = new org.jdesktop.layout.GroupLayout(simulationPanel);
        simulationPanel.setLayout(simulationPanelLayout);
        simulationPanelLayout.setHorizontalGroup(
            simulationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 746, Short.MAX_VALUE)
        );
        simulationPanelLayout.setVerticalGroup(
            simulationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 738, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout controlPanelLayout = new org.jdesktop.layout.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 425, Short.MAX_VALUE)
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 738, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(simulationPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 746, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(controlPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, controlPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, simulationPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void simulationPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_simulationPanelComponentResized

    }//GEN-LAST:event_simulationPanelComponentResized

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel simulationPanel;
    // End of variables declaration//GEN-END:variables
}
