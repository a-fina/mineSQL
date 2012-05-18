package net.mineSQL.util;

import net.mineSQL.model.NASFile;

import java.util.Comparator;

public class FileComparator implements Comparator {
    private String sortField = "name";
    private String sortDir = "ASC";

    public FileComparator() {
    }

    public FileComparator(String sortDir) {
	this.sortDir = sortDir;
    }

    public FileComparator(String sortField, String sortDir) {
	this.sortField = sortField;
	this.sortDir = sortDir;
    }

    public int compare(final Object o1, final Object o2) {
	// First directories
	int i = 0;
	if (((NASFile) o1).getFile().isDirectory() && !((NASFile) o2).getFile().isDirectory())
	    i = -1;
	else if (((NASFile) o2).getFile().isDirectory() && !((NASFile) o1).getFile().isDirectory())
	    i = 1;
	if (i != 0) return i;
	
	if (this.sortField.equals("lastmodified")) {
	    if (this.sortDir.equals("ASC"))
		return new Long(((NASFile) o1).getFile().lastModified()).compareTo(
			new Long(((NASFile) o2).getFile().lastModified()));
	    else
		return new Long(((NASFile) o2).getFile().lastModified()).compareTo(
			new Long(((NASFile) o1).getFile().lastModified()));
	}
	else if (this.sortField.equals("bytes")) {
	    if (this.sortDir.equals("ASC"))
		return new Long(((NASFile) o1).getFile().length()).compareTo(
			new Long(((NASFile) o2).getFile().length()));
	    else
		return new Long(((NASFile) o2).getFile().length()).compareTo(
			new Long(((NASFile) o1).getFile().length()));
	}
	else if (this.sortField.equals("owner")){
	    if (this.sortDir.equals("ASC"))
		return ((NASFile)o1).getOwner().toLowerCase().compareTo(
			((NASFile)o2).getOwner().toLowerCase());
	    else
		return ((NASFile)o2).getOwner().toLowerCase().compareTo(
			((NASFile)o1).getOwner().toLowerCase());
	}
	else {
	    if (this.sortDir.equals("ASC"))
		return ((NASFile)o1).getFile().getName().toLowerCase().compareTo(
			((NASFile)o2).getFile().getName().toLowerCase());
	    else
		return ((NASFile)o2).getFile().getName().toLowerCase().compareTo(
			((NASFile)o1).getFile().getName().toLowerCase());
	}
    }

}
