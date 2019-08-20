package com.twu.biblioteca;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
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
    private String wrongPassword;
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
        wrongPassword = "wrong" + password;
        
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

    private Movie getFirstSampleMovie() {
        ArrayList<Movie> testMovies = sampleData.getMovieLibrary().getItems();
        return testMovies.get(0);
    }

    private void checkAssertionWhenExiting(Runnable runnable) {
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(new Assertion() {
              @Override
              public void checkAssertion() throws Exception {
                  runnable.run();
              }
          }
        );
    }

    @Test
    public void testLogin() {
        try {
            assertThat(spyApp.isLoggedIn(), is(false));
            checkAssertionWhenExiting(() -> {
                verify(spyApp).loginUser();
                assertThat(spyApp.isLoggedIn(), is(true));
            });

            when(mockReader.readLine()).thenReturn(BibliotecaApp.LOGIN_KEY)
                    .thenReturn(userId).thenReturn(password).thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
            spyApp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBadLogin() {
        try {
            assertThat(spyApp.isLoggedIn(), is(false));
            checkAssertionWhenExiting(() -> {
                verify(spyApp).loginUser();
                assertThat(spyApp.isLoggedIn(), is(false));
            });

            when(mockReader.readLine()).thenReturn(BibliotecaApp.LOGIN_KEY)
                    .thenReturn(userId).thenReturn(wrongPassword).thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
            spyApp.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testShowWelcomeAndMenuOnStart() throws IOException {
        String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
        checkAssertionWhenExiting(() -> {
                verify(mockOut).println(WELCOME_MSG);

                verify(mockOut).println("Please enter the number of the option that you would like to select: ");
                verify(mockOut).println(BibliotecaApp.PRE_LOGIN_LIST_BOOKS_KEY + ". List of books");
                verify(mockOut).println(BibliotecaApp.PRE_LOGIN_LIST_MOVIES_KEY + ". List of movies");
                verify(mockOut).println(BibliotecaApp.LOGIN_KEY + ". Login for more options");
                verify(mockOut).println(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY + ". Exit the application");
        });
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testPostLoginShowMenu() throws IOException {
        // Given
        testUser1LoggedIn(bibliotecaApp);
        checkAssertionWhenExiting(() -> {
                verify(mockOut).println("Please enter the number of the option that you would like to select: ");
                verify(mockOut).println(BibliotecaApp.POST_LOGIN_LIST_BOOKS_KEY + ". List of books");
                verify(mockOut).println(BibliotecaApp.POST_LOGIN_CHECK_OUT_BOOK_KEY + ". Check-out a book");
                verify(mockOut).println(BibliotecaApp.POST_LOGIN_RETURN_BOOK_KEY + ". Return a book");
                verify(mockOut).println(BibliotecaApp.POST_LOGIN_LIST_MOVIES_KEY + ". List of movies");
                verify(mockOut).println(BibliotecaApp.POST_LOGIN_CHECK_OUT_MOVIE_KEY + ". Check-out a movie");
                verify(mockOut).println(BibliotecaApp.POST_LOGIN_VIEW_CHECKED_OUT_BOOKS_KEY + ". View books checked out");
                verify(mockOut).println(BibliotecaApp.POST_LOGIN_VIEW_USER_INFO_KEY + ". View my information");
                verify(mockOut).println(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY + ". Exit the application");
        });

        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        bibliotecaApp.start();
    }


    @Test
    public void testPreLoginSelectListBookMenuOption() throws IOException {
        checkAssertionWhenExiting(() -> {
                verify(spyApp).listAvailableBooks();
        });
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_LIST_BOOKS_KEY)
                .thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testPostLoginSelectListBookMenuOption() throws IOException {
        // Given
        testUser1LoggedIn(spyApp);
        checkAssertionWhenExiting(() -> {
                verify(spyApp).listAvailableBooks();
        });

        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_LIST_BOOKS_KEY)
            .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testListAllBooks() {
        // Given
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();

        // When
        bibliotecaApp.listAvailableBooks();

        // Then
        verify(mockOut, atLeastOnce()).print(captor.capture());
        List<String> printedFields = captor.getAllValues();

        assertBookHeaderWasPrinted(printedFields);
        assertBooksAllPrinted(testBooks, printedFields);
    }

    private void assertBooksAllPrinted(ArrayList<Book> testBooks, List<String> printedFields) {
        int i = 4;
        for (Book bookToPrint : testBooks) {
            String[] expectedCols = {
                    bookToPrint.getId(), bookToPrint.getTitle(), bookToPrint.getAuthorName(),
                    bookToPrint.getYearPublished()
            };

            for (int j = 0; j < expectedCols.length; j++) {
                assertThat(printedFields.get(i), containsString(expectedCols[j]));
                i++;
            }
        }
    }

    private void assertBookHeaderWasPrinted(List<String> printedFields) {
        assertThat(printedFields.get(0), containsString("ID"));
        assertThat(printedFields.get(1), containsString("Title"));
        assertThat(printedFields.get(2), containsString("Author"));
        assertThat(printedFields.get(3), containsString("Year Published"));
    }

    @Test
    public void testPostLoginBookCheckoutOption() throws IOException {
        testUser1LoggedIn(spyApp);
        Book bookToCheckout = getFirstSampleBook();

        checkAssertionWhenExiting(() -> {
                verify(mockOut).println("Please enter the ID of the book that you would like to check-out: ");
                verify(spyApp).checkoutBook(bookToCheckout.getId());
        });

        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_CHECK_OUT_BOOK_KEY)
            .thenReturn(bookToCheckout.getId())   // Attempt to checkout the first book on the list
                .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();

    }

    private Book getFirstSampleBook() {
        ArrayList<Book> testBooks = sampleData.getBookLibrary().getItems();
        return testBooks.get(0);
    }

    @Test
    public void bookToCheckoutSuccessful() throws IOException {
        Book bookToCheckout = getFirstSampleBook();

        bibliotecaApp.checkoutBook(bookToCheckout.getId());
        verify(mockOut).println("Thank you! Enjoy the book");

        bibliotecaApp.listAvailableBooks();
        verify(mockOut, times(0)).print(containsString(bookToCheckout.getTitle()));
    }

    @Test
    public void testUnsuccessfulCheckoutBookAlreadyCheckedOut() throws IOException {
        Book bookToCheckout = getFirstSampleBook();
        bookToCheckout.checkOut();  // ensure it's already checked out

        bibliotecaApp.checkoutBook(bookToCheckout.getId());

        verify(mockOut).println("Sorry, that book is not available");
    }

    @Test
    public void bookToCheckoutMenuOptionInvalidBookId() throws IOException {
        bibliotecaApp.checkoutBook("Bad ID");
        verify(mockOut).println("Sorry, that book is not available");
    }


    @Test
    public void testReturnBooksOption() throws IOException {
        testUser1LoggedIn(spyApp);
        checkAssertionWhenExiting(() -> {
                verify(spyApp).initiateBookReturn();
        });

        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_RETURN_BOOK_KEY)
                .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testReturnBookMenuOptionValidUserInputs() throws IOException {
        testUser1LoggedIn(spyApp);
        Book checkedOutBook = firstSampleBookIsCheckedOut();

        when(mockReader.readLine()).thenReturn(checkedOutBook.getId());
        spyApp.initiateBookReturn();

        verify(mockOut).println("Please enter the ID of the book that you would like to return: ");
        verify(spyApp).returnBook(testUser1, checkedOutBook.getId());
        assertThat(spyApp.getCheckedOutBooks(testUser1), not(hasItem(checkedOutBook)));
    }

    private Book firstSampleBookIsCheckedOut() {
        Book checkedOutBook = getFirstSampleBook();
        spyApp.recordCheckout(testUser1, checkedOutBook);
        return checkedOutBook;
    }

    @Test
    public void testSuccessfulReturn() throws IOException {
        testUser1LoggedIn(bibliotecaApp);
        Book checkedOutBook = firstSampleBookIsCheckedOut();

        bibliotecaApp.returnBook(testUser1, checkedOutBook.getId());
        verify(mockOut).println("Thank you for returning the book");

        bibliotecaApp.listAvailableBooks();
        verify(mockOut, atLeastOnce()).print(captor.capture());
        assertThat(captor.getAllValues(), hasItem(containsString(checkedOutBook.getTitle())));
    }

    @Test
    public void testUnsuccessfulReturnNoSuchBook() {
        bibliotecaApp.returnBook(testUser1,"Bad ID");
        verify(mockOut).println("That is not a valid book to return.");
    }

    @Test
    public void testUnsuccessfulReturnCheckedOutByDifferentUser() throws IOException {
        testUser1LoggedIn(bibliotecaApp);
        Book checkoutBook = getFirstSampleBook();
        bibliotecaApp.recordCheckout(testUser2, checkoutBook);    // real book but checked out by a DIFFERENT USER

        // When - original user tries to return the same book
        bibliotecaApp.returnBook(testUser1, checkoutBook.getId());

        verify(mockOut).println("That is not a valid book to return.");
        assertThat(bibliotecaApp.getCheckedOutBooks(testUser2), hasItem(checkoutBook));
    }

    @Test
    public void testPostLoginSelectCheckoutMovieMenuOption() throws IOException {
        testUser1LoggedIn(spyApp);
        String checkoutMovieId = getFirstSampleMovie().getId();
        checkAssertionWhenExiting(() -> {
                verify(mockOut).println("Please enter the ID of the movie that you would like to check-out: ");
                verify(spyApp).checkoutMovie(checkoutMovieId);
        });
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_CHECK_OUT_MOVIE_KEY)
                .thenReturn(checkoutMovieId)   // Attempt to checkout the first movie on the list
                .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testSuccessfulCheckoutMovie() throws IOException {
        Movie checkedOutMovie = getFirstSampleMovie();

        bibliotecaApp.checkoutMovie(checkedOutMovie.getId());
        verify(mockOut).println("Thank you! Enjoy the movie");

        bibliotecaApp.listAvailableMovies();
        verify(mockOut, times(0)).println(containsString(checkedOutMovie.getName()));
    }

    @Test
    public void testUnsuccessfulCheckoutNoSuchMovie() throws IOException {
        bibliotecaApp.checkoutMovie("Bad ID");
        verify(mockOut).println("Sorry, that movie is not available");
    }

    @Test
    public void testUnsuccessfulCheckoutMovieAlreadyCheckedOut() throws IOException {
        Movie testCheckoutMovie = getFirstSampleMovie();
        testCheckoutMovie.checkOut();  // ensure it's already checked out

        bibliotecaApp.checkoutMovie(testCheckoutMovie.getId());
        verify(mockOut).println("Sorry, that movie is not available");
    }


    @Test
    public void testGetNotifiedOnInvalidOption() throws IOException {
        // valid option does not include 100
        when(mockReader.readLine()).thenReturn("100").thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);

        checkAssertionWhenExiting(() -> {
                // Then - should notify user and prompt user for input another time
                verify(mockOut).println("Please select a valid option!");
            try {
                verify(mockReader, times(2)).readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bibliotecaApp.start();

    }

    @Test
    public void testPreLoginSelectListMovieMenuOption() throws IOException {
        checkAssertionWhenExiting(() -> {
                verify(spyApp).listAvailableMovies();
        });
        // When
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_LIST_MOVIES_KEY)
                .thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testPostLoginSelectListMovieMenuOption() throws IOException {
        // Given
        testUser1LoggedIn(spyApp);
        checkAssertionWhenExiting(() -> {
                verify(spyApp).listAvailableMovies();
        });

        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_LIST_MOVIES_KEY)
            .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testSelectListMovieMenuOption() throws IOException {
        ArrayList<Movie> testMovies = sampleData.getMovieLibrary().getItems();

        spyApp.listAvailableMovies();

        verify(mockOut, atLeastOnce()).print(captor.capture());
        List<String> printedFields = captor.getAllValues();
        assertMovieHeaderWasPrinted(printedFields);
        assertMoviesAllPrinted(testMovies, printedFields);
    }


    private void assertMoviesAllPrinted(ArrayList<Movie> testMovies, List<String> printedFields) {
        int i = 5;
        for (Movie movieToPrint : testMovies) {
            String[] expectedCols = {
                    movieToPrint.getId(), movieToPrint.getName(),
                    movieToPrint.getYearReleased(), movieToPrint.getDirector(),
                    movieToPrint.getRating() == null ? "unrated" : Integer.toString(movieToPrint.getRating())
            };

            for (int j = 0; j < expectedCols.length; j++) {
                assertThat(printedFields.get(i), containsString(expectedCols[j]));
                i++;
            }
        }
    }

    private void assertMovieHeaderWasPrinted(List<String> printedFields) {
        assertThat(printedFields.get(0), containsString("ID"));
        assertThat(printedFields.get(1), containsString("Name"));
        assertThat(printedFields.get(2), containsString("Year Released"));
        assertThat(printedFields.get(3), containsString("Director"));
        assertThat(printedFields.get(4), containsString("Rating"));
    }

    @Test
    public void testViewUserInfoValidLogin() throws IOException {
        testUser1LoggedIn(spyApp);
        checkAssertionWhenExiting(() -> {
                verify(mockOut, atLeastOnce()).println(captor.capture());

                List<String> printedFields = captor.getAllValues();

                assertThat(printedFields, hasItem(containsString(testUser1.getName())));
                assertThat(printedFields, hasItem(containsString(testUser1.getEmail())));
                assertThat(printedFields, hasItem(containsString(testUser1.getPhone())));
        });

        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_VIEW_USER_INFO_KEY)
            .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testViewCheckedOutBooksValidLogin() throws IOException {
        testUser1LoggedIn(spyApp);
        Book checkoutBook = firstSampleBookIsCheckedOut();
        checkAssertionWhenExiting(() -> {
                verify(mockOut, atLeastOnce()).print(captor.capture());
                assertThat(captor.getAllValues(), hasItem(containsString(checkoutBook.getTitle())));
        });

        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_VIEW_CHECKED_OUT_BOOKS_KEY)
                .thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        spyApp.start();
    }

    @Test
    public void testPreLoginExitOption() throws IOException {
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        checkAssertionWhenExiting(() -> {
            try {
                verify(mockReader, times(1)).readLine();
                verifyNoMoreInteractions(mockReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bibliotecaApp.start();
    }

    @Test
    public void testPostLoginExitOption() throws IOException {
        testUser1LoggedIn(bibliotecaApp);
        when(mockReader.readLine()).thenReturn(BibliotecaApp.POST_LOGIN_EXIT_APP_KEY);
        checkAssertionWhenExiting(() -> {
            try {
                verify(mockReader, times(3)).readLine();
                verifyNoMoreInteractions(mockReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bibliotecaApp.start();

    }

    @Test
    public void testLoopWhenNoExitSelected() throws IOException {
        when(mockReader.readLine()).thenReturn(BibliotecaApp.PRE_LOGIN_LIST_BOOKS_KEY)
                .thenReturn(BibliotecaApp.PRE_LOGIN_LIST_BOOKS_KEY).thenReturn(BibliotecaApp.PRE_LOGIN_EXIT_APP_KEY);
        checkAssertionWhenExiting(() -> {
                verify(spyApp, times(2)).listAvailableBooks();
        });

        spyApp.start();
    }
}