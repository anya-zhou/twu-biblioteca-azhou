package com.twu.biblioteca;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

import java.util.ArrayList;

public abstract class LibraryItem {

    // Assumes that each item has an unique ID
    // IRL ID could be something like a scan-able barcode, since it's very much possible for two different
    // books to have the same title - this means user needs to use a different unique key to make selections
    protected String id;
    private boolean isAvailable = true;
    protected static final String ID_COL_NAME = "ID";

    public String getId() {
        return this.id;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void checkOut() {
        this.isAvailable = false;
    }

    public void returning() {
        this.isAvailable = true;
    }

    public abstract ArrayList<String> getPrintableHeaders();

    public abstract ArrayList<String> getPrintableFieldStrings();

    public abstract String getDescription();
}
