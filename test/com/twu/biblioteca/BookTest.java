package com.twu.biblioteca;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class BookTest {
    @Test
    public void testGetFormattedString() {
        // Given
        String testTitle = "Test Title";
        Book testBook = new Book("1", testTitle, "Test Author", "1923");
        int testIdWidth = 3;
        int testColWidth = 20;
        String testDivider = "  ";
        // When
        String testString =  testBook.getFormattedString(testIdWidth, testColWidth, testDivider);
        String[] bookDetails = testString.split(testDivider+"\\s+");
        // Then
        assertThat(bookDetails[0].trim(), is(testBook.getId()));
        assertThat(bookDetails[1].trim(), is(testBook.getTitle()));
        assertThat(bookDetails[2].trim(), is(testBook.getAuthorName()));
        assertThat(bookDetails[3].trim(), is(testBook.getYearPublished()));
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
