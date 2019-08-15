package com.twu.biblioteca;

import java.util.ArrayList;

public abstract class LibraryItem {

    // Assumes that each item has an unique ID
    // IRL ID could be something like a scan-able barcode, since it's very much possible for two different
    // books to have the same title - this means user needs to use a different unique key to make selections
    protected String id;
    private boolean isAvailable = true;
    private static final String ID_COL_NAME = "ID";

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

    public abstract String getFormattedString(int idColWidth, int colWidth, String colDivider);

    public static String getHeadingString(ArrayList<String> headerFields, int idColWidth, int colWidth, String colDivider) {
        String header = formatToCol(ID_COL_NAME, idColWidth, colDivider);
        for (String field : headerFields) {
            header += formatToCol(field, colWidth, colDivider);
        }
        return header;
    }

    public static String formatToCol(String s, int colWidth, String colDivider) {
        return String.format("%-" + colWidth + "s" + colDivider, s);
    }
}
