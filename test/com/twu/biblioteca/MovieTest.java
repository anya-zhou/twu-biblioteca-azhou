package com.twu.biblioteca;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MovieTest {
    @Test
    public void testGetPrintableFieldStringsNoRating() {
        // Given
        String testTitle = "Test Title";
        Movie testMovie = new Movie("1", testTitle, "Test Author", "1923");
        // When
        ArrayList<String> bookDetails = testMovie.getPrintableFieldStrings();
        // Then
        assertThat(bookDetails.get(0), is(testMovie.getId()));
        assertThat(bookDetails.get(1), is(testMovie.getName()));
        assertThat(bookDetails.get(2), is(testMovie.getYearReleased()));
        assertThat(bookDetails.get(3), is(testMovie.getDirector()));
        assertThat(bookDetails.get(4), is("unrated"));
    }

    @Test
    public void testGetPrintableFieldStringWithRating() {
        // Given
        String testTitle = "Test Title";
        Movie testMovie = new Movie("2", testTitle, "1892", "Test Director", 10);
        // When
        ArrayList<String> bookDetails = testMovie.getPrintableFieldStrings();
        // Then
        assertThat(bookDetails.get(0), is(testMovie.getId()));
        assertThat(bookDetails.get(1), is(testMovie.getName()));
        assertThat(bookDetails.get(2), is(testMovie.getYearReleased()));
        assertThat(bookDetails.get(3), is(testMovie.getDirector()));
        assertThat(bookDetails.get(4), is("10"));
    }

    @Test
    public void testCheckOut() {
        //Given - initially available book
        Movie testMovie = new Movie("1", "Test Title", "Test Author", "1923");
        assertThat(testMovie.isAvailable(), is(true));
        //When
        testMovie.checkOut();
        //Then
        assertThat(testMovie.isAvailable(), is(false));
    }

    @Test
    public void testReturn() {
        //Given - initially checked out book
        Movie testMovie = new Movie("1", "Test Title", "Test Author", "1923");
        testMovie.checkOut();
        //When
        testMovie.returning();
        //Then
        assertThat(testMovie.isAvailable(), is(true));
    }
}
