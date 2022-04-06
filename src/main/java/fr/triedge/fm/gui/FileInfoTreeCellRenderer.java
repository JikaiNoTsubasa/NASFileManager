package fr.triedge.fm.gui;

import fr.triedge.fm.model.FileInfo;
import fr.triedge.fm.utils.Utils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.IOException;

public class FileInfoTreeCellRenderer extends DefaultTreeCellRenderer {

    public FileInfoTreeCellRenderer(){
        setClosedIcon(new ImageIcon(getClass().getResource("/icon_folder_closed_24.png")));
        setOpenIcon(new ImageIcon(getClass().getResource("/icon_folder_opened_24.png")));
        setLeafIcon(new ImageIcon(getClass().getResource("/icon_document_24.png")));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = new JLabel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode ) value;
        if (node.getUserObject() instanceof FileInfo){
            FileInfo nodeObject = (FileInfo) node.getUserObject();
            label.setText(nodeObject.getName());
            try {
                if (Utils.isImage(nodeObject.getPaths().get(0).getPath())){
                    label.setIcon(new ImageIcon(getClass().getResource("/icon_image_24.png")));
                }else{
                    label.setIcon(new ImageIcon(getClass().getResource("/icon_document_24.png")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return label;

        }else{
            return super.getTreeCellRendererComponent(tree,
                    value,
                    selected,
                    expanded,
                    leaf,
                    row,
                    hasFocus);
        }
    }
}
