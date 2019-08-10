package com.twu.biblioteca;

import java.util.ArrayList;

public class SampleAppData {
    private static Library library;

    public SampleAppData() {
        // Initialize some existing books in a library for test
        ArrayList<Book> testBooks = new ArrayList<Book>();
        Book testBook1 = new Book("The Giver");
        Book testBook2 = new Book("The Secret Garden");
        testBooks.add(testBook1);
        testBooks.add(testBook2);
        library = new Library(testBooks);
    }

    public Library getLibrary() {
        return library;
    }
}
