package com.twu.biblioteca;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MovieTest {
    @Test
    public void testGetPrintableFieldStringsNoRating() {
        Movie testMovie = initializeMovieWithoutRating();

        ArrayList<String> bookDetails = testMovie.getPrintableFieldStrings();

        assertThat(bookDetails.get(0), is(testMovie.getId()));
        assertThat(bookDetails.get(1), is(testMovie.getName()));
        assertThat(bookDetails.get(2), is(testMovie.getYearReleased()));
        assertThat(bookDetails.get(3), is(testMovie.getDirector()));
        assertThat(bookDetails.get(4), is("unrated"));
    }

    private Movie initializeMovieWithoutRating() {
        String testTitle = "Test Title";
        return new Movie("1", testTitle, "Test Author", "1923");
    }

    @Test
    public void testGetPrintableFieldStringWithRating() {
        Movie testMovie = initializeMovieWithRating();

        ArrayList<String> bookDetails = testMovie.getPrintableFieldStrings();

        assertThat(bookDetails.get(0), is(testMovie.getId()));
        assertThat(bookDetails.get(1), is(testMovie.getName()));
        assertThat(bookDetails.get(2), is(testMovie.getYearReleased()));
        assertThat(bookDetails.get(3), is(testMovie.getDirector()));
        assertThat(bookDetails.get(4), is("10"));
    }

    private Movie initializeMovieWithRating() {
        String testTitle = "Test Title";
        return new Movie("2", testTitle, "1892", "Test Director", 10);
    }

    @Test
    public void testCheckOut() {
        Movie testMovie = initializeMovieWithoutRating();
        assertThat(testMovie.isAvailable(), is(true));
        //When
        testMovie.checkOut();
        //Then
        assertThat(testMovie.isAvailable(), is(false));
    }

    @Test
    public void testReturn() {
        //Given - initially checked out book
        Movie testMovie = initializeMovieWithRating();
        testMovie.checkOut();
        //When
        testMovie.returning();
        //Then
        assertThat(testMovie.isAvailable(), is(true));
    }
}
