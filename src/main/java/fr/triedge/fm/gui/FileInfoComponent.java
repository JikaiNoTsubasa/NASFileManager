package fr.triedge.fm.gui;

import fr.triedge.fm.model.FileInfo;
import fr.triedge.fm.utils.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

public class FileInfoComponent extends JPanel {

    private FileInfo fileInfo;
    private int imgSize;
    private TabIndexes tabIndexes;

    public FileInfoComponent(FileInfo fileInfo, int imgSize, TabIndexes tabIndexes){
        setFileInfo(fileInfo);
        setTabIndexes(tabIndexes);
        setImgSize(imgSize);
        build();
    }

    public void build(){
        setLayout(new BorderLayout());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String[] columns = {
                "Image", "Path", "Size", "Creation Date", "Update Date"
        };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(){
            public Class getColumnClass(int column) {
                return (column == 0) ? Icon.class : Object.class;
            }
        };
        table.setModel(model);
        table.setRowHeight(getImgSize());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        getFileInfo().getPaths().forEach(p -> {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon_empty.png"));
            try {
                ImageIcon tmp = createImageFromPath(p.getPath());
                if (tmp != null)
                    icon = tmp;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String creationdate = "No Date";
            if (p.getCreationDate() != null)
                creationdate = sdf.format(p.getCreationDate());
            String modifiedDate = "No Date";
            if (p.getModifiedDate() != null)
                modifiedDate = sdf.format(p.getModifiedDate());
            model.addRow(new Object[]{
                    icon,
                    p.getPath(),
                    p.getSize(),
                    creationdate,
                    modifiedDate
            });
        });
        //table.getColumnModel().getColumn(0).setPreferredWidth(getImgSize());
        add( new JScrollPane(table), BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        JLabel titleLabel = new JLabel(getFileInfo().getName());
        toolBar.add(titleLabel);
        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            int rowId = table.getSelectedRow();
            if (rowId >= 0){
                String selectedPath = table.getModel().getValueAt(rowId, 1).toString();
                if (UI.showYesNo(this, "Delete File?", "Are you sure you want to delete the following?\r\n"+selectedPath)){
                    //System.out.println("Delete: "+selectedPath);
                    try {
                        boolean success = Files.deleteIfExists(Paths.get(selectedPath));
                        if (success){
                            getFileInfo().removePath(selectedPath);
                            getTabIndexes().getController().storeIndexes();
                            getTabIndexes().refreshTree();
                            UI.showInfo(this, "INFO", "Successfully deleted\r\n"+selectedPath+"\r\nPlease re-index");
                        }else{
                            UI.showError(this, "ERROR", "Cannot delete file\r\n"+selectedPath);
                        }
                    } catch (IOException ex) {
                        UI.showError(this, "ERROR", "Cannot delete file\r\n"+selectedPath+"\r\n"+ex.getMessage());
                    }
                }
            }
        });
        toolBar.add(new JSeparator());
        toolBar.add(btnDelete);
        add(toolBar, BorderLayout.NORTH);
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    private String convertPath(String path){
        return path.replaceAll("\\\\+","\\\\\\\\");
    }

    private ImageIcon createImageFromPath(String path) throws IOException {
        if (Utils.isImage(path)) {
            ImageIcon tmp = new ImageIcon(resizeImg(loadImage(path), getImgSize(), getImgSize()));
            return tmp;
        }
        return null;
    }/*
    private ImageIcon resizeImage(ImageIcon old, int width, int height){
        Image img = old.getImage();
        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        return new ImageIcon(bi);
    }
    */

    private BufferedImage resizeImg(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public int getImgSize() {
        return imgSize;
    }

    public void setImgSize(int imgSize) {
        this.imgSize = imgSize;
    }

    public TabIndexes getTabIndexes() {
        return tabIndexes;
    }

    public void setTabIndexes(TabIndexes tabIndexes) {
        this.tabIndexes = tabIndexes;
    }
}
