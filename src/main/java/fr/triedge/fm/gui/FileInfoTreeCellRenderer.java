package fr.triedge.fm.gui;

import fr.triedge.fm.model.FileInfo;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class FileInfoTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode ) value;
        if (node.getUserObject() instanceof FileInfo){
            FileInfo nodeObject = (FileInfo) node.getUserObject();
            return super.getTreeCellRendererComponent(tree,
                    nodeObject.getName(),
                    selected,
                    expanded,
                    leaf,
                    row,
                    hasFocus);

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
