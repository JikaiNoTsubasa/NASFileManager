package fr.triedge.fm.controller;

import fr.triedge.fm.gui.UI;
import fr.triedge.fm.model.FileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Arrays;

public class RunIndexing implements Runnable{

    private static final Logger log = LogManager.getLogger(RunIndexing.class);

    private ProgramController controller;
    private String root;
    private int indexCount;
    private int currentIndexing;

    public RunIndexing(ProgramController controller, String root){
        this.controller = controller;
        this.root = root;
    }

    @Override
    public void run() {
        if (root == null)
            return;

        log.debug("Starting indexing...");

        indexCount = 0;
        currentIndexing = 0;
        File rootFile = new File(root);

        controller.updateProgressBar(true);
        controller.updateStatusLabel("Calculating indexes...");
        calculateIndexes(rootFile);
        controller.updateProgressBar(false);

        controller.updateStatusLabel("Indexing...");
        processIndex(rootFile);
        log.debug("Indexing finished");
        // Refresh display
        // Store indexes
        log.debug("Starting storing indexes...");
        controller.updateStatusLabel("Storing Indexes...");
        controller.storeIndexes();
        controller.updateStatusLabel("Ready");
        controller.updateProgressBar(0,100);
        log.debug("Storing indexes finished");
        UI.showInfo(controller.getMainWindow(), "Indexing", "Indexing finished");
    }

    public void calculateIndexes(File file){
        if (file.isDirectory()){
            Arrays.stream(file.listFiles()).forEach(e -> calculateIndexes(e));
        }else{
            indexCount++;
        }
    }

    public void processIndex(File file){
        if (file.isDirectory()){
            Arrays.stream(file.listFiles()).forEach(e -> processIndex(e));
        }else{
            log.debug("Indexing file "+file.getAbsolutePath()+" ["+currentIndexing+"/"+indexCount+"]");
            if (controller.getModel().getFileInfos().containsKey(file.getName())){
                FileInfo info = controller.getModel().getFileInfos().get(file.getName());
                info.appendPathIfNotExist(file);
            }else{
                FileInfo info = new FileInfo();
                info.setName(file.getName());
                info.appendPathIfNotExist(file);
                controller.getModel().getFileInfos().put(info.getName(), info);
            }
            currentIndexing++;
            controller.updateProgressBar(currentIndexing, indexCount);
            controller.updateStatusLabel("Indexing "+currentIndexing+"/"+indexCount);
        }
    }
}
