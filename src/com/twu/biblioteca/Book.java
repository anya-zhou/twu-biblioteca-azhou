package com.twu.biblioteca;

public class Book {
    private String title;
    private String authorName;
    private String yearPublished;
    private String id;
    static protected int nextId = 1;

    public Book(String title, String authorName, String yearPublished) {
        this.title = title;
        this.authorName = authorName;
        this.yearPublished = yearPublished;
        this.id = Integer.toString(nextId);
        nextId += 1;
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

    @Override
    public String toString() {
        return this.title;
    }
}
