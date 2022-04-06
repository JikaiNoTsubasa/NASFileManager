package fr.triedge.fm.controller;

import fr.triedge.fm.gui.MainWindow;
import fr.triedge.fm.gui.TabIndexes;
import fr.triedge.fm.gui.UI;
import fr.triedge.fm.model.Model;
import fr.triedge.fm.utils.Storage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Properties;

public class ProgramController {

    private static final Logger log = LogManager.getLogger(ProgramController.class);

    public static final String CONF_FILE                = "config/config.properties";
    public static final String CONF_DATA                = "fm.config.data";
    public static final String CONF_INDEX               = "fm.config.index";
    public static final String CONF_IMG_SIZE            = "fm.config.img.size";

    private MainWindow mainWindow;
    private Model model;
    private Properties config;

    public void start(){
        log.debug("Starting controller...");
        model = new Model();
        mainWindow = new MainWindow(this);
        try {
            loadConfig();
            File idx = new File(getConfig().getProperty(CONF_INDEX));
            if (idx.exists())
                loadIndexes();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        buildMainWindow();
        log.debug("Controller started");
    }

    public void openTabIndexes(){
        DecimalFormat f = new DecimalFormat("###,###,###");
        addTab("Indexes ("+f.format(getModel().getDuplicatesFileInfos().size())+")", new TabIndexes(this));
    }

    public void setFolderToIndex(File folder){
        getConfig().setProperty(CONF_DATA, folder.getAbsolutePath());
        try {
            storeConfig();
        } catch (IOException e) {
            log.error("Failed to store config");
        }
    }

    public void clearIndexes(){
        getModel().setFileInfos(new HashMap<>());
        storeIndexes();
        UI.showInfo(getMainWindow(), "Clear Indexes", "Indexes cleared");
    }

    private void storeConfig() throws IOException {
        if (getConfig() != null){
            FileOutputStream fos = new FileOutputStream(CONF_FILE);
            getConfig().store(fos, "");
            fos.close();
        }
    }

    private void loadConfig() throws IOException {
        File f = new File(CONF_FILE);
        if (f.exists()){
            config = new Properties();
            FileInputStream fis = new FileInputStream(CONF_FILE);
            config.load(fis);
            fis.close();
        }else{
            createDefaultConfig();
        }
    }

    private void createDefaultConfig() throws IOException {
        File file = new File(CONF_FILE);
        file.getParentFile().mkdirs();
        setConfig(new Properties());
        getConfig().setProperty(CONF_DATA,"data");
        getConfig().setProperty(CONF_INDEX,"data/indexes.json");
        storeConfig();
    }

    public void addTab(String title, JComponent comp){
        getMainWindow().getTabbedPane().addTab(title, comp);
    }

    public void indexFiles() throws NoSuchFieldException {
        File root = new File(config.getProperty(CONF_DATA));
        log.debug("Indexing files from path "+root.getAbsolutePath());
        if (!root.exists()){
            throw new NoSuchFieldException("Could not found root folder "+root.getAbsolutePath());
        }
        Thread th = new Thread(new RunIndexing(this, root.getAbsolutePath()));
        th.setName("Indexer");
        th.start();
    }

    public void loadIndexes() throws FileNotFoundException {
        File file = new File(config.getProperty(CONF_INDEX));
        log.debug("Loading indexes from file "+file.getAbsolutePath());
        if (!file.exists()){
            log.warn("No indexes found in "+file.getAbsolutePath());
        }else{
            updateProgressBar(true);
            updateStatusLabel("Loading indexes...");
            getModel().setFileInfos(Storage.loadIndexes(file.getPath()));
            updateStatusLabel("Loaded "+getModel().getFileInfos().size()+" indexes");
            log.debug("Loaded "+getModel().getFileInfos().size()+" indexes");
            updateProgressBar(false);
        }
    }

    public void storeIndexes(){
        //getModel().getFileInfos().forEach((k,v) -> log.debug(v));
        File file = new File(config.getProperty(CONF_INDEX));
        if (!file.exists()){
            file.getParentFile().mkdirs();
        }
        try {
            log.debug("Storing to "+file.getAbsolutePath());
            Storage.storeJson(getModel().getFileInfos(), file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateProgressBar(boolean isRunning){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getMainWindow().getStatusBar().setProgress(isRunning);
            }
        });
    }

    public void updateProgressBar(int value, int max){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getMainWindow().getStatusBar().setProgress(value, max);
            }
        });
    }

    public void updateStatusLabel(String text){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getMainWindow().getStatusBar().setText(text);
            }
        });
    }

    private void buildMainWindow(){
        mainWindow.buildDefault();
    }

    public void quitProgram(){
        System.exit(0);
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }
}
