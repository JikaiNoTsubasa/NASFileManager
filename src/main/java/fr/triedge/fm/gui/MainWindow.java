package fr.triedge.fm.gui;

import fr.triedge.fm.controller.ProgramController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {

    private static final Logger log = LogManager.getLogger(MainWindow.class);

    private ProgramController controller;
    private JTabbedPaneCloseButton tabbedPane;
    private JStatusBar statusBar;

    public MainWindow(ProgramController controller){
        super("NAS File Manager");
        this.controller = controller;
    }

    public void buildDefault(){
        JPanel pan = new JPanel(new BorderLayout());
        setContentPane(pan);
        setTabbedPane(new JTabbedPaneCloseButton());
        pan.add(getTabbedPane(), JTabbedPane.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setLocationRelativeTo(null);
        setSize(800, 600);
        JMenu menuFile = new JMenu("File");
        JMenuItem itemQuit = new JMenuItem("Quit");
        itemQuit.addActionListener(e -> {
            controller.quitProgram();
        });

        menuFile.add(new JSeparator());
        menuFile.add(itemQuit);

        JMenu menuIndex = new JMenu("Index");
        JMenuItem itemIndex = new JMenuItem("Run Indexing");
        itemIndex.addActionListener(e -> {
            try {
                controller.indexFiles();
            } catch (NoSuchFieldException ex) {
                ex.printStackTrace();
            }
        });
        menuIndex.add(itemIndex);

        JMenuItem itemManageIndex = new JMenuItem("Manage Indexes");
        itemManageIndex.addActionListener(e -> {
            controller.openTabIndexes();
        });
        menuIndex.add(itemManageIndex);

        JMenuItem itemChangeIndexFolder = new JMenuItem("Setup Folder to Index");
        itemChangeIndexFolder.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File(controller.getConfig().getProperty(ProgramController.CONF_DATA),"."));
            chooser.setDialogTitle("Select Folder to Index");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                if (chooser.getSelectedFile() != null){
                    controller.setFolderToIndex(chooser.getSelectedFile());
                    UI.showInfo(this, "Change Folder to Index", "Selected folder: "+chooser.getSelectedFile().getAbsolutePath());
                }
            }
            else {
                log.warn("Setup Folder to Index: no folder selected");
            }
        });
        menuIndex.add(itemChangeIndexFolder);

        JMenuItem itemRunDelete = new JMenuItem("Run Batch Delete");
        itemRunDelete.addActionListener(e -> controller.deleteBatch());
        menuIndex.add(itemRunDelete);

        JMenuItem itemClearIndex = new JMenuItem("Clear Indexes");
        itemClearIndex.addActionListener(e -> controller.clearIndexes());
        menuIndex.add(new JSeparator());
        menuIndex.add(itemClearIndex);

        JMenuBar bar = new JMenuBar();

        bar.add(menuFile);
        bar.add(menuIndex);
        setJMenuBar(bar);

        // Status bar
        setStatusBar(new JStatusBar(this));
        add(getStatusBar(), BorderLayout.SOUTH);

        setVisible(true);
        getStatusBar().setText("Ready");
    }

    public JTabbedPaneCloseButton getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPaneCloseButton tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public JStatusBar getStatusBar() {
        return statusBar;
    }

    public void setStatusBar(JStatusBar statusBar) {
        this.statusBar = statusBar;
    }
}
