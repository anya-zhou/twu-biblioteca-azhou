package com.twu.biblioteca;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Library {
    private ArrayList<Book> books;

    public Library() {
        this.books = new ArrayList<Book>();
    }

    public Library(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public Book getBook(String bookId) {
        for (Book book : this.books) {
            if (book.getId().equals(bookId)) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<Book> getAvailableBooks() {
        return (ArrayList<Book>) this.books.stream().filter(book -> book.isAvailable()).collect(Collectors.toList());
    }
}
