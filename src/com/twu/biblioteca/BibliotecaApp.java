package com.twu.biblioteca;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.ListResourceBundle;

public class BibliotecaApp {
    PrintStream printer = System.out;
    Library library = new Library();;
    static final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";

    public BibliotecaApp() {
    }

    public BibliotecaApp(PrintStream printer) {
        this.printer = printer;
    }

    public BibliotecaApp(Library library) {
        this.library = library;
    }

    public BibliotecaApp(Library library, PrintStream printer) {
        this.printer = printer;
        this.library = library;
    }

    public void start() {
        this.printer.println(WELCOME_MSG);
        this.listAllBooks();
    }

    public void listAllBooks() {
        for (Book book : this.library.getBooks()) {
            this.printer.println(book.toString());
        }
    }

    public static void main(String[] args) {
        // Populate library with some dummy books for show
        SampleAppData sampleData = new SampleAppData();

        BibliotecaApp app = new BibliotecaApp(sampleData.getLibrary());
        app.start();
    }
}
