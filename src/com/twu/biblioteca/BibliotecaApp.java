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
    private Library library = new Library();

    static final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
    static final String COL_DIV = "  ";
    static final int COL_WIDTH = 30;
    private static final int ID_COL_WIDTH = 5;
    public static final String LIST_BOOKS_KEY = "1";
    public static final String CHECK_OUT_KEY = "2";
    public static final String EXIT_APP_KEY = "3";

    static final Map<String, String> menu = new HashMap<String, String>() {
        {
            put(LIST_BOOKS_KEY, "List of books");
            put(CHECK_OUT_KEY, "Check-out a book");
            put(EXIT_APP_KEY, "Exit the application");
        }
    };

    public BibliotecaApp(PrintStream printer, BufferedReader reader) {
        this.printer = printer;
        this.reader = reader;
    }

    public BibliotecaApp(Library library) {
        this.library = library;
    }

    public BibliotecaApp(Library library, PrintStream printer, BufferedReader reader) {
        this.printer = printer;
        this.reader = reader;
        this.library = library;
    }

    public Library getLibrary() {
        return library;
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
                    this.listAllBooks();
                    break;
                case CHECK_OUT_KEY:
                    this.listAllBooks();
                    try {
                        this.printer.println("Please enter the ID of the book that you would like to check-out: ");
                        String checkoutId = this.reader.readLine();
                        this.checkoutBook(checkoutId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    public void listAllBooks() {
        this.printer.println(this.getBookListHeader());
        for (Book book : this.library.getAvailableBooks()) {
            String bookString = formatToCol(book.getId(), ID_COL_WIDTH);
            bookString += formatToCol(book.getTitle());
            bookString += formatToCol(book.getAuthorName());
            bookString += formatToCol(book.getYearPublished());
            this.printer.println(bookString);
        }
    }

    private String getBookListHeader() {
        String[] headerFields = new String[]{"Title", "Author", "Year Published"};
        String header = formatToCol("ID", ID_COL_WIDTH);
        for (String field : headerFields) {
            header += formatToCol(field);
        }
        return header;
    }

    private String formatToCol(String s) {
        return String.format("%-" + COL_WIDTH + "s" + COL_DIV, s);
    }

    private String formatToCol(String s, int colWidth) {
        return String.format("%-" + colWidth + "s" + COL_DIV, s);
    }

    public void checkoutBook(String checkoutBookId) {
        Book book = this.library.getBook(checkoutBookId);
        if (book != null && book.isAvailable()) {
            book.checkOut();
            this.printer.println("Thank you! Enjoy the book");
        } else {
            this.printer.println("Sorry, that book is not available");
        }
    }

    public static void main(String[] args) {
        // Populate library with some dummy books for show
        SampleAppData sampleData = new SampleAppData();

        BibliotecaApp app = new BibliotecaApp(sampleData.getLibrary());
        app.start();
    }
}
