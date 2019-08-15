package com.twu.biblioteca;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class BookTest {
    @Test
    public void testGetPrintableFieldStrings() {
        // Given
        String testTitle = "Test Title";
        Book testBook = new Book("1", testTitle, "Test Author", "1923");
        // When
        ArrayList<String> bookDetails = testBook.getPrintableFieldStrings();
        // Then
        assertThat(bookDetails.get(0), is(testBook.getId()));
        assertThat(bookDetails.get(1), is(testBook.getTitle()));
        assertThat(bookDetails.get(2), is(testBook.getAuthorName()));
        assertThat(bookDetails.get(3), is(testBook.getYearPublished()));
    }

    @Test
    public void testCheckOut() {
        //Given - initially available book
        Book testBook = new Book("1", "Test Title", "Test Author", "1923");
        assertThat(testBook.isAvailable(), is(true));
        //When
        testBook.checkOut();
        //Then
        assertThat(testBook.isAvailable(), is(false));
    }

    @Test
    public void testReturn() {
        //Given - initially checked out book
        Book testBook = new Book("1", "Test Title", "Test Author", "1923");
        testBook.checkOut();
        //When
        testBook.returning();
        //Then
        assertThat(testBook.isAvailable(), is(true));
    }
}
