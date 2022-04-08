package fr.triedge.fm.model;

import fr.triedge.fm.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileInfo {

    private String name;
    private ArrayList<FilePath> paths = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FilePath> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<FilePath> paths) {
        this.paths = paths;
    }

    public void appendPathIfNotExist(File file){
        if (!pathContainsValue(file.getAbsolutePath())){
            FilePath fp = new FilePath();
            fp.setPath(file.getAbsolutePath());
            try {
                populateMetadata(fp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            getPaths().add(fp);
        }
    }

    public boolean isAllSameSize(){
        boolean res = true;
        long size = getPaths().get(0).getSize();
        for (FilePath p : getPaths()){
            if (p.getSize() != size){
                res = false;
                break;
            }
        }
        return res;
    }

    public void removePath(String path){
        getPaths().removeIf(p -> p.getPath().equals(path));
    }

    private void populateMetadata(FilePath fp) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(Paths.get(fp.getPath()), BasicFileAttributes.class);
        if (attr != null){
            fp.setCreationDate(new Date(attr.creationTime().toMillis()));
            fp.setModifiedDate(new Date(attr.lastModifiedTime().toMillis()));
            fp.setSize(attr.size());
        }
    }

    public boolean pathContainsValue(String path){
        for (FilePath fp : getPaths()){
            if (fp.getPath().equals(path))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        DecimalFormat f = new DecimalFormat("###,###,###");
        StringBuilder tmp = new StringBuilder();
        tmp.append("-- FileInfo --------------------\r\n");
        tmp.append(name).append("\r\n");
        getPaths().stream().forEach(s -> tmp.append(" -> [").append(f.format(s.getSize())).append("] ").append(s.getPath()).append("\r\n"));
        tmp.append("\r\n");
        return tmp.toString();
    }
}
