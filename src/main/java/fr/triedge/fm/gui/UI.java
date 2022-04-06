package fr.triedge.fm.gui;

import javax.swing.*;
import java.awt.*;

public class UI {

    public static void showInfo(Component parent,String title, String message){
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent,String title, String message){
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showYesNo(Component parent, String title, String message){
        int res = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return res==JOptionPane.YES_OPTION;
    }
}
