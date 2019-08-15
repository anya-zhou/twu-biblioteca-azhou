package com.twu.biblioteca;

import java.util.ArrayList;

public class SampleAppData {
    private static Library<Book> bookLibrary;

    public SampleAppData() {
        // Initialize some existing books in a library for test
        ArrayList<Book> testBooks = new ArrayList<Book>();
        Book testBook1 = new Book(
                "1", "The Giver", "Lois Lowry", "1993"
        );
        Book testBook2 = new Book(
                "2", "The Secret Garden", "Frances Hodgson Burnett", "1911"
        );
        testBooks.add(testBook1);
        testBooks.add(testBook2);
        bookLibrary = new Library<Book>(testBooks);
    }

    public Library<Book> getBookLibrary() {
        return bookLibrary;
    }
}
