package com.twu.biblioteca;

public class Book {
    private String title;
    private String authorName;
    private String yearPublished;

    public Book(String title, String authorName, String yearPublished) {
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
    public String toString() {
        return this.title;
    }

}
