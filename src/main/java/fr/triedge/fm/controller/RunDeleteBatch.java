package fr.triedge.fm.controller;

import fr.triedge.fm.gui.UI;
import fr.triedge.fm.model.FileInfo;
import fr.triedge.fm.model.FilePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RunDeleteBatch implements Runnable{

    private static final Logger log = LogManager.getLogger(RunDeleteBatch.class);

    private ProgramController controller;
    private int totalRemovedCount = 0;

    public RunDeleteBatch(ProgramController controller){
        setController(controller);
    }

    @Override
    public void run() {
        log.debug("Starting bacth delete job...");
        ArrayList<FileInfo> files = getController().getModel().getDuplicatesFileInfos();
        int cnt = 1;
        if (files.size() > 0){
            for (FileInfo fi : files){
                getController().updateProgressBar(cnt++, files.size());
                getController().updateStatusLabel("Removing files");
                removeDuplicatePaths(fi);

            }
            getController().updateProgressBar(0, 100);
            getController().updateStatusLabel("Ready");
            getController().storeIndexes();
        }else{
            log.debug("No files to delete");
        }
        log.debug("Delete batch finished");
        UI.showInfo(getController().getMainWindow(), "Batch Removing Process", "Total removed files: "+totalRemovedCount);
    }

    private void removeDuplicatePaths(FileInfo info){
        Collections.sort(info.getPaths());
        List<FilePath> paths = new ArrayList<FilePath>(info.getPaths().subList(1, info.getPaths().size()));
        paths.forEach(p -> {
            try {
                if (info.getPaths().size() >1){
                    boolean success = Files.deleteIfExists(Paths.get(p.getPath()));
                    if (success){
                        info.removePath(p.getPath());
                        log.debug("Removed "+p.getPath());
                        totalRemovedCount++;
                    }

                }
            } catch (IOException e) {
                log.error("Failed to remove "+p.getPath());
            }

        });
    }

    public ProgramController getController() {
        return controller;
    }

    public void setController(ProgramController controller) {
        this.controller = controller;
    }
}
