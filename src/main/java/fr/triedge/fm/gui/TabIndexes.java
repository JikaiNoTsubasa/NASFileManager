package fr.triedge.fm.gui;

import fr.triedge.fm.controller.ProgramController;
import fr.triedge.fm.model.FileInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class TabIndexes extends JPanel {

    private static final Logger log = LogManager.getLogger(TabIndexes.class);

    private JTree tree;
    private ProgramController controller;
    private JPanelFileInfo panelFileInfo;

    public TabIndexes(ProgramController controller) {
        setLayout(new BorderLayout());
        setController(controller);
        refreshTree();
    }

    public void refreshTree(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        if (getController() != null){
            log.debug("Displaying tree with "+getController().getModel().getDuplicatesFileInfos().size()+" elements");
            getController().getModel().getDuplicatesFileInfos().forEach(e -> {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(e);
                root.add(node);
            });
        }
        setTree(new JTree(root));
        getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        getTree().setCellRenderer(new FileInfoTreeCellRenderer());
        getTree().addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)getTree().getLastSelectedPathComponent();
            if (node.getUserObject() instanceof FileInfo){
                FileInfo info = (FileInfo) node.getUserObject();
                getPanelFileInfo().refreshPanel(info, Integer.parseInt(controller.getConfig().getProperty(ProgramController.CONF_IMG_SIZE, "64")));
            }
        });
        setPanelFileInfo(new JPanelFileInfo(this));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(getTree()), getPanelFileInfo());
        split.setDividerLocation(200);
        removeAll();
        add(split, BorderLayout.CENTER);
        getTree().validate();
    }

    private void updateRightPanel(FileInfo info){

    }

    public JTree getTree() {
        return tree;
    }

    public void setTree(JTree tree) {
        this.tree = tree;
    }

    public ProgramController getController() {
        return controller;
    }

    public void setController(ProgramController controller) {
        this.controller = controller;
    }

    public JPanelFileInfo getPanelFileInfo() {
        return panelFileInfo;
    }

    public void setPanelFileInfo(JPanelFileInfo panelFileInfo) {
        this.panelFileInfo = panelFileInfo;
    }
}
