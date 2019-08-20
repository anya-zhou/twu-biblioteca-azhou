package com.twu.biblioteca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BibliotecaApp {
    private PrintStream printer = System.out;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Library<Book> bookLibrary = new Library<>();
    private Library<Movie> movieLibrary = new Library<>();
    private User loggedInUser;
    private LoginService loginService;
    private Map<User, ArrayList<LibraryItem>> checkoutRecord = new HashMap<>();

    private static final String WELCOME_MSG
        = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
    private static final String LOGIN_ERROR_MSG = "Invalid login details, please try again.";
    private static final String INVALID_OPTION_MSG = "Please select a valid option!";
    private static final String COL_DIV = "  ";
    private static final int COL_WIDTH = 30;

    private Map<String, String> preLoginMenu;
    private Map<String, String> postLoginMenu;

    static final String PRE_LOGIN_LIST_BOOKS_KEY = "1";
    static final String PRE_LOGIN_LIST_MOVIES_KEY = "2";
    static final String LOGIN_KEY = "3";
    static final String PRE_LOGIN_EXIT_APP_KEY = "4";

    static final String POST_LOGIN_LIST_BOOKS_KEY = "1";
    static final String POST_LOGIN_CHECK_OUT_BOOK_KEY = "2";
    static final String POST_LOGIN_RETURN_BOOK_KEY = "3";
    static final String POST_LOGIN_LIST_MOVIES_KEY = "4";
    static final String POST_LOGIN_CHECK_OUT_MOVIE_KEY = "5";
    static final String POST_LOGIN_VIEW_USER_INFO_KEY = "6";
    static final String POST_LOGIN_VIEW_CHECKED_OUT_BOOKS_KEY = "7";
    static final String POST_LOGIN_EXIT_APP_KEY = "8";

    Map<String, String> getMenu() {
        if (isLoggedIn()) {
            if (this.postLoginMenu == null) {
                initializePostLoginMenu();
            }
            return this.postLoginMenu;
        }
        if (this.preLoginMenu == null) {
            initializePreLoginMenu();
        }
        return this.preLoginMenu;
    }

    private void initializePreLoginMenu() {
        this.preLoginMenu = new HashMap<String, String>() {
            {
                put(PRE_LOGIN_LIST_BOOKS_KEY, "List of books");
                put(PRE_LOGIN_LIST_MOVIES_KEY, "List of movies");
                put(LOGIN_KEY, "Login for more options");
                put(PRE_LOGIN_EXIT_APP_KEY, "Exit the application");
            }
        };
    }

    private void initializePostLoginMenu() {
        this.postLoginMenu = new HashMap<String, String>() {
            {
                put(POST_LOGIN_LIST_BOOKS_KEY, "List of books");
                put(POST_LOGIN_CHECK_OUT_BOOK_KEY, "Check-out a book");
                put(POST_LOGIN_RETURN_BOOK_KEY, "Return a book");
                put(POST_LOGIN_LIST_MOVIES_KEY, "List of movies");
                put(POST_LOGIN_CHECK_OUT_MOVIE_KEY, "Check-out a movie");
                put(POST_LOGIN_VIEW_CHECKED_OUT_BOOKS_KEY, "View books checked out");
                put(POST_LOGIN_VIEW_USER_INFO_KEY, "View my information");
                put(POST_LOGIN_EXIT_APP_KEY, "Exit the application");
            }
        };
    }

    BibliotecaApp(Library<Book> bookLibrary, Library<Movie> movieLibrary, LoginService loginService) {
        this.bookLibrary = bookLibrary;
        this.movieLibrary = movieLibrary;
        this.loginService = loginService;
    }

    BibliotecaApp(
        Library<Book> bookLibrary, Library<Movie> movieLibrary,
        PrintStream printer, BufferedReader reader, LoginService loginService
    ) {
        this(bookLibrary, movieLibrary, loginService);
        this.printer = printer;
        this.reader = reader;
    }

    void start() {
        this.printer.println(WELCOME_MSG);
        this.showMenu();

        boolean optionExecuted = false;
        while (true) {
           optionExecuted = this.readAndExecuteMenuOption();
           if (optionExecuted) {
               // Show user the menu again before prompting for input again
               this.printer.println();
               this.showMenu();
           }
        }
    }

    private boolean readAndExecuteMenuOption() {
        try {
            this.printer.print("Select option: ");
            String userInput = this.reader.readLine();
            if (this.loggedInUser == null) {
                return this.executePreLoginOption(userInput);
            }
            return this.executeLoggedInOption(userInput);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void showMenu() {
        this.printer.println("Please enter the number of the option that you would like to select: ");
        for (Map.Entry<String, String> menuOption : this.getMenu().entrySet()) {
            this.printer.println(menuOption.getKey() + ". " + menuOption.getValue());
        }
    }

    private boolean executePreLoginOption(String userInput) {
        boolean optionExecuted = false;
        if (this.getMenu().containsKey(userInput)) {
            switch(userInput) {
                // Assumes unsuccessful action returns user to the main menu, if checkout sub-process should be
                // repeated then this method requires additional while loop
                case PRE_LOGIN_LIST_BOOKS_KEY:
                    this.listAvailableBooks();
                    break;
                case PRE_LOGIN_LIST_MOVIES_KEY:
                    this.listAvailableMovies();
                    break;
                case LOGIN_KEY:
                    this.loginUser();
                    break;
                case PRE_LOGIN_EXIT_APP_KEY:
                    System.exit(0);
                    break;
            }
            optionExecuted = true;

        } else {
            // Invalid menu option entered by user
            this.printer.println(INVALID_OPTION_MSG);
        }
        return optionExecuted;
    }

    private boolean executeLoggedInOption(String userInput) {
        boolean optionExecuted = false;
        if (this.getMenu().containsKey(userInput)) {
            switch(userInput) {
                // Assumes unsuccessful action returns user to the main menu, if checkout sub-process should be
                // repeated then this method requires additional while loop
                case POST_LOGIN_LIST_BOOKS_KEY:
                    this.listAvailableBooks();
                    break;
                case POST_LOGIN_CHECK_OUT_BOOK_KEY:
                    this.initiateBookCheckout();
                    break;
                case POST_LOGIN_RETURN_BOOK_KEY:
                    this.initiateBookReturn();
                    break;
                case POST_LOGIN_LIST_MOVIES_KEY:
                    this.listAvailableMovies();
                    break;
                case POST_LOGIN_CHECK_OUT_MOVIE_KEY:
                    this.initiateMovieCheckout();
                    break;
                case POST_LOGIN_VIEW_CHECKED_OUT_BOOKS_KEY:
                    this.listCheckedOutBooks();
                    break;
                case POST_LOGIN_VIEW_USER_INFO_KEY:
                    this.showUserInformation();
                    break;
                case POST_LOGIN_EXIT_APP_KEY:
                    System.exit(0);
                    break;
            }
            optionExecuted = true;

        } else {
            // Invalid menu option entered by user
            this.printer.println(INVALID_OPTION_MSG);
        }
        return optionExecuted;
    }

    void listAvailableBooks() {
        this.listAvailableItems(this.bookLibrary);
    }

    void listAvailableMovies() {
        this.listAvailableItems(this.movieLibrary);
    }

    void initiateBookCheckout() {
        if (isLoggedIn()) {
            String itemId = getItemIdFromUser("check-out", bookLibrary.getItemDescription());
            this.checkoutBook(itemId);
        }
    }

    User loginUser() {
        try {
            this.printer.println("Please enter your library number in the following format 'xxx-xxxx': ");
            String username = this.reader.readLine();
            this.printer.println("Please enter your password: ");
            String password = this.reader.readLine();
            this.loggedInUser = loginService.login(username, password);
            if (!isLoggedIn()) {
                this.printer.println(LOGIN_ERROR_MSG);
            }
            return loggedInUser;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initiateMovieCheckout() {
        String itemId = getItemIdFromUser("check-out", movieLibrary.getItemDescription());
        this.checkoutMovie(itemId);
    }

    private String getItemIdFromUser(String userAction, String itemDescription) {
        try {
            this.printer.println(
                "Please enter the ID of the " + itemDescription + " that you would like to " + userAction + ": "
            );
            String bookId = this.reader.readLine();
            return bookId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    void initiateBookReturn() {
        if (isLoggedIn()) {
            this.initiateReturn(bookLibrary);
        }
    }

    boolean isLoggedIn() {
        return loggedInUser != null;
    }

    private void initiateReturn(Library library) {
        String itemId = getItemIdFromUser("return", library.getItemDescription());
        this.returnBook(loggedInUser, itemId);
    }

    private void listAvailableItems(Library library) {
        ArrayList<LibraryItem> availableItems = library.getAvailableItems();

        this.printFieldStringsAsLine(library.getHeaderStrings());

        for (int i = 0; i < availableItems.size(); i++) {
            this.printFieldStringsAsLine(availableItems.get(i).getPrintableFieldStrings());
        }
    }

    private void printFieldStringsAsLine(ArrayList<String> fieldStrings) {
        for (String fieldString : fieldStrings) {
            this.printer.print(formatToCol(fieldString));
        }
        this.printer.println();
    }

    private String formatToCol(String s) {
        return String.format("%-" + COL_WIDTH + "s" + COL_DIV, s);
    }

    void checkoutBook(String bookId) {
        this.checkout(bookId, bookLibrary);
    }

    void checkoutMovie(String movieId) {
        this.checkout(movieId, movieLibrary);
    }

    private void checkout(String itemId, Library library) {
        LibraryItem item = library.getItem(itemId);
        if (item != null && item.isAvailable()) {
            this.recordCheckout(loggedInUser, item);
            this.printer.println("Thank you! Enjoy the " + library.getItemDescription());
        } else {
            this.printer.println("Sorry, that " + library.getItemDescription() + " is not available");
        }
    }

    void recordCheckout(User user, LibraryItem item) {
        if (!this.checkoutRecord.containsKey(user)) {
            this.checkoutRecord.put(user, new ArrayList<LibraryItem>());
        }
        this.checkoutRecord.get(user).add(item);
        item.checkOut();
    }

    void returnBook(User user, String bookId) {
        Book book = this.bookLibrary.getItem(bookId);
        // Check that the book exits and was previously checked out by the same user
        if (book != null && this.checkoutRecord.containsKey(user) && this.checkoutRecord.get(user).contains(book)) {
            this.recordReturn(user, book);
            this.printer.println("Thank you for returning the book");
        } else {
            this.printer.println("That is not a valid book to return.");
        }
    }

    private void recordReturn(User user, LibraryItem item) {
        if (this.checkoutRecord.containsKey(user)) {
            this.checkoutRecord.get(user).remove(item);
            item.returning();
        }
    }

    ArrayList<Book> getCheckedOutBooks(User user) {
        ArrayList<LibraryItem> checkedOutItems = this.checkoutRecord.getOrDefault(user, new ArrayList<>());
        ArrayList<Book> checkedOutBooks = new ArrayList<>();
        for (LibraryItem item : checkedOutItems) {
            if (item.getClass() == Book.class) {
                checkedOutBooks.add((Book) item);
            }
        }
        return checkedOutBooks;
    }

    private void showUserInformation() {
        if (isLoggedIn()) {
            this.printer.println("Name: " + loggedInUser.getName());
            this.printer.println("E-mail: " + loggedInUser.getEmail());
            this.printer.println("Phone: " + loggedInUser.getPhone());
        }
    }

    private void listCheckedOutBooks() {
        if (isLoggedIn()) {
            ArrayList<Book> checkedOutBooks = this.getCheckedOutBooks(loggedInUser);

            this.printFieldStringsAsLine(bookLibrary.getHeaderStrings());

            for (Book book : checkedOutBooks) {
                this.printFieldStringsAsLine(book.getPrintableFieldStrings());
            }
        }
    }

    public static void main(String[] args) {
        BibliotecaApp app = initializeLibraries();
        app.start();
    }

    private static BibliotecaApp initializeLibraries() {
        SampleAppData sampleData = new SampleAppData();
        LoginService loginService = new LoginService(sampleData.getUsers());
        return new BibliotecaApp(sampleData.getBookLibrary(), sampleData.getMovieLibrary(), loginService);
    }
}
