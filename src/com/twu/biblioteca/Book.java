package com.twu.biblioteca;

import java.util.ArrayList;

public class Book extends LibraryItem{
    private String title;
    private String authorName;
    private String yearPublished;
    private final static ArrayList<String> headerFields = new ArrayList<String>(){
        {
            add(ID_COL_NAME);
            add("Title");
            add("Author");
            add("Year Published");
        }
    };

    public Book(String id, String title, String authorName, String yearPublished) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.yearPublished = yearPublished;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return this.authorName;
    }

    public String getYearPublished() {
        return this.yearPublished;
    }

    @Override
    public ArrayList<String> getPrintableHeaders() {
        return headerFields;
    }

    @Override
    public ArrayList<String> getPrintableFieldStrings() {
        ArrayList<String> fieldStrings = new ArrayList<>();
        fieldStrings.add(this.getId());
        fieldStrings.add(this.getTitle());
        fieldStrings.add(this.getAuthorName());
        fieldStrings.add(this.getYearPublished());
        return fieldStrings;
    }
}
