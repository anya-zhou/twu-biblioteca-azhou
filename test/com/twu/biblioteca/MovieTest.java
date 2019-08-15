package com.twu.biblioteca;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MovieTest {
    @Test
    public void testGetFormattedStringNoRating() {
        // Given
        String testTitle = "Test Movie";
        Movie testMovie = new Movie("2", testTitle, "1892", "Test Director");
        int testIdWidth = 3;
        int testColWidth = 20;
        String testDivider = "  ";
        // When
        String testString =  testMovie.getFormattedString(testIdWidth, testColWidth, testDivider);
        String[] movieDetails = testString.split(testDivider+"\\s+");
        // Then
        assertThat(movieDetails[0].trim(), is(testMovie.getId()));
        assertThat(movieDetails[1].trim(), is(testMovie.getName()));
        assertThat(movieDetails[2].trim(), is(testMovie.getYearReleased()));
        assertThat(movieDetails[3].trim(), is(testMovie.getDirector()));
        assertThat(movieDetails[4].trim(), is("N/A"));
    }

    @Test
    public void testGetFormattedStringWithRating() {
        // Given
        String testTitle = "Test Movie";
        Movie testMovie = new Movie("2", testTitle, "1892", "Test Director", 10);
        int testIdWidth = 3;
        int testColWidth = 20;
        String testDivider = "  ";
        // When
        String testString =  testMovie.getFormattedString(testIdWidth, testColWidth, testDivider);
        String[] movieDetails = testString.split(testDivider+"\\s+");
        // Then
        assertThat(movieDetails[4].trim(), is("10"));
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
