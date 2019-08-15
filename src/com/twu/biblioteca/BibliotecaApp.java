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

    static final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
    static final String COL_DIV = "  ";
    static final int COL_WIDTH = 30;
    private static final int ID_COL_WIDTH = 5;
    public static final String LIST_BOOKS_KEY = "1";
    public static final String CHECK_OUT_BOOK_KEY = "2";
    public static final String RETURN_BOOK_KEY = "3";
    public static final String LIST_MOVIES_KEY = "4";
    public static final String EXIT_APP_KEY = "5";

    static final Map<String, String> menu = new HashMap<String, String>() {
        {
            put(LIST_BOOKS_KEY, "List of books");
            put(CHECK_OUT_BOOK_KEY, "Check-out a book");
            put(RETURN_BOOK_KEY, "Return a book");
            put(LIST_MOVIES_KEY, "List of movies");
            put(EXIT_APP_KEY, "Exit the application");
        }
    };

    public BibliotecaApp(PrintStream printer, BufferedReader reader) {
        this.printer = printer;
        this.reader = reader;
    }

    public BibliotecaApp(Library<Book> bookLibrary, Library<Movie> movieLibrary) {
        this.bookLibrary = bookLibrary;
        this.movieLibrary = movieLibrary;
    }

    public BibliotecaApp(Library<Book> bookLibrary, PrintStream printer, BufferedReader reader) {
        this(printer, reader);
        this.bookLibrary = bookLibrary;
    }

    public BibliotecaApp(
        Library<Book> bookLibrary, Library<Movie> movieLibrary,
        PrintStream printer, BufferedReader reader
    ) {
        this(bookLibrary, movieLibrary);
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
                case LIST_BOOKS_KEY:
                    this.listAvailableBooks();
                    break;
                case CHECK_OUT_BOOK_KEY:
                    this.initiateCheckOut();
                    break;
                case RETURN_BOOK_KEY:
                    this.initiateReturn();
                    break;
                case LIST_MOVIES_KEY:
                    this.listAvailableItems(movieLibrary);
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

    // Assumes unsuccessful failure returns user to the main menu, if checkout sub-process should be
    // repeated then this method requires additional while loop
    public void initiateCheckOut() {
        String bookId = getBookIdFromUser("check-out");
        this.checkoutBook(bookId);
    }

    private String getBookIdFromUser(String userAction) {
        try {
            this.printer.println(
                "Please enter the ID of the book that you would like to " + userAction + ": "
            );
            String bookId = this.reader.readLine();
            return bookId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initiateReturn() {
        String bookId = getBookIdFromUser("return");
        this.returnBook(bookId);
    }

    public void listAvailableItems(Library library) {
        ArrayList<LibraryItem> availableItems = library.getAvailableItems();

        this.printFieldStringsAsLine(library.getHeaderStrings());

        for (int i = 0; i < availableItems.size(); i++) {
            this.printFieldStringsAsLine(availableItems.get(i).getPrintableFieldStrings());
        }
    }

    public void listAvailableBooks() {
        this.listAvailableItems(this.bookLibrary);
    }

    private void printFieldStringsAsLine(ArrayList<String> fieldStrings) {
        for (String fieldString : fieldStrings) {
            this.printer.print(formatToCol(fieldString, COL_WIDTH));
        }
        this.printer.println();
    }

    private String formatToCol(String s) {
        return String.format("%-" + COL_WIDTH + "s" + COL_DIV, s);
    }

    private String formatToCol(String s, int colWidth) {
        return String.format("%-" + colWidth + "s" + COL_DIV, s);
    }

    public void checkoutBook(String bookId) {
        Book book = this.bookLibrary.getItem(bookId);
        if (book != null && book.isAvailable()) {
            book.checkOut();
            this.printer.println("Thank you! Enjoy the book");
        } else {
            this.printer.println("Sorry, that book is not available");
        }
    }

    public void returnBook(String bookId) {
        Book book = this.bookLibrary.getItem(bookId);
        if (book != null) {
            book.returning();
            this.printer.println("Thank you for returning the book");
        } else {
            this.printer.println("That is not a valid book to return.");
        }
    }

    public static void main(String[] args) {
        // Populate bookLibrary with some dummy books for show
        SampleAppData sampleData = new SampleAppData();

        BibliotecaApp app = new BibliotecaApp(sampleData.getBookLibrary(), sampleData.getMovieLibrary());
        app.start();
    }
}
