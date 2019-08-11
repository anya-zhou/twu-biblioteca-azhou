package com.twu.biblioteca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class BibliotecaApp {
    PrintStream printer = System.out;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Library library = new Library();

    static final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
    static final String COL_DIV = "  ";
    static final int COL_WIDTH = 30;
    public static final String LIST_BOOKS_KEY = "1";
    public static final String EXIT_APP_KEY = "2";

    static final Map<String, String> menu = new HashMap<String, String>() {
        {
            put(LIST_BOOKS_KEY, "List of books");
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

    public void start() {
        this.printer.println(WELCOME_MSG);
        this.showMenu();

        boolean optionExecuted = false;
        while (!optionExecuted) {
            optionExecuted = this.readAndExecuteMenuOption();
        }
    }

    public boolean readAndExecuteMenuOption() {
        try {
            String userInput = this.reader.readLine();
            return this.executeUserSelectedOption(userInput);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showMenu() {
        for (Map.Entry<String, String> menuOption : menu.entrySet()) {
            this.printer.println(menuOption.getKey() + ". " + menuOption.getValue());
        }
        this.printer.println("Please enter the number of the option that you would like to select: ");
    }

    public boolean executeUserSelectedOption(String userInput) {
        boolean optionExecuted = false;
        if (menu.containsKey(userInput)) {
            switch(userInput) {
                case LIST_BOOKS_KEY:
                    this.listAllBooks();
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
        for (Book book : this.library.getBooks()) {
            String bookString = formatToCol(book.getTitle());
            bookString += formatToCol(book.getAuthorName());
            bookString += formatToCol(book.getYearPublished());
            this.printer.println(bookString);
        }
    }

    private String getBookListHeader() {
        return formatToCol("Title") + formatToCol("Author") + formatToCol("Year Published");
    }

    private String formatToCol(String s) {
        return String.format("%-" + COL_WIDTH + "s" + COL_DIV, s);
    }

    public static void main(String[] args) {
        // Populate library with some dummy books for show
        SampleAppData sampleData = new SampleAppData();

        BibliotecaApp app = new BibliotecaApp(sampleData.getLibrary());
        app.start();
    }
}
