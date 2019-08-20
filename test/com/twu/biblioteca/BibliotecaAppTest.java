package com.twu.biblioteca;

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
    private User testUser1;
    private User testUser2;

    @Before
    public void setUp() throws IOException {
        // Given
        sampleData = new SampleAppData();
        loginService = new LoginService(sampleData.getUsers());
        testUser1 = sampleData.getUsers().get(0);
        testUser2 = sampleData.getUsers().get(1);
        userId = testUser1.getUsername();
        password = "password";
        bibliotecaApp = new BibliotecaApp(
            sampleData.getBookLibrary(), sampleData.getMovieLibrary(),
            mockOut, mockReader, loginService
        );
        spyApp = spy(bibliotecaApp);
    }

    private void testUser1LoggedIn(BibliotecaApp app) throws IOException {
        when(mockReader.readLine()).thenReturn(userId).thenReturn(password);
        app.loginUser();

        verify(mockOut).println("Please enter your library number in the following format 'xxx-xxxx': ");
        verify(mockOut).println("Please enter your password: ");
    }

    @Test
    public void testLogin() {
        try {
            // Given
            assertThat(spyApp.isLoggedIn(), is(false));
            exit.expectSystemExit();
            // When
            when(mockReader.readLine()).thenReturn(BibliotecaApp.LOGIN_KEY)
                    .thenReturn(userId).thenReturn(password).thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
            spyApp.start();
            // Then
            verify(spyApp).loginUser();
            assertThat(spyApp.isLoggedIn(), is(true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBadLogin() {
        try {
            // Given
            assertThat(spyApp.isLoggedIn(), is(false));
            exit.expectSystemExit();
            // When
            when(mockReader.readLine()).thenReturn(BibliotecaApp.LOGIN_KEY)
                    .thenReturn(userId).thenReturn("badpassword").thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
            spyApp.start();
            // Then
            verify(spyApp).loginUser();
            assertThat(spyApp.isLoggedIn(), is(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShowWelcomeAndMenuOnStart() throws IOException {
        // Given
        final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
        exit.expectSystemExit();
        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        spyApp.start();
        // Then
        verify(mockOut).println(WELCOME_MSG);

        verify(mockOut).println("Please enter the number of the option that you would like to select: ");
        verify(mockOut).println(BibliotecaApp.PRE_LOGIN_LIST_BOOKS_KEY + ". List of books");
        verify(mockOut).println(BibliotecaApp.PRE_LOGIN_LIST_MOVIES_KEY + ". List of movies");
        verify(mockOut).println(BibliotecaApp.LOGIN_KEY + ". Login for more options");
        verify(mockOut).println(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY + ". Exit the application");
        verifyNoMoreInteractions(mockOut);
    }

    @Test
    public void testPostLoginShowMenu() throws IOException {
        // Given
        testUser1LoggedIn(bibliotecaApp);
        exit.expectSystemExit();
        //When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        bibliotecaApp.start();
        //Then
        verify(mockOut).println("Please enter the number of the option that you would like to select: ");
        verify(mockOut).println(BibliotecaApp.POST_LOGIN_LIST_BOOKS_KEY + ". List of books");
        verify(mockOut).println(BibliotecaApp.POST_LOGIN_CHECK_OUT_BOOK_KEY + ". Check-out a book");
        verify(mockOut).println(BibliotecaApp.POST_LOGIN_RETURN_BOOK_KEY + ". Return a book");
        verify(mockOut).println(BibliotecaApp.POST_LOGIN_LIST_MOVIES_KEY + ". List of movies");
        verify(mockOut).println(BibliotecaApp.POST_LOGIN_CHECK_OUT_MOVIE_KEY + ". Check-out a movie");
        verify(mockOut).println(BibliotecaApp.POST_LOGIN_VIEW_CHECKED_OUT_BOOKS_KEY + ". View books checked out");
        verify(mockOut).println(BibliotecaApp.POST_LOGIN_VIEW_USER_INFO_KEY + ". View my information");
        verify(mockOut).println(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY + ". Exit the application");
    }


    @Test
    public void testPreLoginSelectListBookMenuOption() throws IOException {
        // Given
        exit.expectSystemExit();
        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_LIST_BOOKS_KEY)
                .thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        spyApp.start();
        // Then
        verify(spyApp).listAvailableBooks();
    }

    @Test
    public void testPostLoginSelectListBookMenuOption() throws IOException {
        // Given
        testUser1LoggedIn(spyApp);
        exit.expectSystemExit();
        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
        // Then
        verify(spyApp).listAvailableBooks();
    }

    @Test
    public void testListAllBooks() {
        // Given
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
    public void testPostLoginBookCheckoutOption() throws IOException {
        // Given
        testUser1LoggedIn(spyApp);
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();
        Book checkoutBook = testBooks.get(0);
        exit.expectSystemExit();
        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_CHECK_OUT_BOOK_KEY)
            .thenReturn(checkoutBook.getId())   // Attempt to checkout the first book on the list
                .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
        // Then
        verify(mockOut).println("Please enter the ID of the book that you would like to check-out: ");
        verify(spyApp).checkoutBook(checkoutBook.getId());
    }

    @Test
    public void testCheckoutBookSuccessful() throws IOException {
        // Given
        Book testCheckoutBook = sampleData.getBookLibrary().getItems().get(0);
        // When
        bibliotecaApp.checkoutBook(testCheckoutBook.getId());
        // Then
        verify(mockOut).println("Thank you! Enjoy the book");

        // When
        bibliotecaApp.listAvailableBooks();
        //Then
        verify(mockOut, times(0)).print(containsString(testCheckoutBook.getTitle()));
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
    public void testCheckoutBookMenuOptionInvalidBookId() throws IOException {
        // When
        bibliotecaApp.checkoutBook("Bad ID");
        // Then
        verify(mockOut).println("Sorry, that book is not available");
    }


    @Test
    public void testReturnBooksOption() throws IOException {
        // Given
        testUser1LoggedIn(spyApp);
        exit.expectSystemExit();
        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_RETURN_BOOK_KEY)
                .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
        // Then
        verify(spyApp).initiateBookReturn();
    }

    @Test
    public void testReturnBookMenuOptionValidUserInputs() throws IOException {
        testUser1LoggedIn(spyApp);
        Book checkedOutBook = sampleData.getBookLibrary().getItems().get(0);
        spyApp.recordCheckout(testUser1, checkedOutBook);  // ensure it's checked out
        when(mockReader.readLine()).thenReturn(checkedOutBook.getId()); // Attempt to return that book when prompted
        // When
        spyApp.initiateBookReturn();
        // Then - prompt user to select which one to return, invoke return
        // prompt user to select which one to return, remove it from user's checkout record
        verify(mockOut).println("Please enter the ID of the book that you would like to return: ");
        verify(spyApp).returnBook(testUser1, checkedOutBook.getId());
        assertThat(spyApp.getCheckedOutBooks(testUser1), not(hasItem(checkedOutBook)));
    }

    @Test
    public void testSuccessfulReturn() throws IOException {
        // Given
        testUser1LoggedIn(bibliotecaApp);
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();
        Book checkoutBook = testBooks.get(0);
        bibliotecaApp.recordCheckout(testUser1, checkoutBook);    // ensure it's checked out

        // When
        bibliotecaApp.returnBook(testUser1, checkoutBook.getId());
        bibliotecaApp.listAvailableBooks();

        // Then
        verify(mockOut, atLeastOnce()).print(captor.capture());
        assertThat(captor.getAllValues(), hasItem(containsString(checkoutBook.getTitle())));
        verify(mockOut).println("Thank you for returning the book");
    }

    @Test
    public void testUnsuccessfulReturnNoSuchBook() {
        // When
        bibliotecaApp.returnBook(testUser1,"Bad ID");
        // Then
        verify(mockOut).println("That is not a valid book to return.");
    }

    @Test
    public void testUnsuccessfulReturnCheckedOutByDifferentUser() throws IOException {
        // Given
        testUser1LoggedIn(bibliotecaApp);
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();
        Book checkoutBook = testBooks.get(0);
        bibliotecaApp.recordCheckout(testUser2, checkoutBook);    // real book but checked out by a DIFFERENT USER

        // When - original user tries to return the same book
        bibliotecaApp.returnBook(testUser1, checkoutBook.getId());
        bibliotecaApp.listAvailableBooks();

        // Then
        verify(mockOut).println("That is not a valid book to return.");
        assertThat(bibliotecaApp.getCheckedOutBooks(testUser2), hasItem(checkoutBook));
    }

    @Test
    public void testPostLoginSelectCheckoutMovieMenuOption() throws IOException {
        // Given
        testUser1LoggedIn(spyApp);
        String checkoutMovieId = sampleData.getMovieLibrary().getItems().get(0).getId();
        exit.expectSystemExit();
        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_CHECK_OUT_MOVIE_KEY)
                .thenReturn(checkoutMovieId)   // Attempt to checkout the first movie on the list
                .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
        // Then
        verify(mockOut).println("Please enter the ID of the movie that you would like to check-out: ");
        verify(spyApp).checkoutMovie(checkoutMovieId);
    }

    @Test
    public void testSuccessfulCheckoutMovie() throws IOException {
        // Given - attempt to checkout an existing book at index 0
        ArrayList<Movie> testMovies = sampleData.getMovieLibrary().getItems();
        Movie checkedOutMovie = testMovies.get(0);
        // When
        bibliotecaApp.checkoutMovie(checkedOutMovie.getId());
        bibliotecaApp.listAvailableMovies();
        // Then
        verify(mockOut).println("Thank you! Enjoy the movie");

        // When
        bibliotecaApp.listAvailableMovies();
        // Then
        verify(mockOut, times(0)).println(containsString(checkedOutMovie.getName()));
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
    public void testGetNotifiedOnInvalidOption() throws IOException {
        // Given - one invalid selection before a valid one
        when(mockReader.readLine()).thenReturn("100").thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        // When
        exit.expectSystemExit();
        bibliotecaApp.start(); // valid option does not include 100
        // Then - should notify user and prompt user for input one more time
        verify(mockOut).println("Please select a valid option!");
        verify(mockReader, times(2)).readLine();
    }

    @Test
    public void testSelectListMovieMenuOption() throws IOException {
        ArrayList<Movie> testMovies = sampleData.getMovieLibrary().getItems();
        exit.expectSystemExit();

        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_LIST_MOVIES_KEY)
            .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();

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
    public void testViewUserInfoValidLogin() throws IOException {
        //Given
        testUser1LoggedIn(spyApp);
        exit.expectSystemExit();

        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_VIEW_USER_INFO_KEY)
            .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();

        // Then
        verify(mockOut, atLeastOnce()).println(captor.capture());

        List<String> printedFields = captor.getAllValues();

        assertThat(captor.getAllValues(), hasItem(containsString(testUser1.getName())));
        assertThat(captor.getAllValues(), hasItem(containsString(testUser1.getEmail())));
        assertThat(captor.getAllValues(), hasItem(containsString(testUser1.getPhone())));
    }

    @Test
    public void testViewCheckedOutBooksValidLogin() throws IOException {
        //Given
        testUser1LoggedIn(spyApp);
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();
        Book checkoutBook = testBooks.get(0);
        spyApp.recordCheckout(testUser1, checkoutBook);
        exit.expectSystemExit();

        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_VIEW_CHECKED_OUT_BOOKS_KEY)
                .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();

        // Then
        verify(mockOut, atLeastOnce()).print(captor.capture());
        assertThat(captor.getAllValues(), hasItem(containsString(checkoutBook.getTitle())));
    }

    @Test
    public void testPreLoginExitOption() throws IOException {
        // Given
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        exit.expectSystemExit(); // Prevents System.exit from existing the JVM
        // When
        bibliotecaApp.start();
        // Then - should not prompt for user input again
        verify(mockReader, times(1)).readLine();
        verifyNoMoreInteractions(mockReader);
    }

    @Test
    public void testPostLoginExitOption() throws IOException {
        // Given
        testUser1LoggedIn(bibliotecaApp);
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
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
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_LIST_BOOKS_KEY)
                .thenReturn(BibliotecaApp.PRE_LOGIN_LIST_BOOKS_KEY).thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        exit.expectSystemExit(); // Prevents System.exit from existing the JVM
        // When
        spyApp.start();
        // Then - should prompt for menu option twice and show options twice before exiting
        verify(spyApp, times(2)).listAvailableBooks();
    }
}