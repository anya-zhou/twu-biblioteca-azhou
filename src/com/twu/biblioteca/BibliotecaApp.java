package com.twu.biblioteca;

import sun.rmi.runtime.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BibliotecaApp {
    private PrintStream printer = System.out;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Library<Book> bookLibrary = new Library<>();
    private Library<Movie> movieLibrary = new Library<>();
    private User loggedInUser;
    private LoginService loginService;
    private Map<User, ArrayList<LibraryItem>> checkoutRecord = new HashMap<>();

    static final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
    static final String COL_DIV = "  ";
    static final int COL_WIDTH = 30;
    private static final int ID_COL_WIDTH = 5;
    public static final String LIST_BOOKS_KEY = "1";
    public static final String CHECK_OUT_BOOK_KEY = "2";
    public static final String RETURN_BOOK_KEY = "3";
    public static final String LIST_MOVIES_KEY = "4";
    public static final String CHECK_OUT_MOVIE_KEY = "5";
    public static final String VIEW_CHECKED_OUT_BOOKS_KEY = "6";
    public static final String EXIT_APP_KEY = "7";

    static final Map<String, String> menu = new HashMap<String, String>() {
        {
            put(LIST_BOOKS_KEY, "List of books");
            put(CHECK_OUT_BOOK_KEY, "Check-out a book");
            put(RETURN_BOOK_KEY, "Return a book");
            put(LIST_MOVIES_KEY, "List of movies");
            put(CHECK_OUT_MOVIE_KEY, "Check-out a movie");
            put(VIEW_CHECKED_OUT_BOOKS_KEY, "View books checked out");
            put(EXIT_APP_KEY, "Exit the application");
        }
    };

    public BibliotecaApp(PrintStream printer, BufferedReader reader, LoginService loginService) {
        this.printer = printer;
        this.reader = reader;
        this.loginService = loginService;
    }

    public BibliotecaApp(Library<Book> bookLibrary, Library<Movie> movieLibrary, LoginService loginService) {
        this.bookLibrary = bookLibrary;
        this.movieLibrary = movieLibrary;
        this.loginService = loginService;
    }

    public BibliotecaApp(
        Library<Book> bookLibrary, Library<Movie> movieLibrary,
        PrintStream printer, BufferedReader reader, LoginService loginService
    ) {
        this(bookLibrary, movieLibrary, loginService);
        this.printer = printer;
        this.reader = reader;
    }

    public Library<Book> getBookLibrary() {
        return bookLibrary;
    }

    public Library<Movie> getMovieLibrary() {
        return movieLibrary;
    }

    public void start() {
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

    public boolean readAndExecuteMenuOption() {
        try {
            this.printer.print("Select option: ");
            String userInput = this.reader.readLine();
            return this.executeUserSelectedOption(userInput);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showMenu() {
        this.printer.println("Please enter the number of the option that you would like to select: ");
        for (Map.Entry<String, String> menuOption : menu.entrySet()) {
            this.printer.println(menuOption.getKey() + ". " + menuOption.getValue());
        }
    }

    public boolean executeUserSelectedOption(String userInput) {
        boolean optionExecuted = false;
        if (menu.containsKey(userInput)) {
            switch(userInput) {
                // Assumes unsuccessful action returns user to the main menu, if checkout sub-process should be
                // repeated then this method requires additional while loop
                case LIST_BOOKS_KEY:
                    this.listAvailableBooks();
                    break;
                case CHECK_OUT_BOOK_KEY:
                    this.initiateBookCheckout();
                    break;
                case RETURN_BOOK_KEY:
                    this.initiateBookReturn();
                    break;
                case LIST_MOVIES_KEY:
                    this.listAvailableMovies();
                    break;
                case CHECK_OUT_MOVIE_KEY:
                    this.initiateMovieCheckout();
                    break;
                case EXIT_APP_KEY:
                    System.exit(0);
                    break;
            }
            optionExecuted = true;

        } else {
            // Invalid menu option entered by user
            this.printer.println("Please select a valid option!");
        }
        return optionExecuted;
    }

    public void listAvailableBooks() {
        this.listAvailableItems(this.bookLibrary);
    }

    public void listAvailableMovies() {
        this.listAvailableItems(this.movieLibrary);
    }

    public void initiateBookCheckout() {
        loggedInUser = this.loginUser();
        if (loggedInUser != null) {
            String itemId = getItemIdFromUser("check-out", bookLibrary.getItemDescription());
            this.checkoutBook(itemId);
        }
    }

    private User loginUser() {
        try {
            this.printer.println("Please enter your library number in the following format 'xxx-xxxx': ");
            String username = this.reader.readLine();
            this.printer.println("Please enter your password: ");
            String password = this.reader.readLine();
            return loginService.login(username, password);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initiateMovieCheckout() {
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

    public void initiateBookReturn() {
        loggedInUser = this.loginUser();
        if (loggedInUser != null) {
            this.initiateReturn(bookLibrary);
        }
    }

    public void initiateReturn(Library library) {
        String itemId = getItemIdFromUser("return", library.getItemDescription());
        this.returnBook(loggedInUser, itemId);
    }

    public void listAvailableItems(Library library) {
        ArrayList<LibraryItem> availableItems = library.getAvailableItems();

        this.printFieldStringsAsLine(library.getHeaderStrings());

        for (int i = 0; i < availableItems.size(); i++) {
            this.printFieldStringsAsLine(availableItems.get(i).getPrintableFieldStrings());
        }
    }

    private void printFieldStringsAsLine(ArrayList<String> fieldStrings) {
        for (String fieldString : fieldStrings) {
            this.printer.print(formatToCol(fieldString, COL_WIDTH));
        }
        this.printer.println();
    }

    private String formatToCol(String s, int colWidth) {
        return String.format("%-" + colWidth + "s" + COL_DIV, s);
    }

    public void checkoutBook(String bookId) {
        this.checkout(bookId, bookLibrary);
    }

    public void checkoutMovie(String movieId) {
        this.checkout(movieId, movieLibrary);
    }

    public void checkout(String itemId, Library library) {
        LibraryItem item = library.getItem(itemId);
        if (item != null && item.isAvailable()) {
            this.recordCheckout(loggedInUser, item);
            this.printer.println("Thank you! Enjoy the " + library.getItemDescription());
        } else {
            this.printer.println("Sorry, that " + library.getItemDescription() + " is not available");
        }
    }

    protected void recordCheckout(User user, LibraryItem item) {
        if (!this.checkoutRecord.containsKey(user)) {
            this.checkoutRecord.put(user, new ArrayList<LibraryItem>());
        }
        this.checkoutRecord.get(user).add(item);
        item.checkOut();
    }

    public void returnBook(User user, String bookId) {
        Book book = this.bookLibrary.getItem(bookId);
        if (book != null) {
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

    public User getUser(String userId) {
        return loginService.getUser(userId);
    }

    public ArrayList<Book> getCheckedOutBooks(User user) {
        this.printer.println(user);
        ArrayList<Book> checkedoutBooks = new ArrayList<>();
        if (this.checkoutRecord.containsKey(user)) {
            for (LibraryItem item : this.checkoutRecord.get(user)) {
                if (item.getClass() == Book.class) {
                    checkedoutBooks.add((Book) item);
                }
            }
        }
        return checkedoutBooks;
    }

    public static void main(String[] args) {
        // Populate bookLibrary with some dummy books for show
        SampleAppData sampleData = new SampleAppData();
        LoginService loginService = new LoginService(sampleData.getUsers());
        BibliotecaApp app = new BibliotecaApp(sampleData.getBookLibrary(), sampleData.getMovieLibrary(), loginService);
        app.start();
    }
}
