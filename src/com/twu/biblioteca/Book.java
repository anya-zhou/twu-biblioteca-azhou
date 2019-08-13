package com.twu.biblioteca;

public class Book {
    private String title;
    private String authorName;
    private String yearPublished;
    private String id;
    private boolean isAvailable;

    // Assumes that each book has an unique ID
    // IRL ID could be something like a scan-able barcode, since it's very much possible for two different
    // books to have the same title - this means user needs to use a different unique key to make selections
    public Book(String id, String title, String authorName, String yearPublished) {
        this.title = title;
        this.authorName = authorName;
        this.yearPublished = yearPublished;
        this.id = id;
        this.isAvailable = true;
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

    @Override
    public String toString() {
        return this.title;
    }
}
