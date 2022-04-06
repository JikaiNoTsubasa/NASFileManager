package fr.triedge.fm;

import fr.triedge.fm.controller.ProgramController;
import fr.triedge.fm.gui.MainWindow;

import javax.swing.*;

public class RunNASFileManager {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ProgramController c = new ProgramController();
                c.start();
            }
        });
    }
}
