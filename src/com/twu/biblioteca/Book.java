package com.twu.biblioteca;

import java.util.ArrayList;

public class Book extends LibraryItem{
    private String title;
    private String authorName;
    private String yearPublished;
    private static ArrayList<String> headerFields = new ArrayList<String>(){
        {
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

    public static String getHeadingString(int idColWidth, int colWidth, String colDivider) {
     return getHeadingString(headerFields, idColWidth, colWidth, colDivider);
    }

    @Override
    public String getFormattedString(int idColWidth, int colWidth, String colDivider) {
        String bookString = formatToCol(this.getId(), idColWidth, colDivider);
        bookString += formatToCol(this.getTitle(), colWidth, colDivider);
        bookString += formatToCol(this.getAuthorName(), colWidth, colDivider);
        bookString += formatToCol(this.getYearPublished(), colWidth, colDivider);
        return bookString;
    }
}
