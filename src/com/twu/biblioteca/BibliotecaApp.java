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

    static final Map<String, String> menu = new HashMap<String, String>() {
        {
            put("1", "List of books");
        }
    };

    public BibliotecaApp() {
    }

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
        try {
            String userInput = this.reader.readLine();
            this.executeUserSelectedOption(userInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMenu() {
        for (Map.Entry<String, String> menuOption : menu.entrySet()) {
            this.printer.println(menuOption.getKey() + ". " + menuOption.getValue());
        }
        this.printer.println("Please enter the number of the option that you would like to select: ");
    }

    public void executeUserSelectedOption(String userInput) {
        if (userInput.equals("1")) {
            this.listAllBooks();
        }
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
