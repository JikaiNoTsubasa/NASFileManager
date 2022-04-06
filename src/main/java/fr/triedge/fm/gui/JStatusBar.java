package fr.triedge.fm.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class JStatusBar extends JPanel {

    private JProgressBar progressBar;
    private JLabel statusLabel;

    public JStatusBar(JFrame frame){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        if (frame!=null){
            setPreferredSize(new Dimension(frame.getWidth(), 20));
        }
        setProgressBar(new JProgressBar(JProgressBar.HORIZONTAL));
        getProgressBar().setPreferredSize(new Dimension(100, 20));
        setStatusLabel(new JLabel());
        getStatusLabel().setHorizontalAlignment(SwingConstants.LEFT);
        add(getProgressBar());
        add(getStatusLabel());
    }

    public void setProgress(int value, int max){
        getProgressBar().setMaximum(max);
        getProgressBar().setValue(value);
    }

    public void setProgress(boolean isRunning){
        getProgressBar().setIndeterminate(isRunning);
    }

    public void setText(String text){
        getStatusLabel().setText(text);
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }
}
