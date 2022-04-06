package fr.triedge.fm.gui;

import fr.triedge.fm.model.FileInfo;

import javax.swing.*;
import java.awt.*;

public class JPanelFileInfo extends JPanel {

    private FileInfo fileInfo;
    private TabIndexes tabIndexes;

    public JPanelFileInfo(TabIndexes tabIndexes){
        setLayout(new BorderLayout());
        setTabIndexes(tabIndexes);
    }

    public void refreshPanel(FileInfo info, int imgSize){
        setFileInfo(info);
        if (getFileInfo() != null){
            removeAll();
            add(new FileInfoComponent(getFileInfo(), imgSize, getTabIndexes()), BorderLayout.CENTER);
            validate();
        }
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public TabIndexes getTabIndexes() {
        return tabIndexes;
    }

    public void setTabIndexes(TabIndexes tabIndexes) {
        this.tabIndexes = tabIndexes;
    }
}
