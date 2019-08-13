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

}
