package fr.triedge.fm.model;

import java.util.Date;

public class FilePath implements Comparable<FilePath>{

    private String path;
    private long size;
    private Date creationDate;
    private Date modifiedDate;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public int compareTo(FilePath o) {
        return this.getCreationDate().compareTo(o.getCreationDate());
    }
}
