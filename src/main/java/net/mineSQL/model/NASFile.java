package net.mineSQL.model;

import java.io.File;

public class NASFile {

    private static final long serialVersionUID = 8786163367689757617L;
    private File file;
    private String owner;

    public NASFile(File file, String owner) {
	this.file = file;
	this.owner = owner;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
