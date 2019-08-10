package com.twu.biblioteca;

import java.io.PrintStream;

public class BibliotecaApp {
    PrintStream printer = System.out;
    Library library = new Library();
    static final String WELCOME_MSG = "Welcome to Biblioteca. Your one-stop-shop for great book titles in Bangalore!";
    static final String COL_DIV = "  ";
    static final int COL_WIDTH = 30;

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
