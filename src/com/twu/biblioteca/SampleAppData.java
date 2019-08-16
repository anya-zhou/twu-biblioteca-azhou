package com.twu.biblioteca;

import java.util.ArrayList;

public class SampleAppData {
    private static Library<Book> bookLibrary;
    private static Library<Movie> movieLibrary;

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

        ArrayList<Movie> testMovies = new ArrayList<Movie>();
        Movie testMovie1 = new Movie(
                "1", "Gattaca", "1997", "Andrew Niccol"
        );
        Movie testMovie2 = new Movie(
                "2", "Moon", "2009", "Duncan Jones", 7
        );
        testMovies.add(testMovie1);
        testMovies.add(testMovie2);
        movieLibrary = new Library<Movie>(testMovies);
    }

    public Library<Book> getBookLibrary() {
        return bookLibrary;
    }
    public Library<Movie> getMovieLibrary() {
        return movieLibrary;
    }

    public ArrayList<User> getUsers() {
        User testUser1 = new User("123-4567", "password");
        User testUser2 = new User("000-0000", "password2");
        return new ArrayList<User>() { {
            add(testUser1);
            add(testUser2);
        }};
    }
}
