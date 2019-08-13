package com.twu.biblioteca;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

public class BookTest {
    @Test
    public void testToStringIsTitle() {
        String testTitle = "Test Title";
        Book testBook = new Book("1", testTitle, "Test Author", "1923");
        assertThat(testBook.toString(), is(testTitle));
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

}
