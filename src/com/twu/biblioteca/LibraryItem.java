package com.twu.biblioteca;

import java.util.ArrayList;

abstract class LibraryItem {

    // Assumes that each item has an unique ID
    // IRL ID could be something like a scan-able barcode, since it's very much possible for two different
    // books to have the same title - this means user needs to use a different unique key to make selections
    String id;
    private boolean isAvailable = true;
    static final String ID_COL_NAME = "ID";

    String getId() {
        return this.id;
    }

    boolean isAvailable() {
        return this.isAvailable;
    }

    void checkOut() {
        this.isAvailable = false;
    }

    void returning() {
        this.isAvailable = true;
    }

    abstract ArrayList<String> getPrintableHeaders();

    abstract ArrayList<String> getPrintableFieldStrings();

    abstract String getDescription();
}
