/*********************************************************************
 * Massive Photo Uploader: Upload a batch of albums to facebook
 * Copyright (C) 2010  Johannes Kählare
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************/

/*
 * MassivePhotoUploaderView.java
 */
package massivephotouploader;

import java.io.IOException;
import java.net.URISyntaxException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import swe.joynes.preparator.Prepare;
import swe.joynes.uploader.BatchUploader;
import swe.joynes.uploader.Init;
import uk.me.phillsacre.Constants;
import uk.me.phillsacre.PropertyUtils;

/**
 * The application's main frame.
 */
public class MassivePhotoUploaderView extends FrameView {

    private Prepare albums = null;
    static public PropertyUtils properties;
    private String tutorialUrl = "http://joynes.se/applic/massivePhotoUploaderFacebook/tutorial.php";

    public MassivePhotoUploaderView(SingleFrameApplication app) {
        super(app);

        initComponents();

        properties = new PropertyUtils();
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        jButtonUpload.setEnabled(false);

        jComboBoxVisibility.setSelectedItem(properties.getProperty(Constants.Properties.VISIBILITY));
        jComboBoxResize.setSelectedItem(properties.getProperty(Constants.Properties.MAX_DIMENSION));

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        
        jTextAreaConsole.setText(jTextAreaConsole.getText() + 
                "Massive Photo Uploader is back again as version 1.4!\nI just made enough to make it work again. The difference is that you need to set the visibility part in the app preferences.\n\nMassive Photo Uploader Copyright (C) 2010  Johannes Kählare\n" +
                "This program comes with ABSOLUTELY NO WARRANTY.\n" +
                "This is free software, and you are welcome to redistribute it under certain conditions.");
        jTextAreaConsole.setCaretPosition(jTextAreaConsole.getDocument().getLength());

        
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MassivePhotoUploaderApp.getApplication().getMainFrame();
            aboutBox = new MassivePhotoUploaderAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MassivePhotoUploaderApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaConsole = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabelAlbumCount = new javax.swing.JLabel();
        jLabelImageCount = new javax.swing.JLabel();
        jProgressBarMeter = new javax.swing.JProgressBar();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jButtonBrowse = new javax.swing.JButton();
        jButtonUpload = new javax.swing.JButton();
        jComboBoxVisibility = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxResize = new javax.swing.JComboBox();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem Instructions = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem1 = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(massivephotouploader.MassivePhotoUploaderApp.class).getContext().getResourceMap(MassivePhotoUploaderView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setForeground(resourceMap.getColor("mainPanel.foreground")); // NOI18N
        mainPanel.setFont(resourceMap.getFont("mainPanel.font")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(509, 600));
        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(20, 180));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );

        mainPanel.add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setMinimumSize(new java.awt.Dimension(20, 100));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(20, 180));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );

        mainPanel.add(jPanel2, java.awt.BorderLayout.LINE_END);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextAreaConsole.setBackground(resourceMap.getColor("jTextAreaConsole.background")); // NOI18N
        jTextAreaConsole.setColumns(20);
        jTextAreaConsole.setEditable(false);
        jTextAreaConsole.setFont(resourceMap.getFont("jTextAreaConsole.font")); // NOI18N
        jTextAreaConsole.setLineWrap(true);
        jTextAreaConsole.setRows(5);
        jTextAreaConsole.setToolTipText(resourceMap.getString("jTextAreaConsole.toolTipText")); // NOI18N
        jTextAreaConsole.setName("jTextAreaConsole"); // NOI18N
        jScrollPane1.setViewportView(jTextAreaConsole);

        mainPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(268, 100));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setPreferredSize(new java.awt.Dimension(20, 100));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel4, java.awt.BorderLayout.LINE_START);

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(20, 100));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel5, java.awt.BorderLayout.LINE_END);

        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setLayout(new java.awt.BorderLayout());

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(resourceMap.getIcon("jLabel3.icon")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jPanel6.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel8.setName("jPanel8"); // NOI18N
        jPanel8.setPreferredSize(new java.awt.Dimension(549, 30));

        jLabelAlbumCount.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabelAlbumCount.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabelAlbumCount.setToolTipText(resourceMap.getString("jLabel1.toolTipText")); // NOI18N
        jLabelAlbumCount.setName("jLabel1"); // NOI18N

        jLabelImageCount.setFont(resourceMap.getFont("jLabelImageCount.font")); // NOI18N
        jLabelImageCount.setText(resourceMap.getString("jLabelImageCount.text")); // NOI18N
        jLabelImageCount.setToolTipText(resourceMap.getString("jLabelImageCount.toolTipText")); // NOI18N
        jLabelImageCount.setName("jLabelImageCount"); // NOI18N

        jProgressBarMeter.setName("jProgressBarMeter"); // NOI18N

        jLabel4.setFont(resourceMap.getFont("jLabel4.font")); // NOI18N
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setToolTipText(resourceMap.getString("jLabel4.toolTipText")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabelAlbumCount, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelImageCount, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBarMeter, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jProgressBarMeter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelAlbumCount)
                        .addComponent(jLabelImageCount)
                        .addComponent(jLabel4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jPanel3.add(jPanel6, java.awt.BorderLayout.CENTER);

        mainPanel.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel7.setName("jPanel7"); // NOI18N
        jPanel7.setPreferredSize(new java.awt.Dimension(509, 80));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(massivephotouploader.MassivePhotoUploaderApp.class).getContext().getActionMap(MassivePhotoUploaderView.class, this);
        jButtonBrowse.setAction(actionMap.get("ChooseFolder")); // NOI18N
        jButtonBrowse.setFont(resourceMap.getFont("jButtonBrowse.font")); // NOI18N
        jButtonBrowse.setText(resourceMap.getString("jButtonBrowse.text")); // NOI18N
        jButtonBrowse.setToolTipText(resourceMap.getString("jButtonBrowse.toolTipText")); // NOI18N
        jButtonBrowse.setName("jButtonBrowse"); // NOI18N

        jButtonUpload.setAction(actionMap.get("UploadImages")); // NOI18N
        jButtonUpload.setFont(resourceMap.getFont("jButtonUpload.font")); // NOI18N
        jButtonUpload.setText(resourceMap.getString("jButtonUpload.text")); // NOI18N
        jButtonUpload.setToolTipText(resourceMap.getString("jButtonUpload.toolTipText")); // NOI18N
        jButtonUpload.setName("jButtonUpload"); // NOI18N

        jComboBoxVisibility.setFont(resourceMap.getFont("jComboBoxVisibility.font")); // NOI18N
        jComboBoxVisibility.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Not working anymore!" }));
        jComboBoxVisibility.setToolTipText(resourceMap.getString("jComboBoxVisibility.toolTipText")); // NOI18N
        jComboBoxVisibility.setAction(actionMap.get("setVisibility")); // NOI18N
        jComboBoxVisibility.setName("jComboBoxVisibility"); // NOI18N

        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setToolTipText(resourceMap.getString("jLabel5.toolTipText")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setFont(resourceMap.getFont("jLabel6.font")); // NOI18N
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setToolTipText(resourceMap.getString("jLabel6.toolTipText")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jComboBoxResize.setFont(resourceMap.getFont("jComboBoxResize.font")); // NOI18N
        jComboBoxResize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "1920", "1680", "1280", "1024", "800" }));
        jComboBoxResize.setToolTipText(resourceMap.getString("jComboBoxResize.toolTipText")); // NOI18N
        jComboBoxResize.setAction(actionMap.get("setResize")); // NOI18N
        jComboBoxResize.setName("jComboBoxResize"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButtonBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jComboBoxVisibility, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxResize, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jButtonUpload, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonUpload, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6))
                            .addGap(10, 10, 10)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBoxVisibility, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBoxResize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jButtonBrowse, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        Instructions.setAction(actionMap.get("showTutorial")); // NOI18N
        Instructions.setText(resourceMap.getString("Instructions.text")); // NOI18N
        Instructions.setToolTipText(resourceMap.getString("Instructions.toolTipText")); // NOI18N
        Instructions.setName("Instructions"); // NOI18N
        helpMenu.add(Instructions);

        aboutMenuItem1.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem1.setName("aboutMenuItem1"); // NOI18N
        helpMenu.add(aboutMenuItem1);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 379, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public Task ChooseFolder() {
        return new ChooseFolderTask(getApplication());
    }

    private class ChooseFolderTask extends org.jdesktop.application.Task<Object, Void> {

        ChooseFolderTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to ChooseFolderTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.

            try {
                JFrame frame = new JFrame();

                // Get path from config

                String lastDir = properties.getProperty(Constants.Properties.LAST_DIRECTORY);

                JFileChooser fc = new JFileChooser(new File(lastDir));
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                fc.showOpenDialog(frame);

                // Get the selected file
                File file = fc.getSelectedFile();

                if (file != null && file.exists()) {

                    properties.setProperty(Constants.Properties.LAST_DIRECTORY, file.getAbsolutePath());

                    albums = new Prepare(file.getAbsolutePath());
                    jTextAreaConsole.setText(albums.print());

                    jLabelAlbumCount.setText("Albums: " + String.valueOf(albums.getAlbums().size()));
                    jLabelImageCount.setText("Images: " + String.valueOf(albums.getPhotoSize()));

                    jButtonUpload.setEnabled(true);

                }
            } catch (Exception ex) {
                Logger.getLogger(MassivePhotoUploaderView.class.getName()).log(Level.WARNING, null, ex);
                alert("Error: " + ex.getMessage());
            }
            return 0;
        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
        /**
         * POPUP a message alert
         * @param str
         */
    }

    @Action
    public Task UploadImages() {
        return new UploadImagesTask(getApplication());
    }

    private class UploadImagesTask extends org.jdesktop.application.Task<Object, Void> {

        UploadImagesTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to UploadImagesTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.

            try {
                jButtonBrowse.setEnabled(false);
                jButtonUpload.setEnabled(false);
                jComboBoxResize.setEnabled(false);
                jComboBoxVisibility.setEnabled(false);
                jProgressBarMeter.setValue(0);
                if (albums != null && albums.getAlbums().size() > 0) {

                    Init init = new Init(jTextAreaConsole, MassivePhotoUploaderApp.getApplication().getMainFrame());

                    if (!init.connect()) {
                        alert("Press ok when logged in to facebook");
                    }
                    init.authenticate();

                    // start uploading in a new thread
                    new BatchUploader(init.getFacebookClient(), albums, jTextAreaConsole,
                            MassivePhotoUploaderApp.getApplication().getMainFrame(), jProgressBarMeter);

                } else {
                    alert("You have not selected a valid album!");
                }
            } catch (Exception ex) {
                Logger.getLogger(MassivePhotoUploaderView.class.getName()).log(Level.WARNING, null, ex);
                if (ex.getMessage().contains("Session key invalid or")) {
                    alert("Your Session has expired. Please click 'Upload' again to obtain a new one!");
                } else {
                    alert("Error: " + ex.getMessage());
                }
            }

            jButtonBrowse.setEnabled(true);
            jButtonUpload.setEnabled(true);
            jComboBoxResize.setEnabled(true);
            jComboBoxVisibility.setEnabled(true);
            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
    }

    public void alert(String str) {
        JOptionPane.showMessageDialog(MassivePhotoUploaderApp.getApplication().getMainFrame(), str);
    }

    @Action
    public void setVisibility() {
        properties.setProperty(Constants.Properties.VISIBILITY, jComboBoxVisibility.getSelectedItem().toString());
    }

    @Action
    public void setResize() {
        properties.setProperty(Constants.Properties.MAX_DIMENSION, jComboBoxResize.getSelectedItem().toString());
    }

    @Action
    public void showTutorial() throws URISyntaxException {
        try {
            java.awt.Desktop.getDesktop().browse(new URI(tutorialUrl));
        } catch (IOException ex) {
            alert("Could not open your browser");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JButton jButtonUpload;
    private javax.swing.JComboBox jComboBoxResize;
    private javax.swing.JComboBox jComboBoxVisibility;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelAlbumCount;
    private javax.swing.JLabel jLabelImageCount;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JProgressBar jProgressBarMeter;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaConsole;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
}
