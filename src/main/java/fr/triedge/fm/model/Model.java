package fr.triedge.fm.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Model {

    private HashMap<String, FileInfo> fileInfos = new HashMap<>();

    public HashMap<String, FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(HashMap<String, FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public ArrayList<FileInfo> getDuplicatesFileInfos(){
        ArrayList<FileInfo> array = new ArrayList<>();
        getFileInfos().forEach((k,v)->{
            if (v.getPaths().size() >1)
                array.add(v);
        });
        return array;
    }
}
