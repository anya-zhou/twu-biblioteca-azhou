package com.twu.biblioteca;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;

@RunWith(MockitoJUnitRunner.class)
public class BibliotecaAppTest {
    @Mock
    PrintStream mockOut;

    @Mock
    BufferedReader mockReader;

    @Captor
    ArgumentCaptor<String> captor;

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    private BibliotecaApp bibliotecaApp;
    private BibliotecaApp spyApp;
    private SampleAppData sampleData;
    private LoginService loginService;
    private String userId;
    private String password;

    @Before
    public void setUp() throws IOException {
        // Given
        sampleData = new SampleAppData();
        loginService = new LoginService(sampleData.getUsers());
        userId = sampleData.getUsers().get(0).getUsername();
        password = sampleData.getUsers().get(0).getPassword();
        bibliotecaApp = new BibliotecaApp(
            sampleData.getBookLibrary(), sampleData.getMovieLibrary(),
            mockOut, mockReader, loginService
        );
        spyApp = spy(bibliotecaApp);
        when(mockReader.readLine()).thenReturn(BibliotecaApp.EXIT_APP_KEY); // Default input when prompted is exit
    }

    @Test
    public void testShowWelcomeOnStarting() {
        // Given
        final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
        // When
        exit.expectSystemExit();
        bibliotecaApp.start();
        // Then
        verify(mockOut).println(WELCOME_MSG);
    }

    @Test
    public void testListAllBooks() {
        // Given - populate library with some dummy books from SampleAppData
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();

        // When
        bibliotecaApp.listAvailableBooks();

        // Then
        int numColumns = testBooks.get(0).getPrintableHeaders().size();
        int totalExpectedFields = numColumns * (testBooks.size() + 1);
        verify(mockOut, times(totalExpectedFields)).print(captor.capture());

        List<String> printedFields = captor.getAllValues();

        assertThat(printedFields.get(0), containsString("ID"));
        assertThat(printedFields.get(1), containsString("Title"));
        assertThat(printedFields.get(2), containsString("Author"));
        assertThat(printedFields.get(3), containsString("Year Published"));

        int fieldIndex = 4;
        for (Book bookToPrint : testBooks) {
            // Each subsequent printed line should contain title, author and year
            assertThat(printedFields.get(fieldIndex), containsString(bookToPrint.getId()));
            assertThat(printedFields.get(fieldIndex+1), containsString(bookToPrint.getTitle()));
            assertThat(printedFields.get(fieldIndex+2), containsString(bookToPrint.getAuthorName()));
            assertThat(printedFields.get(fieldIndex+3), containsString(bookToPrint.getYearPublished()));
            fieldIndex += 4;
        }
    }

    private void verifyPrintIgnoreTrailingBlanks(String s) {
        verify(mockOut, atLeastOnce()).print(captor.capture());
        assertThat(captor.getAllValues(), hasItem(containsString(s)));
    }

    @Test
    public void testPrintNothingForEmptyLibrary() {
        // Given - empty library as no library passed in through the constructor
        BibliotecaApp emptyBibliotecaApp = new BibliotecaApp(mockOut, mockReader, loginService);
        // When
        emptyBibliotecaApp.listAvailableBooks();
        // Then - print header only
        verify(mockOut, times(1)).println();
    }

    @Test
    public void testShowMenu() {
        // When
        bibliotecaApp.showMenu();
        //Then
        verify(mockOut).println("Please enter the number of the option that you would like to select: ");
        verify(mockOut).println(BibliotecaApp.LIST_BOOKS_KEY + ". List of books");
        verify(mockOut).println(BibliotecaApp.CHECK_OUT_BOOK_KEY + ". Check-out a book");
        verify(mockOut).println(BibliotecaApp.RETURN_BOOK_KEY + ". Return a book");
        verify(mockOut).println(BibliotecaApp.LIST_MOVIES_KEY + ". List of movies");
        verify(mockOut).println(BibliotecaApp.EXIT_APP_KEY + ". Exit the application");
    }

    @Test
    public void testSelectListBookMenuOption() {
        // When
        spyApp.executeUserSelectedOption(BibliotecaApp.LIST_BOOKS_KEY);
        // Then
        verify(spyApp).listAvailableBooks();
    }

    @Test
    public void testCheckoutBookMenuOptionValidUserInputs() throws IOException {
        // Given
        Book checkoutBook = sampleData.getBookLibrary().getItems().get(0);
        when(mockReader.readLine()).thenReturn(userId)
                .thenReturn(password).thenReturn(checkoutBook.getId()); // Attempt to checkout the first book on the list
        // When
        spyApp.executeUserSelectedOption(BibliotecaApp.CHECK_OUT_BOOK_KEY);
        // Then
        // prompts for user log in
        // prompt user to select which one to checkout, invoke checkout on selected book
        verify(mockOut).println("Please enter your library number in the following format 'xxx-xxxx': ");
        verify(mockOut).println("Please enter your password: ");
        verify(mockOut).println("Please enter the ID of the book that you would like to check-out: ");
        verify(spyApp).checkoutBook(checkoutBook.getId());
        assertThat(spyApp.getCheckedOutBooks(spyApp.getUser(userId)), hasItem(is(checkoutBook)));
    }

    @Test
    public void testCheckoutBookMenuOptionInvalidUserInputs() throws IOException {
        // Given
        Book checkoutBook = sampleData.getBookLibrary().getItems().get(0);
        when(mockReader.readLine()).thenReturn(userId)
                .thenReturn("badpassword").thenReturn(checkoutBook.getId()); // Attempt to checkout with bad login details
        // When
        spyApp.executeUserSelectedOption(BibliotecaApp.CHECK_OUT_BOOK_KEY);
        // Then
        // prompts for user log in, when log in is bad should NOT proceed to allowing user to checkout a book
        verify(mockOut).println("Please enter your library number in the following format 'xxx-xxxx': ");
        verify(mockOut).println("Please enter your password: ");
        verify(mockOut, times(0)).println("Please enter the ID of the book that you would like to check-out: ");
        assertThat(spyApp.getCheckedOutBooks(spyApp.getUser(userId)), not(hasItem(is(checkoutBook))));
    }


    @Test
    public void testSelectCheckoutMovieMenuOption() throws IOException {
        // Given

        String checkoutMovieId = sampleData.getMovieLibrary().getItems().get(0).getId();
        when(mockReader.readLine()).thenReturn(checkoutMovieId); // Attempt to checkout the first book on the list
        // When
        spyApp.executeUserSelectedOption(BibliotecaApp.CHECK_OUT_MOVIE_KEY);
        // Then - list all books, prompt user to select which one to checkout, invoke checkout on selected book
        verify(mockOut).println("Please enter the ID of the movie that you would like to check-out: ");
        verify(spyApp).checkoutMovie(checkoutMovieId);
    }

    @Test
    public void testSuccessfulCheckoutBook() throws IOException {
        // Given - attempt to checkout an existing book at index 0
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();
        String checkoutBookId = testBooks.get(0).getId();
        // When
        bibliotecaApp.checkoutBook(checkoutBookId);
        bibliotecaApp.listAvailableBooks();
        // Then
        // 1. Should show success message
        // 2. Checked out book should NOT be listed after
        verify(mockOut).println("Thank you! Enjoy the book");

        for (int i = 1; i < testBooks.size(); i++) {    // Should skip first book at 0 since it's checked out
            verifyPrintIgnoreTrailingBlanks(testBooks.get(i).getTitle());
        }
    }

    @Test
    public void testUnsuccessfulCheckoutNoSuchBook() throws IOException {
        // When
        bibliotecaApp.checkoutBook("Bad ID");
        // Then
        verify(mockOut).println("Sorry, that book is not available");
    }

    @Test
    public void testUnsuccessfulCheckoutBookAlreadyCheckedOut() throws IOException {
        // Given - attempt to checkout an book that was already checked out
        Book testCheckoutBook = sampleData.getBookLibrary().getItems().get(0);
        testCheckoutBook.checkOut();  // ensure it's already checked out

        // When
        bibliotecaApp.checkoutBook(testCheckoutBook.getId());
        // Then
        verify(mockOut).println("Sorry, that book is not available");
    }


    @Test
    public void testSuccessfulCheckoutMovie() throws IOException {
        // Given - attempt to checkout an existing book at index 0
        ArrayList<Movie> testMovies = sampleData.getMovieLibrary().getItems();
        String checkoutMovieId = testMovies.get(0).getId();
        // When
        bibliotecaApp.checkoutMovie(checkoutMovieId);
        bibliotecaApp.listAvailableMovies();
        // Then
        // 1. Should show success message
        // 2. Checked out book should NOT be listed after
        verify(mockOut).println("Thank you! Enjoy the movie");

        for (int i = 1; i < testMovies.size(); i++) {    // Should skip first book at 0 since it's checked out
            verifyPrintIgnoreTrailingBlanks(testMovies.get(i).getName());
        }
    }

    @Test
    public void testUnsuccessfulCheckoutNoSuchMovie() throws IOException {
        // When
        bibliotecaApp.checkoutMovie("Bad ID");
        // Then
        verify(mockOut).println("Sorry, that movie is not available");
    }

    @Test
    public void testUnsuccessfulCheckoutMovieAlreadyCheckedOut() throws IOException {
        // Given - attempt to checkout an book that was already checked out
        Movie testCheckoutMovie = sampleData.getMovieLibrary().getItems().get(0);
        testCheckoutMovie.checkOut();  // ensure it's already checked out

        // When
        bibliotecaApp.checkoutMovie(testCheckoutMovie.getId());
        // Then
        verify(mockOut).println("Sorry, that movie is not available");
    }

    @Test
    public void testShowMenuAndGetSelectionOnStarting() {
        // Given

        // When
        exit.expectSystemExit();
        spyApp.start();
        // Then
        verify(spyApp).showMenu();
        verify(spyApp).executeUserSelectedOption((String) any());
    }

    @Test
    public void testReturnBooksOption() {
        // When
        spyApp.executeUserSelectedOption(BibliotecaApp.RETURN_BOOK_KEY);
        // Then
        verify(spyApp).initiateBookReturn();
    }

    @Test
    public void testSelectReturnBookMenuOption() throws IOException {
        Book checkedOutBook = sampleData.getBookLibrary().getItems().get(0);
        checkedOutBook.checkOut();  // ensure it's checked out
        when(mockReader.readLine()).thenReturn(checkedOutBook.getId()); // Attempt to return that book when prompted
        // When
        spyApp.initiateBookReturn();
        // Then - prompt user to select which one to return, invoke return
        verify(mockOut).println("Please enter the ID of the book that you would like to return: ");
        verify(spyApp).returnBook(checkedOutBook.getId());
    }

    @Test
    public void testSuccessfulReturn() throws IOException {
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();
        Book checkoutBook = testBooks.get(0);
        checkoutBook.checkOut();    // ensure it's checked out

        // When
        bibliotecaApp.returnBook(checkoutBook.getId());
        bibliotecaApp.listAvailableBooks();
        // Then
        // 1. Returned book should show in the list
        // 2. Should show success message
        int numColumns = testBooks.get(0).getPrintableHeaders().size();
        int totalExpectedFields = numColumns * (testBooks.size() + 1);
        verify(mockOut, times(totalExpectedFields)).print(captor.capture());
        assertThat(captor.getAllValues(), hasItem(containsString(checkoutBook.getTitle())));
        verify(mockOut).println("Thank you for returning the book");
    }

    @Test
    public void testUnsuccessfulReturnNoSuchBook() throws IOException {
        // When
        bibliotecaApp.returnBook("Bad ID");
        // Then
        verify(mockOut).println("That is not a valid book to return.");
    }

    @Test
    public void testGetNotifiedOnInvalidOption() throws IOException {
        // Given - one invalid selection before a valid one
        when(mockReader.readLine()).thenReturn("100").thenReturn(BibliotecaApp.EXIT_APP_KEY);
        // When
        exit.expectSystemExit();
        bibliotecaApp.start(); // valid option does not include 100
        // Then - should notify user and prompt user for input one more time
        verify(mockOut).println("Please select a valid option!");
        verify(mockReader, times(2)).readLine();
    }

    @Test
    public void testSelectListMovieMenuOption() {
        ArrayList<Movie> testMovies = sampleData.getMovieLibrary().getItems();

        // When
        spyApp.executeUserSelectedOption(BibliotecaApp.LIST_MOVIES_KEY);

        // Then
        int numColumns = sampleData.getMovieLibrary().getHeaderStrings().size();
        int totalExpectedFields = numColumns * (testMovies.size() + 1);
        verify(mockOut, times(totalExpectedFields)).print(captor.capture());

        List<String> printedFields = captor.getAllValues();

        assertThat(printedFields.get(0), containsString("ID"));
        assertThat(printedFields.get(1), containsString("Name"));
        assertThat(printedFields.get(2), containsString("Year Released"));
        assertThat(printedFields.get(3), containsString("Director"));
        assertThat(printedFields.get(4), containsString("Rating"));

        int fieldIndex = 5;
        for (Movie movieToPrint : testMovies) {
            // Each subsequent printed line should contain all movie fields
            assertThat(printedFields.get(fieldIndex), containsString(movieToPrint.getId()));
            assertThat(printedFields.get(fieldIndex+1), containsString(movieToPrint.getName()));
            assertThat(printedFields.get(fieldIndex+2), containsString(movieToPrint.getYearReleased()));
            assertThat(printedFields.get(fieldIndex+3), containsString((movieToPrint.getDirector())));
            if (movieToPrint.getRating() == null) {
                assertThat(printedFields.get(fieldIndex+4), containsString("unrated"));
            } else {
                assertThat(printedFields.get(fieldIndex+4), containsString(Integer.toString(movieToPrint.getRating())));
            }
            fieldIndex += 5;
        }
    }

    @Test
    public void testExitOption() throws IOException {
        // Given
        when(mockReader.readLine()).thenReturn(BibliotecaApp.EXIT_APP_KEY);
        exit.expectSystemExit(); // Prevents System.exit from existing the JVM
        // When
        bibliotecaApp.start();
        // Then - should not prompt for user input again
        verify(mockReader, times(1)).readLine();
        verifyNoMoreInteractions(mockReader);
    }

    @Test
    public void testLoopWhenNoExitSelected() throws IOException {
        // Given
        when(mockReader.readLine()).thenReturn(BibliotecaApp.LIST_BOOKS_KEY)
                .thenReturn(BibliotecaApp.LIST_BOOKS_KEY).thenReturn(BibliotecaApp.EXIT_APP_KEY);
        exit.expectSystemExit(); // Prevents System.exit from existing the JVM
        // When
        spyApp.start();
        // Then - should prompt for menu option twice and show options twice before exiting
        verify(spyApp, times(2)).listAvailableBooks();
    }
}